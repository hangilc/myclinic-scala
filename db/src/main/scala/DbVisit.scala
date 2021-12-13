package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import doobie.*
import doobie.implicits.*
import dev.myclinic.scala.db.DoobieMapping.{given}
import dev.myclinic.scala.db.{DbVisitPrim => Prim}

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

trait DbVisit extends Mysql:
  def getVisit(visitId: Int): IO[Visit] =
    mysql(Prim.getVisit(visitId).unique)

  def batchGetVisit(visitIds: List[Int]): IO[Map[Int, Visit]] =
    val op =
      for
        visits <- visitIds
          .map(visitId => Prim.getVisit(visitId).unique)
          .sequence
        items = visits.map(visit => (visit.visitId, visit))
      yield Map(items: _*)
    mysql(op)

  def listRecentVisit(offset: Int, count: Int): IO[List[Visit]] =
    mysql(Prim.listRecentVisit(offset, count))

  def listVisitByDate(at: LocalDate): IO[List[Visit]] =
    mysql(Prim.listVisitByDate(at))

  def countVisitByPatient(patientId: Int): IO[Int] =
    mysql(Prim.countByPatient(patientId))

  def listVisitByPatient(patientId: Int, offset: Int, count: Int): IO[List[Visit]] =
    mysql(Prim.listByPatient(patientId, offset, count))

  def listVisitIdByPatient(patientId: Int, offset: Int, count: Int): IO[List[Int]] =
    mysql(Prim.listVisitIdByPatient(patientId, offset, count))