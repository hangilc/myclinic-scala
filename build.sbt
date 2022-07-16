val ESVersion = org.scalajs.linker.interface.ESVersion

ThisBuild / scalaVersion := "3.1.3"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "dev.myclinic.scala"
ThisBuild / organizationName := "myclinic"
ThisBuild / resolvers += Resolver.mavenLocal
// ThisBuild / scalaJSLinkerConfig ~= (_.withESFeatures(_.withESVersion(ESVersion.ES2018)))

val mysqlVersion = "6.0.6"
val http4sVersion = "0.23.12"
val doobieVersion = "1.0.0-RC2"
val circeVersion = "0.14.2"
val circeYamlVersion = "0.14.1"
val scalaJavaTimeVersion = "2.3.0"
val scalaJSDomVersion = "2.2.0"
val catsVersion = "2.8.0"
val macrotaskExecutorVersion = "1.0.0"
val jacksonVersion = "2.13.3"
val slf4jVersion = "1.7.25"
val fs2Version = "3.2.8"
val scalaLoggingVersion = "3.9.4"
val scalaTestVersion = "3.2.10"

ThisBuild / scalacOptions ++= Seq("-deprecation", "-encoding", "UTF-8", "-feature")
ThisBuild / javacOptions ++= Seq("-encoding", "UTF-8")

val rootDir = ThisBuild / baseDirectory

lazy val root = project
  .in(file("."))
  .aggregate(
    appUtilJS,
    appUtilJVM,
    appbase,
    appointAdmin,
    appointApp,
    client,
    clinicopJS,
    clinicopJVM,
    config,
    db,
    domq,
    drawerJS,
    drawerJVM,
    drawerformJS,
    drawerformJVM,
    formatshohousenJS,
    formatshohousenJVM,
    holidayjpJS,
    holidayjpJVM,
    javalib,
    kanjidateJS,
    kanjidateJVM,
    modelJS,
    modelJVM,
    myclinicutilJS,
    myclinicutilJVM,
    practiceApp,
    rcpt,
    receptionApp,
    server,
    utilJS,
    utilJVM,
    validatorJS,
    validatorJVM,
    webclient,
    masterDb
  )
  .settings(
    publish := {},
    publishLocal := {}
  )

lazy val formatshohousen = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .in(file("formatshohousen"))
  .dependsOn(util)
  .settings(
    name := "formatshohousen"
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
    ),
    Test / logBuffered := false
  )

val formatshohousenJS = formatshohousen.js
val formatshohousenJVM = formatshohousen.jvm

lazy val myclinicutil = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .in(file("myclinic-util"))
  .dependsOn(model)
  .settings(
    name := "myclinicutil"
  )

val myclinicutilJS = myclinicutil.js
val myclinicutilJVM = myclinicutil.jvm

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
  .jsSettings(
    scalaJSUseMainModuleInitializer := false,
    libraryDependencies ++= Seq(
    )
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
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
      "io.circe" %%% "circe-core" % circeVersion
    )
  )

lazy val masterDb = project
  .in(file("master-db"))
  .dependsOn(db)
  .enablePlugins(PackPlugin)
  .settings(
    name := "master-db",
    libraryDependencies ++= Seq(
      "org.apache.commons" % "commons-csv" % "1.9.0",
      "co.fs2" %% "fs2-core" % fs2Version,
      "co.fs2" %% "fs2-io" % fs2Version
    )
  )

lazy val client = project
  .in(file("client"))
  .dependsOn(modelJVM, utilJVM)
  .settings(
    name := "client",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "io.circe" %%% "circe-core" % circeVersion
    )
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
  .enablePlugins(PackPlugin)
  .dependsOn(
    db,
    modelJVM,
    utilJVM,
    appointAdmin,
    clinicopJVM,
    rcpt,
    javalib,
    config,
    drawerJVM,
    drawerformJVM,
    formatshohousenJVM
  )
  .settings(
    name := "server",
    resolvers += Resolver.mavenLocal,
    libraryDependencies ++= Seq(
      "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
      "ch.qos.logback" % "logback-classic" % "1.1.3",
      //"ch.qos.logback" % "logback-classic" % "1.1.3" % "runtime",
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "dev.fujiwara" % "drawer" % "1.0.0-SNAPSHOT"
    )
  )

lazy val webclient = project
  .in(file("webclient"))
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(modelJS, utilJS, drawerJS, drawerformJS)
  .settings(
    name := "webclient",
    scalaJSUseMainModuleInitializer := false,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % scalaJSDomVersion,
      "io.circe" %%% "circe-core" % circeVersion,
      "io.circe" %%% "circe-generic" % circeVersion,
      "io.circe" %%% "circe-parser" % circeVersion,
      "org.scala-js" %%% "scala-js-macrotask-executor" % macrotaskExecutorVersion
    )
  )

lazy val domq = project
  .in(file("domq"))
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(validatorJS)
  .settings(
    name := "domq",
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % scalaJSDomVersion,
      "org.scala-js" %%% "scala-js-macrotask-executor" % macrotaskExecutorVersion,
      "org.typelevel" %%% "cats-core" % catsVersion
    )
  )

lazy val appbase = project
  .in(file("app-base"))
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(
    domq,
    modelJS,
    utilJS,
    webclient,
    validatorJS,
    drawerJS,
    kanjidateJS,
    validatorJS,
    appUtilJS
  )
  .settings(
    name := "myclinic-appbase",
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % scalaJSDomVersion,
      "org.typelevel" %%% "cats-core" % catsVersion,
      "org.scala-js" %%% "scala-js-macrotask-executor" % macrotaskExecutorVersion
    )
  )

