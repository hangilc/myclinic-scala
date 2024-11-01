val ESVersion = org.scalajs.linker.interface.ESVersion

ThisBuild / scalaVersion := "3.1.3"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "dev.myclinic.scala"
ThisBuild / organizationName := "myclinic"
ThisBuild / resolvers += Resolver.mavenLocal

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

ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-feature",
)
ThisBuild / javacOptions ++= Seq("-encoding", "UTF-8")

val rootDir = ThisBuild / baseDirectory

lazy val root = project
  .in(file("."))
  .aggregate(
    appUtil,
    // appbase,
    appointAdmin,
    // appointApp,
    // client,
    clinicop,
    config,
    db,
    // domq,
    drawerscala,
    drawerform,
    formatshohousen,
    holidayjp,
    javalib,
    kanjidate,
    model,
    // myclinicutilJS,
    // myclinicutilJVM,
    // practiceApp,
    rcpt,
    // receptionApp,
    // repl,
    server,
    // utilJS,
    // utilJVM,
    util
    // validatorJS,
    // validatorJVM,
    // webclient,
    // masterDb
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


// lazy val myclinicutil = crossProject(JSPlatform, JVMPlatform)
//   .crossType(CrossType.Full)
//   .in(file("myclinic-util"))
//   .dependsOn(model)
//   .settings(
//     name := "myclinicutil"
//   )

// val myclinicutilJS = myclinicutil.js
// val myclinicutilJVM = myclinicutil.jvm

lazy val model = project
  .in(file("model"))
  .dependsOn(util, holidayjp, clinicop, drawerscala)
  .settings(
    name := "model",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
    )
  )
  // .jvmSettings(
  //   libraryDependencies ++= Seq(
  //     "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
  //   )
  // )

// val modelJS = model.js
// val modelJVM = model.jvm

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

// lazy val masterDb = project
//   .in(file("master-db"))
//   .dependsOn(db)
//   .enablePlugins(PackPlugin)
//   .settings(
//     name := "master-db",
//     libraryDependencies ++= Seq(
//       "org.apache.commons" % "commons-csv" % "1.9.0",
//       "co.fs2" %% "fs2-core" % fs2Version,
//       "co.fs2" %% "fs2-io" % fs2Version
//     )
//   )

// lazy val client = project
//   .in(file("client"))
//   .dependsOn(modelJVM, utilJVM)
//   .settings(
//     name := "client",
//     libraryDependencies ++= Seq(
//       "org.http4s" %% "http4s-dsl" % http4sVersion,
//       "org.http4s" %% "http4s-blaze-client" % http4sVersion,
//       "org.http4s" %% "http4s-circe" % http4sVersion,
//       "io.circe" %%% "circe-core" % circeVersion
//     )
//   )

lazy val util = project
  .in(file("util"))
  .settings(
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
    )
  )

// val utilJS = util.js
// val utilJVM = util.jvm.dependsOn(timejs)

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
      "ch.qos.logback" % "logback-classic" % "1.1.3",
      //"ch.qos.logback" % "logback-classic" % "1.1.3" % "runtime",
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-server" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "dev.fujiwara" % "drawer" % "1.0.1-SNAPSHOT",
      "com.twilio.sdk" % "twilio" % "8.34.1"
    )
  )

// lazy val webclient = project
//   .in(file("webclient"))
//   .enablePlugins(ScalaJSPlugin)
//   .dependsOn(modelJS, utilJS, drawerJS, drawerformJS)
//   .settings(
//     name := "webclient",
//     scalaJSUseMainModuleInitializer := false,
//     libraryDependencies ++= Seq(
//       "org.scala-js" %%% "scalajs-dom" % scalaJSDomVersion,
//       "io.circe" %%% "circe-core" % circeVersion,
//       "io.circe" %%% "circe-generic" % circeVersion,
//       "io.circe" %%% "circe-parser" % circeVersion,
//       "org.scala-js" %%% "scala-js-macrotask-executor" % macrotaskExecutorVersion
//     )
//   )

// lazy val domq = project
//   .in(file("domq"))
//   .enablePlugins(ScalaJSPlugin)
//   .dependsOn(validatorJS)
//   .settings(
//     name := "domq",
//     libraryDependencies ++= Seq(
//       "org.scala-js" %%% "scalajs-dom" % scalaJSDomVersion,
//       "org.scala-js" %%% "scala-js-macrotask-executor" % macrotaskExecutorVersion,
//       "org.typelevel" %%% "cats-core" % catsVersion
//     )
//   )

// lazy val appbase = project
//   .in(file("app-base"))
//   .enablePlugins(ScalaJSPlugin)
//   .dependsOn(
//     domq,
//     modelJS,
//     utilJS,
//     webclient,
//     validatorJS,
//     drawerJS,
//     kanjidateJS,
//     validatorJS,
//     appUtilJS
//   )
//   .settings(
//     name := "myclinic-appbase",
//     libraryDependencies ++= Seq(
//       "org.scala-js" %%% "scalajs-dom" % scalaJSDomVersion,
//       "org.typelevel" %%% "cats-core" % catsVersion,
//       "org.scala-js" %%% "scala-js-macrotask-executor" % macrotaskExecutorVersion
//     )
//   )

// lazy val appointApp = project
//   .in(file("appoint-app"))
//   .enablePlugins(ScalaJSPlugin)
//   .dependsOn(
//     domq,
//     modelJS,
//     utilJS,
//     webclient,
//     validatorJS,
//     appbase,
//     kanjidateJS
//   )
//   .settings(
//     name := "myclinic-appoint",
//     Compile / fastLinkJS / scalaJSLinkerOutputDirectory :=
//       (rootDir.value / "server" / "web" / "appoint" / "scalajs"),
//     Compile / fullLinkJS / scalaJSLinkerOutputDirectory :=
//       (rootDir.value / "server" / "web" / "appoint" / "scalajs"),
//     libraryDependencies ++= Seq(
//       "org.scala-js" %%% "scalajs-dom" % scalaJSDomVersion,
//       "org.scala-js" %%% "scala-js-macrotask-executor" % macrotaskExecutorVersion
//     )
//   )

