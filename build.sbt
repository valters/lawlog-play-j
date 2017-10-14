name := """law-log-j"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.3"

libraryDependencies += guice

// bootstrap
libraryDependencies += "org.webjars" % "jquery" % "2.2.4"
libraryDependencies += "org.webjars" % "bootstrap" % "3.3.7-1" exclude("org.webjars", "jquery")
libraryDependencies += "org.webjars" % "font-awesome" % "4.7.0"

libraryDependencies += "org.webjars" % "vue" % "2.4.2"

// xml
libraryDependencies += "io.github.valters" % "valters-xml" % "1.0.0"


// Test Database
libraryDependencies += "com.h2database" % "h2" % "1.4.194"

// Testing libraries for dealing with CompletionStage...
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test

// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
