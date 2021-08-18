import Dependencies._

ThisBuild / scalaVersion := "2.13.6"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "dev.myclinic.scala"
ThisBuild / organizationName := "myclinic"

val mysqlVersion = "8.0.19"
val http4sVersion = "0.23.1"
val doobieVersion = "1.0.0-M5"

lazy val root = project.in(file("."))
  .aggregate(server, appointApp)
  .settings(
  )

lazy val server = project.in(file("server"))
  .settings(
      name := "myclinic-server",
      libraryDependencies ++= Seq(
        "ch.qos.logback" % "logback-classic" % "1.1.3" % "runtime",
        "mysql" % "mysql-connector-java" % mysqlVersion,
        "org.tpolecat" %% "doobie-core" % doobieVersion,
        "org.tpolecat" %% "doobie-hikari" % doobieVersion,
        "org.http4s" %% "http4s-dsl" % http4sVersion,
        "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      )
  )

lazy val appointApp = project.in(file("appoint-app"))
  .settings(
    name := "myclinic-appoint",
      scalaJSUseMainModuleInitializer := true,
  )

