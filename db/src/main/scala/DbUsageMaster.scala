package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.{DbUsageMasterPrim => Prim}
import doobie.*
import doobie.implicits.*

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

trait DbUsageMaster extends Mysql:
  def selectUsageMasterByUsageName(name: String): IO[List[UsageMaster]] =
    mysql(Prim.selectUsageMasterByUsageName(name))

  def listUsageMasterKubunName(): IO[List[String]] =
    mysql(Prim.listUsageMasterKubunName())

  def listUsageMasterDetailKubunName(): IO[List[String]] =
    mysql(Prim.listUsageMasterDetailKubunName())

  def listUsageMasterTimingName(): IO[List[String]] =
    mysql(Prim.listUsageMasterTimingName())

