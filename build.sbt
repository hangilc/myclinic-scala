import Dependencies._

ThisBuild / scalaVersion := "2.13.6"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "dev.myclinic.scala"
ThisBuild / organizationName := "myclinic"

lazy val root = (project in file("."))
  .settings(
    name := "myclinic-scala",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.19",
    libraryDependencies ++= Seq(
      "org.tpolecat" %% "doobie-core" % "0.12.1",
      "org.tpolecat" %% "doobie-hikari" % "0.12.1"
    )
  )
