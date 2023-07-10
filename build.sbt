ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

lazy val root = (project in file("."))
  .settings(
    name := "scala-mongo"
  )

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "reactivemongo" % "1.1.0-RC10",
  "org.slf4j" % "slf4j-api" % "1.7.32"
)