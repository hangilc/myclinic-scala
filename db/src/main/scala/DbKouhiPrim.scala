package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.DoobieMapping.*
import doobie.*
import doobie.implicits.*
import scala.math.Ordered.orderingToOrdered

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object DbKouhiPrim:
  def getKouhi(kouhiId: Int): Query0[Kouhi] =
    sql"""
      select * from kouhi where kouhi_id = $kouhiId
    """.query[Kouhi]

  def listAvailableKouhi(
      patientId: Int,
      at: LocalDate
  ): ConnectionIO[List[Kouhi]] =
    sql"""
      select * from kouhi where patient_id = ${patientId} 
        and valid_from <= ${at}
        and (valid_upto = '0000-00-00' || ${at} <= valid_upto)
        order by kouhi_id
    """.query[Kouhi].to[List]

  def listKouhi(patientId: Int): ConnectionIO[List[Kouhi]] =
    sql"""
      select * from kouhi where patient_id = ${patientId}
      order by kouhi_id desc
    """.query[Kouhi].to[List]

  def enterKouhi(d: Kouhi): ConnectionIO[(Kouhi, AppEvent)] =
    val op = sql"""
      insert into kouhi 
        (patient_id, futansha, jukyuusha, 
        valid_from, valid_upto)
        values (${d.patientId}, ${d.futansha}, ${d.jukyuusha},
        ${d.validFrom}, ${d.validUpto})
    """
    for
      id <- op.update.withUniqueGeneratedKeys[Int]("kouhi_id")
      entered <- getKouhi(id).unique
      event <- DbEventPrim.logKouhiCreated(entered)
    yield (entered, event)

  def updateKouhi(d: Kouhi): ConnectionIO[AppEvent] =
    val op = sql"""
      update kouhi set
        patient_id = ${d.patientId},
        futansha = ${d.futansha},
        jukyuusha = ${d.jukyuusha},
        valid_from = ${d.validFrom},
        valid_upto = ${d.validUpto}
        where kouhi_id = ${d.kouhiId}
    """
    for
      affected <- op.update.run
      _ = if affected != 1 then throw new RuntimeException("Update kouhi failed.")
      updated <- getKouhi(d.kouhiId).unique
      event <- DbEventPrim.logKouhiUpdated(updated)
    yield event

  def deleteKouhi(kouhiId: Int): ConnectionIO[AppEvent] =
    val op = sql"""
      delete from kouhi where kouhi_id = ${kouhiId}
    """
    for
      target <- getKouhi(kouhiId).unique
      affected <- op.update.run
      _ = if affected != 1 then
        throw new RuntimeException(s"Failed to delete kouhi: ${kouhiId}")
      event <- DbEventPrim.logKouhiDeleted(target)
    yield event

  def countKouhiUsage(kouhiId: Int): ConnectionIO[Int] =
    sql"""
      select count(*) from visit where kouhi_id = ${kouhiId}
    """.query[Int].unique
