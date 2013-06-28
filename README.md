# atom-scala

Clojure-style atoms for Scala

Atoms manage shared, synchronous, independent state. Intended to hold immutable data-structures.

* Thread safe
* Uses uses a CAS (compare and swap) to safely update (under load)
* Fast
* Covers 90% of your state managements needs

## Artifacts

Add the following lines in your build.sbt;

```
resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies += "io.github.martintrojer" % "atom-scala_2.10" % "0.1-SNAPSHOT"
```

## Usage

The main function `swap` takes a side-effect free function that gets called with the old state and produces new. Can be called multiple times.

The `reset` function simply overwrites the old value.

```scala
scala> import Atom._
import Atom._

scala> val a = Atom(1)
a: Atom.Atom[Int] = 1

scala> a swap { _ + 1 }
res0: Int = 2

scala> a.get
res1: Int = 2

scala> a.reset(42)

scala> a.get
res2: Int = 42
```

Also supports Monadic Usage

```scala
// map
scala> Atom(42) map { _ + 1 }
res3: Atom.Atom[Int] = 43

scala> for ( v <- Atom(42) ) yield v + 1
res4: Atom.Atom[Int] = 43

// flatMap
val a1 = Atom(42)
val a2 = Atom(43)
for {
  v1 <- a1
  v2 <- a2
} yield v1 + v2
res5: Atom.Atom[Int] = 85

// foreach
var res = 0
val a = Atom(42)
for ( v <- a ) {
  res = res + v
}
res: Int = 42
```

Also see the [tests](https://github.com/martintrojer/atom-scala/blob/master/src/test/scala/Atom/AtomTest.scala).

## References

* [Clojure Atoms](http://clojure.org/atoms)
* [Akka Agents](http://doc.akka.io/docs/akka/2.1.0/scala/agents.html) (inspired by Clojure's Agents)

## License

Copyright (C) 2013 Martin Trojer

Distributed under the Eclipse Public License.
