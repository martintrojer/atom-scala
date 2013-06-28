//  Copyright (c) Martin Trojer. All rights reserved.
//  The use and distribution terms for this software are covered by the
//  Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
//  which can be found in the file epl-v10.html at the root of this distribution.
//  By using this software in any fashion, you are agreeing to be bound by
//  the terms of this license.
//  You must not remove this notice, or any other, from this software.

package Atom

import org.scalatest.FunSuite

class AtomTest extends FunSuite {

  test("creation") {
    expectResult(0) { Atom(0).get }
    expectResult("hello") { Atom("hello").get }
    expectResult(List('foo)) { Atom(List('foo)).get }
  }

  test("swap") {
    expectResult(1) { Atom(0).swap { _ + 1 } }
    expectResult(1) {
      val a = Atom(0)
      a.swap { _ + 1 }
      a.get
    }
    expectResult("HelloWorld") { Atom("Hello").swap { _ + "World" } }
  }

  test("reset") {
    expectResult(42) {
      val a = Atom(0)
      a.reset(42)
      a.get
    }
  }

  test("foreach") {
    expectResult(42) {
      var res = 0
      val a = Atom(42)
      for ( v <- a ) { res = res + v }
      res
    }
  }

  test("map") {
    expectResult(Atom(43)) {
      val a = Atom(42)
      for ( v <- a ) yield v + 1
    }

    expectResult(Atom(43)) {
      Atom(42) map { _ + 1 }
    }
  }

  test("flatMap") {
    expectResult(Atom(42 + 43)) {
      val a1 = Atom(42)
      val a2 = Atom(43)
      for {
        v1 <- a1
        v2 <- a2
      } yield v1 + v2
    }
  }

}
