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

  def listVisitIdByShahokokuhoReverse(shahokokuhoId: Int): IO[List[Int]] =
    mysql(Prim.listVisitIdByShahokokuhoReverse(shahokokuhoId))

  def listVisitIdByKoukikoureiReverse(koukikoureiId: Int): IO[List[Int]] =
    mysql(Prim.listVisitIdByKoukikoureiReverse(koukikoureiId))

  def listVisitIdByKouhiReverse(kouhiId: Int): IO[List[Int]] =
    mysql(Prim.listVisitIdByKouhiReverse(kouhiId))
    