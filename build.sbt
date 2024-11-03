ThisBuild / scalaVersion := "3.1.3"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "dev.myclinic.scala"
ThisBuild / organizationName := "myclinic"
ThisBuild / resolvers += Resolver.mavenLocal

val mysqlVersion = "6.0.6"
val slf4jVersion = "2.0.16"
val logbackVersion = "1.5.12"
val circeVersion = "0.14.2"
val circeYamlVersion = "0.14.1"
val jacksonVersion = "2.14.3"
// val jacksonVersion = "2.13.3"
val catsVersion = "2.12.0"
val fs2Version = "3.11.0"
val http4sVersion = "0.23.12"
val doobieVersion = "1.0.0-RC2"
val scalaJavaTimeVersion = "2.3.0"
val scalaJSDomVersion = "2.2.0"
val macrotaskExecutorVersion = "1.0.0"
val scalaLoggingVersion = "3.9.4"
val scalaTestVersion = "3.2.10"

ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-feature"
)
ThisBuild / javacOptions ++= Seq("-encoding", "UTF-8")

val rootDir = ThisBuild / baseDirectory

lazy val root = project
  .in(file("."))
  .aggregate(
    appUtil,
    appointAdmin,
    clinicop,
    config,
    db,
    drawerscala,
    drawerform,
    formatshohousen,
    holidayjp,
    javalib,
    kanjidate,
    model,
    rcpt,
    server,
    util
  )
  .settings(
    publish := {},
    publishLocal := {}
  )

lazy val formatshohousen = project
  .in(file("formatshohousen"))
  .dependsOn(util)
  .settings(
    name := "formatshohousen",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
    ),
    Test / logBuffered := false
  )

lazy val model = project
  .in(file("model"))
  .dependsOn(util, holidayjp, clinicop, drawerscala)
  .settings(
    name := "model",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
    )
  )

lazy val db = project
  .in(file("db"))
  .dependsOn(model)
  .settings(
    name := "db",
    libraryDependencies ++= Seq(
      "mysql" % "mysql-connector-java" % mysqlVersion,
      "org.tpolecat" %% "doobie-core" % doobieVersion,
      "org.tpolecat" %% "doobie-hikari" % doobieVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "io.circe" %% "circe-core" % circeVersion
    )
  )

lazy val util = project
  .in(file("util"))
  .settings(
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
    )
  )

lazy val server = project
  .in(file("server"))
  .enablePlugins(PackPlugin)
  .dependsOn(
    db,
    model,
    util,
    appointAdmin,
    clinicop,
    rcpt,
    javalib,
    config,
    drawerscala,
    drawerform,
    formatshohousen
  )
  .settings(
    name := "server",
    resolvers += Resolver.mavenLocal,
    libraryDependencies ++= Seq(
      "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
      "ch.qos.logback" % "logback-classic" % logbackVersion,
      //"ch.qos.logback" % "logback-classic" % "1.1.3" ,
      //"ch.qos.logback" % "logback-classic" % "1.1.3" % "runtime",
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-server" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "dev.fujiwara" % "drawer" % "1.0.1-SNAPSHOT",
      "com.twilio.sdk" % "twilio" % "8.34.1"
    )
  )

lazy val appointAdmin = project
  .in(file("appoint-admin"))
  .dependsOn(model, db, util, clinicop)

lazy val holidayjp = project
  .in(file("holidayjp"))
  .dependsOn(util, kanjidate)

lazy val clinicop = project
  .in(file("clinicop"))
  .dependsOn(util, holidayjp)

lazy val rcpt = project
  .in(file("rcpt"))
  .dependsOn(javalib, model, appUtil)

lazy val javalib = project
  .in(file("javalib"))
  .dependsOn(model, appUtil)
  .settings(
    name := "javalib",
    version := "1.0.0-SHANPSHOT",
    libraryDependencies ++= Seq(
      "org.slf4j" % "slf4j-api" % slf4jVersion,
      "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
      "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonVersion,
      "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % jacksonVersion,
      "com.fasterxml.jackson.dataformat" % "jackson-dataformat-xml" % jacksonVersion,
      "dev.fujiwara" % "drawer" % "1.0.1-SNAPSHOT"
    )
  )

lazy val config = project
  .in(file("config"))
  .dependsOn(javalib, model, clinicop)
  .settings(
    name := "config",
    libraryDependencies ++= Seq(
      "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
      "io.circe" %% "circe-yaml" % circeYamlVersion
    )
  )

lazy val appUtil = project
  .in(file("app-util"))
  .dependsOn(util, model, kanjidate)

lazy val drawerscala = project
  .in(file("drawer"))
  .settings(
    name := "drawerscala",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "dev.fujiwara" % "drawer" % "1.0.0-SNAPSHOT"
    )
  )

lazy val drawerform = project
  .in(file("drawerform"))
  .dependsOn(model, kanjidate)
  .settings(
    name := "drawerform",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "dev.fujiwara" % "drawer" % "1.0.0-SNAPSHOT"
    )
  )

lazy val kanjidate = project
  .in(file("kanjidate"))
