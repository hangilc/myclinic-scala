import Dependencies._

ThisBuild / scalaVersion := "2.13.6"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "dev.myclinic.scala"
ThisBuild / organizationName := "myclinic"

val sqliteVersion = "3.36.0.1"
val mysqlVersion = "8.0.19"
//val http4sVersion = "1.0.0-M21"
val http4sVersion = "0.23.1"
val doobieVersion = "1.0.0-M5"
//val circeVersion = "0.14.1"
val scalaJavaTimeVersion = "2.2.2"
val scalaJSDomVersion = "1.1.0"

ThisBuild / scalacOptions ++= Seq("-Wunused", "-deprecation")

val rootDir = ThisBuild / baseDirectory

lazy val root = project
  .in(file("."))
  .aggregate(
    db,
    server,
    appointApp,
    modelJS,
    modelJVM,
    utilJS,
    utilJVM,
    clientJS,
    clientJVM,
    apiJS,
    apiJVM
  )
  .settings(
    publish := {},
    publishLocal := {}
  )

lazy val db = project
  .in(file("db"))
  .dependsOn(modelJVM)
  .settings(
    name := "db",
    libraryDependencies ++= Seq(
      "org.xerial" % "sqlite-jdbc" % sqliteVersion,
      "org.tpolecat" %% "doobie-core" % doobieVersion,
      "org.tpolecat" %% "doobie-hikari" % doobieVersion
    )
  )

lazy val server = project
  .in(file("server"))
  .dependsOn(db, modelJVM, utilJVM, apiJVM)
  .settings(
    name := "server",
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % "1.1.3" % "runtime",
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      //"org.http4s" %% "http4s-circe" % http4sVersion,
      //"io.circe" %% "circe-generic" % circeVersion,
      //"org.endpoints4s" %% "http4s-server" % "7.0.0",
      "org.endpoints4s" %% "openapi" % "3.1.0"
    )
  )

lazy val appointApp = project
  .in(file("appoint-app"))
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(domq, modelJS, utilJS, utilJS, clientJS, webclient)
  .settings(
    name := "myclinic-appoint",
    scalaJSUseMainModuleInitializer := true,
    Compile / fastLinkJS / scalaJSLinkerOutputDirectory :=
      (rootDir.value / "server" / "web" / "appoint" / "scalajs"),
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % scalaJSDomVersion
    )
  )

lazy val domq = project
  .in(file("domq"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "domq",
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % scalaJSDomVersion
    )
  )

lazy val model = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("model"))
  .settings(
    name := "model",
    // libraryDependencies ++= Seq(
    //   "io.circe" %%% "circe-core" % circeVersion,
    //   "io.circe" %%% "circe-generic" % circeVersion,
    //   "io.circe" %%% "circe-parser" % circeVersion
    // )
  )
  .jsSettings(
    scalaJSUseMainModuleInitializer := false,
    zonesFilter := { (z: String) => z == "Asia/Tokyo" },
    libraryDependencies ++= Seq(
      "io.github.cquiroz" %%% "scala-java-time" % scalaJavaTimeVersion
    )
  )

val modelJS = model.js
val modelJVM = model.jvm

lazy val util = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("util"))
  .jsSettings(
    scalaJSUseMainModuleInitializer := false,
    zonesFilter := { (z: String) => z == "Asia/Tokyo" },
    libraryDependencies ++= Seq(
      "io.github.cquiroz" %%% "scala-java-time" % scalaJavaTimeVersion
    )
  )

val utilJS = util.js
val utilJVM = util.jvm

lazy val client = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .in(file("client"))
  .settings(
  )
  .jvmSettings(
  )
  .jsSettings(
    scalaJSUseMainModuleInitializer := false,
    zonesFilter := { (z: String) => z == "Asia/Tokyo" },
    libraryDependencies ++= Seq(
      "io.github.cquiroz" %%% "scala-java-time" % scalaJavaTimeVersion
    )
  )

val clientJS = client.js
val clientJVM = client.jvm

lazy val api = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("api"))
  .dependsOn(model)
  .settings(
    libraryDependencies ++= Seq(
      "org.endpoints4s" %%% "algebra" % "1.5.0",
      "org.endpoints4s" %%% "json-schema-generic" % "1.5.0",
      //"org.endpoints4s" %% "json-schema-circe" % "1.5.0",
      //"org.endpoints4s" %%% "algebra-circe" % "1.5.0",
   )
  )

val apiJS = api.js
val apiJVM = api.jvm

lazy val webclient = project
  .in(file("webclient"))
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(apiJS)
  .settings(
    name := "webclient",
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "org.endpoints4s" %%% "xhr-client" % "3.1.0"
    )
  )
