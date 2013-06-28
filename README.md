# atom-scala

Clojure-style atoms for Scala

Atoms manage shared, synchronous, independent state. Intended to hold immutable data-structures.

* Thread safe
* Uses uses a CAS (compare and swap) for safe updates
* Fast
* Covers 90% of your state managements needs (based my empirical evidence)

## Artifacts

Add the following lines in your build.sbt;

```
resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies += "io.github.martintrojer" % "atom-scala_2.10" % "0.1-SNAPSHOT"
```

## Usage

The main function `swap` takes a side-effect free function that gets called with the old state and produces the new state. Please be aware, this function can be called multiple times!

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

Supports Monadic Usage

```scala
// map
scala> Atom(42) map { _ + 1 }
res3: Atom.Atom[Int] = 43

scala> for ( v <- Atom(Set('a)) ) yield v + 'b
res4: Atom.Atom[scala.collection.immutable.Set[Symbol]] = Set('a, 'b)

// flatMap
val a1 = Atom(Map('a -> 1))
val a2 = Atom(Map('b -> 2))
for {
  v1 <- a1
  v2 <- a2
} yield (v1 ++ v2)
res5: Atom.Atom[scala.collection.immutable.Map[Symbol,Int]] = Map('a -> 1, 'b -> 2)

// foreach
var res = 0
val a = Atom(42)
for ( v <- a ) {
  res = res + v
}
res: Int = 42
```

## References

* [Clojure Atoms](http://clojure.org/atoms)
* [Akka Agents](http://doc.akka.io/docs/akka/2.1.0/scala/agents.html) (inspired by Clojure's Agents)

## License

Copyright (C) 2013 Martin Trojer

Distributed under the Eclipse Public License.
