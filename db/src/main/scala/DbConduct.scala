package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.{DbConductPrim => Prim}
import doobie.*
import doobie.implicits.*

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

trait DbConduct extends Mysql:

  def listConductForVisit(visitId: Int): IO[List[Conduct]] =
    mysql(Prim.listConductForVisit(visitId))

  def getConduct(conductId: Int): IO[Conduct] =
    mysql(Prim.getConduct(conductId).unique)