lazy val appointAdmin = project
  .in(file("appoint-admin"))
  .dependsOn(model, db, util, clinicop)

// lazy val receptionApp = project
//   .in(file("reception-app"))
//   .enablePlugins(ScalaJSPlugin)
//   .dependsOn(
//     domq,
//     modelJS,
//     utilJS,
//     webclient,
//     validatorJS,
//     appbase,
//     appUtilJS,
//     drawerJS,
//     drawerformJS,
//     kanjidateJS
//   )
//   .settings(
//     name := "myclinic-reception",
//     Compile / fastLinkJS / scalaJSLinkerOutputDirectory :=
//       (rootDir.value / "server" / "web" / "reception" / "scalajs"),
//     Compile / fullLinkJS / scalaJSLinkerOutputDirectory :=
//       (rootDir.value / "server" / "web" / "reception" / "scalajs"),
//     libraryDependencies ++= Seq(
//       "org.scala-js" %%% "scalajs-dom" % scalaJSDomVersion,
//       "org.scala-js" %%% "scala-js-macrotask-executor" % macrotaskExecutorVersion
//     )
//   )

// lazy val practiceApp = project
//   .in(file("practice-app"))
//   .enablePlugins(ScalaJSPlugin)
//   .dependsOn(
//     domq,
//     modelJS,
//     utilJS,
//     webclient,
//     validatorJS,
//     appbase,
//     appUtilJS,
//     drawerJS,
//     kanjidateJS,
//     formatshohousenJS,
//     myclinicutilJS
//   )
//   .settings(
//     name := "myclinic-practice",
//     Compile / fastLinkJS / scalaJSLinkerOutputDirectory :=
//       (rootDir.value / "server" / "web" / "practice" / "scalajs"),
//     Compile / fullLinkJS / scalaJSLinkerOutputDirectory :=
//       (rootDir.value / "server" / "web" / "practice" / "scalajs"),
//     libraryDependencies ++= Seq(
//       "org.scala-js" %%% "scalajs-dom" % scalaJSDomVersion,
//       "org.scala-js" %%% "scala-js-macrotask-executor" % macrotaskExecutorVersion,
//       "org.scalatest" %%% "scalatest-funsuite" % "3.2.13" % "test",
//     )
//   )

// lazy val validator = crossProject(JSPlatform, JVMPlatform)
//   .crossType(CrossType.Pure)
//   .in(file("validator"))
//   .dependsOn(util, model)
//   .settings(
//     libraryDependencies ++= Seq(
//       "org.typelevel" %%% "cats-core" % catsVersion
//     )
//   )

// val validatorJVM = validator.jvm
// val validatorJS = validator.js

lazy val holidayjp = project
  .in(file("holidayjp"))
  .dependsOn(util, kanjidate)

// val holidayjpJVM = holidayjp.jvm
// val holidayjpJS = holidayjp.js

lazy val clinicop = project
  .in(file("clinicop"))
  .dependsOn(util, holidayjp)

// val clinicopJVM = clinicop.jvm
// val clinicopJS = clinicop.js

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
      "dev.fujiwara" % "drawer" % "1.0.0-SNAPSHOT",
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

// val drawerformJVM = drawerform.jvm
// val drawerformJS = drawerform.js

lazy val kanjidate = project
  .in(file("kanjidate"))

// val kanjidateJVM = kanjidate.jvm
// val kanjidateJS = kanjidate.js.dependsOn(timejs)

// lazy val repl = project
//   .in(file("repl"))
//   .dependsOn(modelJVM)
//   .settings(
//     name := "repl",
//     libraryDependencies ++= Seq(
//       "org.http4s" %% "http4s-dsl" % http4sVersion,
//       "org.http4s" %% "http4s-blaze-client" % http4sVersion,
//       "org.http4s" %% "http4s-circe" % http4sVersion,
//       "io.circe" %%% "circe-core" % circeVersion
//     )
//   )

// lazy val chrome = project
//   .in(file("chrome"))
//   .dependsOn(modelJVM, client)
//   .settings(
//     name := "chrome",
//     libraryDependencies ++= Seq(
//       "org.seleniumhq.selenium" % "selenium-chrome-driver" % "4.3.0",
//       "org.seleniumhq.selenium" % "selenium-support" % "4.3.0",
//       "org.http4s" %% "http4s-dsl" % http4sVersion,
//       "org.http4s" %% "http4s-blaze-client" % http4sVersion,
//       "org.http4s" %% "http4s-circe" % http4sVersion,
//       "io.circe" %%% "circe-core" % circeVersion,
//       "org.scalatest" %% "scalatest" % "3.2.13" % "test",
//       "org.scalatest" %% "scalatest-funsuite" % "3.2.13" % "test"
//     )
//   )

// lazy val timejs = project
//   .in(file("timejs"))
//   .enablePlugins(ScalaJSPlugin, TzdbPlugin)
//   .settings(
//     dbVersion := TzdbPlugin.Version("2022a"),
//     zonesFilter := ((z: String) => z == "Asia/Tokyo"),
//     libraryDependencies ++= Seq(
//       "org.scalatest" %%% "scalatest" % "3.2.13" % "test",
//       "org.scalatest" %%% "scalatest-funsuite" % "3.2.13" % "test",
//       "io.github.cquiroz" %%% "scala-java-time" % scalaJavaTimeVersion
//     )
//   )
