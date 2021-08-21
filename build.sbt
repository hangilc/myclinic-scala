import Dependencies._

ThisBuild / scalaVersion := "2.13.6"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "dev.myclinic.scala"
ThisBuild / organizationName := "myclinic"

val sqliteVersion = "3.36.0.1"
val mysqlVersion = "8.0.19"
val http4sVersion = "1.0.0-M21"
val doobieVersion = "1.0.0-M5"
val circeVersion = "0.14.1"
val scalaJavaTimeVersion = "2.2.2"
val scalaJSDomVersion = "1.1.0"

val rootDir = ThisBuild / baseDirectory

lazy val root = project.in(file("."))
  .aggregate(db, server, appointApp, modelJS, modelJVM, utilJS, utilJVM)
  .settings(
    publish := {},
    publishLocal := {},
  )

lazy val db = project.in(file("db"))
  .dependsOn(modelJVM)
  .settings(
    name := "db",
    libraryDependencies ++= Seq(
      "org.xerial" % "sqlite-jdbc" % sqliteVersion,
      "org.tpolecat" %% "doobie-core" % doobieVersion,
      "org.tpolecat" %% "doobie-hikari" % doobieVersion,
    )
  )

lazy val server = project.in(file("server"))
  .dependsOn(db, modelJVM, utilJVM)
  .settings(
    name := "server",
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % "1.1.3" % "runtime",
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "io.circe" %% "circe-generic" % circeVersion,
    )
  )

lazy val appointApp = project.in(file("appoint-app"))
  .dependsOn(modelJS, utilJS, utilJS)
  .settings(
    name := "myclinic-appoint",
      scalaJSUseMainModuleInitializer := true,
      Compile / fastLinkJS / scalaJSLinkerOutputDirectory := 
        (rootDir.value / "server" / "web" / "appoint" / "scalajs"),
      libraryDependencies ++= Seq(
        "org.scala-js" %%% "scalajs-dom" % scalaJSDomVersion,
      ),
  )

lazy val model = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("model"))
  .settings(
    name := "model",
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-core" % circeVersion,
      "io.circe" %%% "circe-generic" % circeVersion,
      "io.circe" %%% "circe-parser" % circeVersion,
    ),
  )
  .jsSettings(
      scalaJSUseMainModuleInitializer := false,
      zonesFilter := {(z: String) => z == "Asia/Tokyo"},
      libraryDependencies ++= Seq(
        "io.github.cquiroz" %%% "scala-java-time" % scalaJavaTimeVersion,
      ),
  )

val modelJS = model.js
val modelJVM = model.jvm

lazy val util = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("util"))
  .jsSettings(
      scalaJSUseMainModuleInitializer := false,
      zonesFilter := {(z: String) => z == "Asia/Tokyo"},
      libraryDependencies ++= Seq(
        "io.github.cquiroz" %%% "scala-java-time" % scalaJavaTimeVersion,
      ),
  )

val utilJS = util.js
val utilJVM = util.jvm
