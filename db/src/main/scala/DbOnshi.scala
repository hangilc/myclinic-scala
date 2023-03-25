package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import doobie.*
import doobie.implicits.*

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

trait DbOnshi extends Mysql:
  def getOnshi(visitId: Int): IO[Onshi] = 
    mysql(DbOnshiPrim.getOnshi(visitId).unique)

  def findOnshi(visitId: Int): IO[Option[Onshi]] =
    mysql(DbOnshiPrim.getOnshi(visitId).option);

  def enterOnshi(onshi: Onshi): IO[AppEvent] =
    mysql(DbOnshiPrim.enterOnshi(onshi))

  def updateOnshi(onshi: Onshi): IO[AppEvent] =
    mysql(DbOnshiPrim.updateOnshi(onshi))

  def deleteOnshi(visitId: Int): IO[AppEvent] =
    mysql(DbOnshiPrim.deleteOnshi((visitId)))

  def batchProbeOnshi(visitIds: List[Int]): IO[List[Int]] =
    mysql(DbOnshiPrim.batchProbeOnshi(visitIds))

