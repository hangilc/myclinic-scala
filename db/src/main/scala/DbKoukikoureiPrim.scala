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

object DbKoukikoureiPrim:
  def getKoukikourei(koukikoureiId: Int): Query0[Koukikourei] =
    sql"""
      select * from hoken_koukikourei where koukikourei_id = $koukikoureiId
    """.query[Koukikourei]

  def getKoukikoureiOpt(koukikoureiId: Int): ConnectionIO[Option[Koukikourei]] =
    if koukikoureiId == 0 then None.pure[ConnectionIO]
    else getKoukikourei(koukikoureiId).unique.map(Some.apply)

  def listAvailableKoukikourei(
      patientId: Int,
      at: LocalDate
  ): ConnectionIO[List[Koukikourei]] =
    sql"""
      select * from hoken_koukikourei where patient_id = ${patientId} 
        and valid_from <= ${at}
        and (valid_upto = '0000-00-00' || ${at} <= valid_upto)
        order by koukikourei_id desc
    """.query[Koukikourei].to[List]

  def listKoukikourei(patientId: Int): ConnectionIO[List[Koukikourei]] =
    sql"""
      select * from hoken_koukikourei where patient_id = ${patientId}
      order by koukikourei_id desc
    """.query[Koukikourei].to[List]

  def enterKoukikourei(d: Koukikourei): ConnectionIO[(Koukikourei, AppEvent)] =
    val op = sql"""
      insert into hoken_koukikourei 
        (patient_id, hokensha_bangou, hihokensha_bangou, futan_wari,
        valid_from, valid_upto)
        values (${d.patientId}, ${d.hokenshaBangou}, ${d.hihokenshaBangou}, ${d.futanWari}, 
        ${d.validFrom}, ${d.validUpto})
    """
    for
      id <- op.update.withUniqueGeneratedKeys[Int]("koukikourei_id")
      entered <- getKoukikourei(id).unique
      event <- DbEventPrim.logKoukikoureiCreated(entered)
    yield (entered, event)

  def updateKoukikourei(d: Koukikourei): ConnectionIO[AppEvent] =
    val op = sql"""
      update hoken_koukikourei set
        patient_id = ${d.patientId}, 
        hokensha_bangou = ${d.hokenshaBangou},
        hihokensha_bangou = ${d.hihokenshaBangou},
        futan_wari = ${d.futanWari},
        valid_from = ${d.validFrom},
        valid_upto = ${d.validUpto}
        where koukikourei_id = ${d.koukikoureiId}
    """
    for
      _ <- op.update.run
      updated <- getKoukikourei(d.koukikoureiId).unique
      event <- DbEventPrim.logKoukikoureiUpdated(updated)
    yield event

  def deleteKoukikourei(koukikoureiId: Int): ConnectionIO[AppEvent] =
    val op = sql"""
      delete from hoken_koukikourei where koukikourei_id = ${koukikoureiId}
    """
    for
      target <- getKoukikourei(koukikoureiId).unique
      affected <- op.update.run
      _ = if affected != 1 then
        throw new RuntimeException(
          s"Failed to delete koukikourei: ${koukikoureiId}"
        )
      event <- DbEventPrim.logKoukikoureiDeleted(target)
    yield event

  def countKoukikoureiUsage(koukikoureiId: Int): ConnectionIO[Int] =
    sql"""
      select count(*) from visit where koukikourei_id = ${koukikoureiId}
    """.query[Int].unique

  def enterOrUpdateKoukikourei(koukikourei: Koukikourei): ConnectionIO[(Koukikourei, AppEvent)] =
    if koukikourei.koukikoureiId == 0 then 
      enterKoukikourei(koukikourei)
    else 
      updateKoukikourei(koukikourei).map((koukikourei, _))
