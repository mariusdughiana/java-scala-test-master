import AssemblyKeys._

name := "recs-api"

version := "1.0"

scalaVersion := "2.11.3"

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "ch.qos.logback" % "logback-classic" % "1.0.13",
  "com.typesafe.akka" %% "akka-actor" % "2.3.9",
  "com.typesafe.akka" %% "akka-slf4j" % "2.3.9",
  "io.spray" %% "spray-can" % "1.3.3",
  "io.spray" %% "spray-routing" % "1.3.3",
  "org.apache.httpcomponents" % "httpclient" % "4.5.1",
  "org.json4s" %% "json4s-native" % "3.2.11",
  "org.scalatest" %% "scalatest" % "2.2.0" % "test",
  "io.spray" %% "spray-testkit" % "1.3.1" % "test",
  "org.specs2" %% "specs2" % "2.3.13" % "test",
  "com.github.tomakehurst" % "wiremock" % "1.36" % "test"
)


assemblySettings

mainClass in assembly := Some("com.sky.assignment.Application")

