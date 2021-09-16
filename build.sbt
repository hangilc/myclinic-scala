import Dependencies._

ThisBuild / scalaVersion := "3.0.2"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "dev.myclinic.scala"
ThisBuild / organizationName := "myclinic"

val sqliteVersion = "3.36.0.1"
val mysqlVersion = "8.0.19"
val http4sVersion = "0.23.3"
val doobieVersion = "1.0.0-M5"
val circeVersion = "0.14.1"
val scalaJavaTimeVersion = "2.3.0"
val scalaJSDomVersion = "1.1.0"
val tapirVersion = "0.19.0-M8"
val tapirDocVersion = "0.19.0-M4"

ThisBuild / scalacOptions ++= Seq("-deprecation", "-encoding", "UTF-8", "-new-syntax", "-indent")

val rootDir = ThisBuild / baseDirectory

lazy val root = project
  .in(file("."))
  .aggregate(
    modelJS,
    modelJVM,
    db,
    utilJVM,
    utilJS,
    server,
    webclient,
    domq,
    modeljsonJVM,
    modeljsonJS,
    appointApp
  )
  .settings(
    publish := {},
    publishLocal := {}
  )

lazy val model = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("model"))
  .settings(
    name := "model"
  )
  .jsConfigure(_ enablePlugins TzdbPlugin)
  .jsSettings(
    scalaJSUseMainModuleInitializer := false,
    zonesFilter := { (z: String) => z == "Asia/Tokyo" },
    libraryDependencies ++= Seq(
      "io.github.cquiroz" %%% "scala-java-time" % scalaJavaTimeVersion
    )
  )

val modelJS = model.js
val modelJVM = model.jvm

lazy val db = project
  .in(file("db"))
  .dependsOn(modelJVM, modeljsonJVM)
  .settings(
    name := "db",
    libraryDependencies ++= Seq(
      "org.xerial" % "sqlite-jdbc" % sqliteVersion,
      "org.tpolecat" %% "doobie-core" % doobieVersion,
      "org.tpolecat" %% "doobie-hikari" % doobieVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion
    ),
  )

lazy val util = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("util"))
  .jsConfigure(_ enablePlugins TzdbPlugin)
  .jsSettings(
    scalaJSUseMainModuleInitializer := false,
    zonesFilter := { (z: String) => z == "Asia/Tokyo" },
    libraryDependencies ++= Seq(
      "io.github.cquiroz" %%% "scala-java-time" % scalaJavaTimeVersion
    )
  )

val utilJS = util.js
val utilJVM = util.jvm

lazy val server = project
  .in(file("server"))
  .dependsOn(db, modelJVM, utilJVM, modeljsonJVM)
  .settings(
    name := "server",
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % "1.1.3" % "runtime",
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion
    ),
  )

lazy val webclient = project
  .in(file("webclient"))
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(modelJS, modeljsonJS, utilJS)
  .settings(
    name := "webclient",
    scalaJSUseMainModuleInitializer := false,
    libraryDependencies ++= Seq(
      ("org.scala-js" %%% "scalajs-dom" % "1.2.0")
        .cross(CrossVersion.for3Use2_13)
    )
  )

lazy val domq = project
  .in(file("domq"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "domq",
    libraryDependencies ++= Seq(
      ("org.scala-js" %%% "scalajs-dom" % scalaJSDomVersion)
        .cross(CrossVersion.for3Use2_13)
    )
  )

lazy val appointApp = project
  .in(file("appoint-app"))
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(domq, modelJS, utilJS, utilJS, webclient)
  .settings(
    name := "myclinic-appoint",
    scalaJSUseMainModuleInitializer := true,
    Compile / fastLinkJS / scalaJSLinkerOutputDirectory :=
      (rootDir.value / "server" / "web" / "appoint" / "scalajs"),
    libraryDependencies ++= Seq(
      ("org.scala-js" %%% "scalajs-dom" % scalaJSDomVersion)
        .cross(CrossVersion.for3Use2_13)
    )
  )

lazy val modeljson = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("modeljson"))
  .dependsOn(model)
  .jsConfigure(_ enablePlugins TzdbPlugin)
  .jsSettings(
    scalaJSUseMainModuleInitializer := false,
    zonesFilter := { (z: String) => z == "Asia/Tokyo" },
    libraryDependencies ++= Seq(
      "io.github.cquiroz" %%% "scala-java-time" % scalaJavaTimeVersion
    )
  )
  .settings(
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-core" % circeVersion,
      "io.circe" %%% "circe-generic" % circeVersion,
      "io.circe" %%% "circe-parser" % circeVersion
    )
  )

val modeljsonJVM = modeljson.jvm
val modeljsonJS = modeljson.js
