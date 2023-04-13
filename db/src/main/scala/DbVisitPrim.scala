package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import doobie.*
import doobie.implicits.*
import dev.myclinic.scala.db.DoobieMapping.{given}

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

object DbVisitPrim:
  def getVisit(visitId: Int): Query0[Visit] =
    sql"""
      select * from visit where visit_id = ${visitId}
    """.query[Visit]

  def deleteVisit(visitId: Int): ConnectionIO[Unit] =
    sql"""
      delete from visit where visit_id = ${visitId}
    """.update.run.map(affected => {
      if affected != 1 then
        throw new RuntimeException("Failed to delete visit.")
    })

  def enterVisit(visit: Visit): ConnectionIO[(AppEvent, Visit)] =
    val op = sql"""
      insert into visit (patient_id, v_datetime, shahokokuho_id, roujin_id,
        kouhi_1_id, kouhi_2_id, kouhi_3_id, koukikourei_id, attributes)
        values (${visit.patientId}, ${visit.visitedAt}, ${visit.shahokokuhoId}, ${visit.roujinId}, 
        ${visit.kouhi1Id}, ${visit.kouhi2Id}, ${visit.kouhi3Id}, ${visit.koukikoureiId},
        ${visit.attributesStore})
    """
    for
      visitId <- op.update.withUniqueGeneratedKeys[Int]("visit_id")
      entered <- getVisit(visitId).unique
      event <- DbEventPrim.logVisitCreated(entered)
    yield (event, entered)

  def updateVisit(visit: Visit): ConnectionIO[(AppEvent, Visit)] =
    val op = sql"""
      update visit set patient_id = ${visit.patientId}, v_datetime = ${visit.visitedAt}, 
        shahokokuho_id = ${visit.shahokokuhoId}, roujin_id = ${visit.roujinId},
        kouhi_1_id = ${visit.kouhi1Id}, kouhi_2_id = ${visit.kouhi2Id}, kouhi_3_id = ${visit.kouhi3Id}, 
        koukikourei_id = ${visit.koukikoureiId}, attributes = ${visit.attributesStore}
        where visit_id = ${visit.visitId}
    """
    for
      affected <- op.update.run
      _ = if affected != 1 then throw new RuntimeException("update visit failed")
      updated <- getVisit(visit.visitId).unique
      event <- DbEventPrim.logVisitUpdated(updated)
    yield (event, updated)

  def updateHokenIds(visitId: Int, hokenIdSet: HokenIdSet): ConnectionIO[AppEvent] =
    for
      visit <- getVisit(visitId).unique
      newVisit = visit.copy(
        shahokokuhoId = hokenIdSet.shahokokuhoId,
        koukikoureiId = hokenIdSet.koukikoureiId,
        roujinId = hokenIdSet.roujinId,
        kouhi1Id = hokenIdSet.kouhi1Id,
        kouhi2Id = hokenIdSet.kouhi2Id,
        kouhi3Id = hokenIdSet.kouhi3Id
      )
      result <- updateVisit(newVisit)
      (event, updated) = result
    yield event

  def countByShahokokuho(shahokokuhoId: Int): ConnectionIO[Int] =
    sql"""
      select count(*) from visit where shahokokuho_id = ${shahokokuhoId}
    """.query[Int].unique

  def countByRoujin(roujinId: Int): ConnectionIO[Int] =
    sql"""
      select count(*) from visit where roujin_id = ${roujinId}
    """.query[Int].unique

  def countByKoukikourei(koukikoureiId: Int): ConnectionIO[Int] =
    sql"""
      select count(*) from visit where koukikourei_id = ${koukikoureiId}
    """.query[Int].unique

  def countByKouhi(kouhiId: Int): ConnectionIO[Int] =
    sql"""
      select count(*) from visit where 
        kouhi_1_id = ${kouhiId} or
        kouhi_2_id = ${kouhiId} or
        kouhi_3_id = ${kouhiId}
    """.query[Int].unique

  def countByPatient(patientId: Int): ConnectionIO[Int] =
    sql"""
      select count(*) from visit where patient_id = ${patientId}
    """.query[Int].unique

  def listRecentVisit(offset: Int, count: Int): ConnectionIO[List[Visit]] =
    sql"""
    select * from visit order by visit_id desc limit ${offset}, ${count}
    """.query[Visit].to[List]

  def listVisitByDate(at: LocalDate): ConnectionIO[List[Visit]] =
    sql"""
      select * from visit where date(v_datetime) = ${at} order by visit_id
    """.query[Visit].to[List]

  def listByPatient(
      patientId: Int,
      offset: Int,
      count: Int
  ): ConnectionIO[List[Visit]] =
    sql"""
      select * from visit where patient_id = ${patientId} limit ${offset}, ${count}
    """.query[Visit].to[List]

  def listByPatientReverse(
      patientId: Int,
      offset: Int,
      count: Int
  ): ConnectionIO[List[Visit]] =
    sql"""
      select * from visit where patient_id = ${patientId} order by visit_id desc limit ${offset}, ${count}
    """.query[Visit].to[List]

  def listVisitIdByPatient(
      patientId: Int,
      offset: Int,
      count: Int
  ): ConnectionIO[List[Int]] =
    sql"""
      select visit_id from visit where patient_id = ${patientId} limit ${offset}, ${count}
    """.query[Int].to[List]

  def listVisitIdByPatientReverse(
      patientId: Int,
      offset: Int,
      count: Int
  ): ConnectionIO[List[Int]] =
    sql"""
      select visit_id from visit where patient_id = ${patientId} order by visit_id desc limit ${offset}, ${count}
    """.query[Int].to[List]

  def batchGetVisit(visitIds: List[Int]): ConnectionIO[Map[Int, Visit]] =
    for
      visits <- visitIds
        .map(visitId => getVisit(visitId).unique)
        .sequence
      items = visits.map(visit => (visit.visitId, visit))
    yield Map(items: _*)

  def getLastVisitId(): ConnectionIO[Int] =
    sql"""
      select max(visit_id) from visit
    """.query[Int].unique

  def listVisitSince(patientId: Int, date: LocalDate): Query0[Visit] =
    sql"""
      select * from visit where patient_id = ${patientId} and date(v_datetime) >= ${date} order by visit_id desc
    """.query[Visit]

  def listVisitIdByShahokokuhoReverse(shahokokuhoId: Int): ConnectionIO[List[Int]] =
    sql"""
      select visit_id from visit where shahokokuho_id = ${shahokokuhoId} order by visit_id desc
    """.query[Int].to[List]

  def listVisitIdByKoukikoureiReverse(koukikoureiId: Int): ConnectionIO[List[Int]] =
    sql"""
      select visit_id from visit where koukikourei_id = ${koukikoureiId} order by visit_id desc
    """.query[Int].to[List]

  def listVisitIdByKouhiReverse(kouhiId: Int): ConnectionIO[List[Int]] =
    sql"""
      select visit_id from visit where kouhi_1_id = ${kouhiId} 
      or kouhi_2_id = ${kouhiId} or kouhi_3_id = ${kouhiId}
      order by visit_id desc
    """.query[Int].to[List]
