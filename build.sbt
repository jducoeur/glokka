organization := "tv.cntt"

name := "glokka"

version := "1.3-SNAPSHOT"

scalaVersion := "2.10.3"

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked"
)

// http://www.scala-sbt.org/release/docs/Detailed-Topics/Java-Sources
// Avoid problem when Glokka is built with Java 7 but the projects that use Glokka
// are run with Java 6
javacOptions ++= Seq("-source", "1.6", "-target", "1.6")

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.2.1"

libraryDependencies += "com.typesafe.akka" %% "akka-cluster" % "2.2.1"

libraryDependencies += "com.typesafe.akka" %% "akka-contrib" % "2.2.1"

libraryDependencies += "org.specs2" %% "specs2" % "2.1.1" % "test"

// For "sbt console" used while developing for cluster mode
unmanagedClasspath in Compile <+= (baseDirectory) map { bd => Attributed.blank(bd / "config_example") }

//------------------------------------------------------------------------------

// Skip API doc generation to speedup "publish-local" while developing.
// Comment out this line when publishing to Sonatype.
publishArtifact in (Compile, packageDoc) := false