lazy val appointApp = project
  .in(file("appoint-app"))
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(
    domq,
    modelJS,
    utilJS,
    webclient,
    validatorJS,
    appbase,
    kanjidateJS
  )
  .settings(
    name := "myclinic-appoint",
    Compile / fastLinkJS / scalaJSLinkerOutputDirectory :=
      (rootDir.value / "server" / "web" / "appoint" / "scalajs"),
    Compile / fullLinkJS / scalaJSLinkerOutputDirectory :=
      (rootDir.value / "server" / "web" / "appoint" / "scalajs"),
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % scalaJSDomVersion,
      "org.scala-js" %%% "scala-js-macrotask-executor" % macrotaskExecutorVersion
    )
  )

lazy val appointAdmin = project
  .in(file("appoint-admin"))
  .dependsOn(modelJVM, db, utilJVM, clinicopJVM)
  .settings()

lazy val receptionApp = project
  .in(file("reception-app"))
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(
    domq,
    modelJS,
    utilJS,
    webclient,
    validatorJS,
    appbase,
    appUtilJS,
    drawerJS,
    drawerformJS,
    kanjidateJS
  )
  .settings(
    name := "myclinic-reception",
    Compile / fastLinkJS / scalaJSLinkerOutputDirectory :=
      (rootDir.value / "server" / "web" / "reception" / "scalajs"),
    Compile / fullLinkJS / scalaJSLinkerOutputDirectory :=
      (rootDir.value / "server" / "web" / "reception" / "scalajs"),
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % scalaJSDomVersion,
      "org.scala-js" %%% "scala-js-macrotask-executor" % macrotaskExecutorVersion
    )
  )

lazy val practiceApp = project
  .in(file("practice-app"))
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(
    domq,
    modelJS,
    utilJS,
    webclient,
    validatorJS,
    appbase,
    appUtilJS,
    drawerJS,
    kanjidateJS,
    formatshohousenJS,
    myclinicutilJS
  )
  .settings(
    name := "myclinic-practice",
    Compile / fastLinkJS / scalaJSLinkerOutputDirectory :=
      (rootDir.value / "server" / "web" / "practice" / "scalajs"),
    Compile / fullLinkJS / scalaJSLinkerOutputDirectory :=
      (rootDir.value / "server" / "web" / "practice" / "scalajs"),
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % scalaJSDomVersion,
      "org.scala-js" %%% "scala-js-macrotask-executor" % macrotaskExecutorVersion
    )
  )

lazy val validator = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("validator"))
  .dependsOn(util, model)
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
  .dependsOn(util, kanjidate)
  .jsSettings(
    scalaJSUseMainModuleInitializer := false,
    libraryDependencies ++= Seq(
    )
  )

val holidayjpJVM = holidayjp.jvm
val holidayjpJS = holidayjp.js

lazy val clinicop = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("clinicop"))
  .dependsOn(util, holidayjp)
  .jsSettings(
    scalaJSUseMainModuleInitializer := false
  )

val clinicopJVM = clinicop.jvm
val clinicopJS = clinicop.js

lazy val rcpt = project
  .in(file("rcpt"))
  .dependsOn(javalib, modelJVM, appUtilJVM)

lazy val javalib = project
  .in(file("javalib"))
  .settings(
    name := "javalib",
    version := "1.0.0-SHANPSHOT",
    crossPaths := false,
    autoScalaLibrary := false,
    libraryDependencies ++= Seq(
      "org.slf4j" % "slf4j-api" % slf4jVersion,
      "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
      "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonVersion,
      "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % jacksonVersion,
      "com.fasterxml.jackson.dataformat" % "jackson-dataformat-xml" % jacksonVersion,
    )
  )

lazy val config = project
  .in(file("config"))
  .dependsOn(javalib, modelJVM, clinicopJVM)
  .settings(
    name := "config",
    libraryDependencies ++= Seq(
      "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
      "io.circe" %% "circe-yaml" % circeYamlVersion
    )
  )

lazy val appUtil = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("app-util"))
  .dependsOn(util, model, kanjidate)
  .jsSettings(
    scalaJSUseMainModuleInitializer := false,
    libraryDependencies ++= Seq(
    )
  )

val appUtilJS = appUtil.js
val appUtilJVM = appUtil.jvm

lazy val drawer = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Full)
  .in(file("drawer"))
  .settings(
    name := "drawer",
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
      "dev.fujiwara" % "drawer" % "1.0.0-SNAPSHOT"
    )
  )

val drawerJVM = drawer.jvm
val drawerJS = drawer.js

lazy val drawerform = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Full)
  .in(file("drawerform"))
  .dependsOn(model, kanjidate)
  .settings(
    name := "drawerform",
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
      "dev.fujiwara" % "drawer" % "1.0.0-SNAPSHOT"
    )
  )

val drawerformJVM = drawerform.jvm
val drawerformJS = drawerform.js

lazy val kanjidate = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("kanjidate"))
  .jsConfigure(_ enablePlugins TzdbPlugin)
  .jsSettings(
    scalaJSUseMainModuleInitializer := false,
    zonesFilter := { (z: String) => z == "Asia/Tokyo" },
    libraryDependencies ++= Seq(
      "io.github.cquiroz" %%% "scala-java-time" % scalaJavaTimeVersion
    )
  )

val kanjidateJVM = kanjidate.jvm
val kanjidateJS = kanjidate.js
