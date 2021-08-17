import Dependencies._

ThisBuild / scalaVersion := "2.13.6"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "dev.myclinic.scala"
ThisBuild / organizationName := "myclinic"

val http4sVersion = "0.23.1"
val doobieVersion = "1.0.0-M5"

lazy val root = project.in(file("."))
  .aggregate(app.js, app.jvm)
  .settings(
    publish := {},
    publishLocal := {}
  )

lazy val app = crossProject(JSPlatform, JVMPlatform).in(file("."))
  .settings(
    name := "app"
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % "1.1.3" % "runtime",
      "mysql" % "mysql-connector-java" % "8.0.19",
      "org.tpolecat" %% "doobie-core" % doobieVersion,
      "org.tpolecat" %% "doobie-hikari" % doobieVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion
    )
  )
  .jsSettings(
    scalaJSUseMainModuleInitializer := true
  )
