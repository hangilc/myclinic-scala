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
     
  def listRecentVisit(offset: Int, count: Int): ConnectionIO[List[Visit]] =
    sql"""
    select * from visit order by visit_id desc limit ${offset}, ${count}
    """.query[Visit].to[List]

  def listVisitByDate(at: LocalDate): ConnectionIO[List[Visit]] =
    sql"""
      select * from visit where date(v_datetime) = ${at} order by visit_id
    """.query[Visit].to[List]
