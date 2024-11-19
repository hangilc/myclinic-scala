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

  def findVisit(visitId: Int): IO[Option[Visit]] =
    mysql(Prim.getVisit(visitId).option)

  def batchGetVisit(visitIds: List[Int]): IO[Map[Int, Visit]] =
    mysql(DbVisitPrim.batchGetVisit(visitIds))

  def listRecentVisit(offset: Int, count: Int): IO[List[Visit]] =
    mysql(Prim.listRecentVisit(offset, count))

  def listVisitByDate(at: LocalDate): IO[List[Visit]] =
    mysql(Prim.listVisitByDate(at))

  def countVisitByPatient(patientId: Int): IO[Int] =
    mysql(Prim.countByPatient(patientId))

  def listVisitByPatient(patientId: Int, offset: Int, count: Int): IO[List[Visit]] =
    mysql(Prim.listByPatient(patientId, offset, count))

  def listVisitByPatientReverse(patientId: Int, offset: Int, count: Int): IO[List[Visit]] =
    mysql(Prim.listByPatientReverse(patientId, offset, count))

  def listVisitIdByPatient(patientId: Int, offset: Int, count: Int): IO[List[Int]] =
    mysql(Prim.listVisitIdByPatient(patientId, offset, count))

  def listVisitIdByPatientReverse(patientId: Int, offset: Int, count: Int): IO[List[Int]] =
    mysql(Prim.listVisitIdByPatientReverse(patientId, offset, count))

  def updateHokenIds(visitId: Int, hokenIdSet: HokenIdSet): IO[AppEvent] =
    mysql(Prim.updateHokenIds(visitId, hokenIdSet))

  def updateVisit(visit: Visit): IO[AppEvent] =
    mysql(Prim.updateVisit(visit).map(_._1))

  def getLastVisitId(): IO[Int] =
    mysql(Prim.getLastVisitId())

  def listVisitSince(patientId: Int, date: LocalDate): IO[List[Visit]] =
    mysql(Prim.listVisitSince(patientId, date).to[List])

  def shahokokuhoUsageSince(shahokokuhoId: Int, date: LocalDate): IO[List[Visit]] =
    mysql(Prim.shahokokuhoUsageSince(shahokokuhoId, date))

  def shahokokuhoUsage(shahokokuhoId: Int): IO[List[Visit]] =
    mysql(Prim.shahokokuhoUsage(shahokokuhoId))

  def koukikoureiUsageSince(koukikoureiId: Int, date: LocalDate): IO[List[Visit]] =
    mysql(Prim.koukikoureiUsageSince(koukikoureiId, date))

  def koukikoureiUsage(koukikoureiId: Int): IO[List[Visit]] =
    mysql(Prim.koukikoureiUsage(koukikoureiId))

  def kouhiUsageSince(kouhiId: Int, date: LocalDate): IO[List[Visit]] =
    mysql(Prim.kouhiUsageSince(kouhiId, date))
  
  def kouhiUsage(kouhiId: Int): IO[List[Visit]] =
    mysql(Prim.kouhiUsage(kouhiId))

  def listVisitIdByPatientAndMonth(patientId: Int, year: Int, month: Int): IO[List[Int]] =
    mysql(Prim.listVisitIdByPatientAndMonth(patientId, year, month))

  def listVisitByMonth(year: Int, month: Int): IO[List[Visit]] =
    mysql(Prim.listVisitByMonth(year, month))

  def listVisitIdInDateInterval(fromDate: LocalDate, uptoDate: LocalDate): IO[List[Int]] =
    mysql(Prim.listVisitIdInDateInterval(fromDate, uptoDate))

  def listVisitIdByDateIntervalAndPatient(fromDate: LocalDate, uptoDate: LocalDate, patientId: Int): IO[List[Int]] =
    mysql(Prim.listVisitIdByDateIntervalAndPatient(fromDate, uptoDate, patientId))
