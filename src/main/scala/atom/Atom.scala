//  Copyright (c) Martin Trojer. All rights reserved.
//  The use and distribution terms for this software are covered by the
//  Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
//  which can be found in the file epl-v10.html at the root of this distribution.
//  By using this software in any fashion, you are agreeing to be bound by
//  the terms of this license.
//  You must not remove this notice, or any other, from this software.

package Atom

import java.util.concurrent.atomic.AtomicReference

/** Atoms manage shared, synchronous, independent state.
  * Intended to hold immutable data-structures, thread-safe.
  *
  * Access the contents of an atom with the get function,
  * use swap to update the state and reset to overwrite.
  *
  * {{{
  *   scala> val a = Atom(1)
  *   a: frins.Atom[Int] = 1
  *   scala> a swap { _ + 1 }
  *   res0: Int = 2
  *   scala> a.get
  *   res1: Int = 2
  * }}}
  *
  * Also supports Monadic Usage
  *
  *  {{{
  *     // foreach
  *     var res = 0
  *     val a = Atom(42)
  *     for ( v <- a ) { res = res + v }
  *
  *     // map
  *     Atom(42) map { _ + 1 }
  *     for ( v <- Atom(42) ) yield v + 1
  *
  *     // flatMap
  *     val a1 = Atom(42)
  *     val a2 = Atom(43)
  *     for {
  *       v1 <- a1
  *       v2 <- a2
  *     } yield v1 + v2
  *  }}}
  *
  * See, http://clojure.org/atoms
  *
  * @param state initial state
  * @tparam T type of contained state
  */

class Atom[T](private val state: AtomicReference[T]) {

  /** Get the contained state (or de-ref the atom) */
  def get() = state.get()

  /** Alter (swap in) the state
    *
    * @param f Side-effect free function that gets called with the old state and produces new.
    *          Can be called multiple times.
    * @return New state after successful update
    */
  def swap(f: T => T): T = {
    val v = state.get()
    val newv = f(v)
    if (state.compareAndSet(v, newv)) newv else swap(f)
  }

  /** Overwrite the state
    *
    * @param nv New value of the state
    */
  def reset(nv: T) = state.set(nv)

  // ----

  def map[B](f: T => B): Atom[B] = Atom(f(get))

  def flatMap[B](f: T => Atom[B]): Atom[B] = f(get)

  def foreach[U](f: T => U): Unit = f(get)

  // ----

  override def toString() = get.toString
  override def equals(that: Any) = that match {
    case that: Atom[T]  => get == that.get
    case _              => false
  }
}

object Atom {
  def apply[T](state: T) = new Atom[T](new AtomicReference[T](state))
}
