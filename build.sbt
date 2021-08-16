import Dependencies._

ThisBuild / scalaVersion := "2.13.6"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "dev.myclinic.scala"
ThisBuild / organizationName := "myclinic"

val http4sVersion = "0.21.15"
val doobieVersion = "0.10.0"

lazy val root = (project in file("."))
  .settings(
    name := "myclinic-scala",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.19",
    libraryDependencies ++= Seq(
      "org.tpolecat" %% "doobie-core" % doobieVersion,
      "org.tpolecat" %% "doobie-hikari" % doobieVersion
    ),
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion
    )
  )
