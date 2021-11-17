import Dependencies._

ThisBuild / scalaVersion := "3.0.2"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "dev.myclinic.scala"
ThisBuild / organizationName := "myclinic"

val mysqlVersion = "6.0.6"
val http4sVersion = "0.23.3"
val doobieVersion = "1.0.0-M5"
val circeVersion = "0.14.1"
val scalaJavaTimeVersion = "2.3.0"
val scalaJSDomVersion = "1.2.0"
val catsVersion = "2.6.1"

ThisBuild / scalacOptions ++= Seq("-deprecation", "-encoding", "UTF-8")

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
    appbase,
    appointApp,
    appointAdmin,
    receptionApp,
  )
  .settings(
    publish := {},
    publishLocal := {},
  )

lazy val model = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("model"))
  .dependsOn(util, holidayjp, clinicop)
  .settings(
    name := "model",
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-core" % circeVersion,
      "io.circe" %%% "circe-generic" % circeVersion,
      "io.circe" %%% "circe-parser" % circeVersion
    )
  )
  .jsConfigure(_ enablePlugins TzdbPlugin)
  .jsSettings(
    scalaJSUseMainModuleInitializer := false,
    zonesFilter := { (z: String) => z == "Asia/Tokyo" },
    libraryDependencies ++= Seq(
      "io.github.cquiroz" %%% "scala-java-time" % scalaJavaTimeVersion
    )
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      scalaTest
    )
  )


val modelJS = model.js
val modelJVM = model.jvm

lazy val db = project
  .in(file("db"))
  .dependsOn(modelJVM)
  .settings(
    name := "db",
    libraryDependencies ++= Seq(
      "mysql" % "mysql-connector-java" % mysqlVersion,
      "org.tpolecat" %% "doobie-core" % doobieVersion,
      "org.tpolecat" %% "doobie-hikari" % doobieVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "io.circe" %%% "circe-core" % circeVersion,
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
  .dependsOn(db, modelJVM, utilJVM, appointAdmin, clinicopJVM, javalib)
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
  .dependsOn(modelJS, utilJS)
  .settings(
    name := "webclient",
    scalaJSUseMainModuleInitializer := false,
    libraryDependencies ++= Seq(
      ("org.scala-js" %%% "scalajs-dom" % "1.2.0")
        .cross(CrossVersion.for3Use2_13),
      "io.circe" %%% "circe-core" % circeVersion,
      "io.circe" %%% "circe-generic" % circeVersion,
      "io.circe" %%% "circe-parser" % circeVersion
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

lazy val appbase = project
  .in(file("app-base"))
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(domq, modelJS, utilJS, webclient, validatorJS)
  .settings(
    name := "myclinic-appbase",
    libraryDependencies ++= Seq(
      ("org.scala-js" %%% "scalajs-dom" % scalaJSDomVersion)
        .cross(CrossVersion.for3Use2_13)
    )
  )

lazy val appointApp = project
  .in(file("appoint-app"))
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(domq, modelJS, utilJS, webclient, validatorJS, appbase)
  .settings(
    name := "myclinic-appoint",
    Compile / fastLinkJS / scalaJSLinkerOutputDirectory :=
      (rootDir.value / "server" / "web" / "appoint" / "scalajs"),
    libraryDependencies ++= Seq(
      ("org.scala-js" %%% "scalajs-dom" % scalaJSDomVersion)
        .cross(CrossVersion.for3Use2_13)
    )
  )

lazy val appointAdmin = project.in(file("appoint-admin"))
  .dependsOn(modelJVM, db, utilJVM, clinicopJVM)
  .settings()

lazy val receptionApp = project
  .in(file("reception-app"))
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(domq, modelJS, utilJS, webclient, validatorJS, appbase)
  .settings(
    name := "myclinic-reception",
    Compile / fastLinkJS / scalaJSLinkerOutputDirectory :=
      (rootDir.value / "server" / "web" / "reception" / "scalajs"),
    libraryDependencies ++= Seq(
      ("org.scala-js" %%% "scalajs-dom" % scalaJSDomVersion)
        .cross(CrossVersion.for3Use2_13)
    )
  )

lazy val validator = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("validator"))
  .dependsOn(util, model)
  .jsConfigure(_ enablePlugins TzdbPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats-core" % catsVersion
    )
  )

val validatorJVM = validator.jvm
val validatorJS = validator.js

lazy val holidayjp = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("holidayjp"))
  .dependsOn(util)
  .jsConfigure(_ enablePlugins TzdbPlugin)
  .jsSettings(
    scalaJSUseMainModuleInitializer := false,
    zonesFilter := { (z: String) => z == "Asia/Tokyo" },
    libraryDependencies ++= Seq(
      "io.github.cquiroz" %%% "scala-java-time" % scalaJavaTimeVersion
    )
  )

val holidayjpJVM = holidayjp.jvm
val holidayjpJS = holidayjp.js

lazy val clinicop = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)  
  .in(file("clinicop"))
  .dependsOn(util, holidayjp)
  .jsConfigure(_ enablePlugins TzdbPlugin)
  .jsSettings(
    scalaJSUseMainModuleInitializer := false,
    zonesFilter := { (z: String) => z == "Asia/Tokyo" },
    libraryDependencies ++= Seq(
      "io.github.cquiroz" %%% "scala-java-time" % scalaJavaTimeVersion
    )
  )

val clinicopJVM = clinicop.jvm
val clinicopJS = clinicop.js

lazy val rcpt = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)  
  .in(file("rcpt"))
  //.dependsOn()
  .jsConfigure(_ enablePlugins TzdbPlugin)
  .jsSettings(
    scalaJSUseMainModuleInitializer := false,
    zonesFilter := { (z: String) => z == "Asia/Tokyo" },
    libraryDependencies ++= Seq(
      "io.github.cquiroz" %%% "scala-java-time" % scalaJavaTimeVersion
    )
  )

val rcptJVM = rcpt.jvm
val rcptJS = rcpt.js

lazy val javalib = project
  .in(file("javalib"))
  .settings(
    version := "1.0.0-SHANPSHOT",
    crossPaths := false,
    autoScalaLibrary := false
  )
