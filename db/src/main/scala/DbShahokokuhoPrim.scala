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

object DbShahokokuhoPrim:
  def getShahokokuho(shahokokuhoId: Int): Query0[Shahokokuho] =
    sql"""
      select * from hoken_shahokokuho where shahokokuho_id = $shahokokuhoId
    """.query[Shahokokuho]

  def getShahokokuhoOpt(shahokokuhoId: Int): ConnectionIO[Option[Shahokokuho]] =
    if shahokokuhoId == 0 then None.pure[ConnectionIO]
    else getShahokokuho(shahokokuhoId).unique.map(Some.apply)

  def listAvailableShahokokuho(
      patientId: Int,
      at: LocalDate
  ): ConnectionIO[List[Shahokokuho]] =
    sql"""
      select * from hoken_shahokokuho where patient_id = ${patientId} 
        and valid_from <= ${at}
        and (valid_upto = '0000-00-00' || ${at} <= valid_upto)
        order by shahokokuho_id desc
    """.query[Shahokokuho].to[List]

  def listShahokokuho(patientId: Int): ConnectionIO[List[Shahokokuho]] =
    sql"""
      select * from hoken_shahokokuho where patient_id = ${patientId}
      order by shahokokuho_id desc
    """.query[Shahokokuho].to[List]

  def enterShahokokuho(d: Shahokokuho): ConnectionIO[(Shahokokuho, AppEvent)] =
    val op = sql"""
      insert into hoken_shahokokuho 
        (patient_id, hokensha_bangou, hihokensha_kigou, hihokensha_bangou,
        honnin, valid_from, valid_upto, kourei, edaban)
        values (${d.patientId}, ${d.hokenshaBangou}, ${d.hihokenshaKigou},
        ${d.hihokenshaBangou}, ${d.honninStore}, ${d.validFrom}, ${d.validUpto},
        ${d.koureiStore}, ${d.edaban})
    """
    for
      id <- op.update.withUniqueGeneratedKeys[Int]("shahokokuho_id")
      entered <- getShahokokuho(id).unique
      event <- DbEventPrim.logShahokokuhoCreated(entered)
    yield (entered, event)

  def updateShahokokuho(d: Shahokokuho): ConnectionIO[AppEvent] =
    println("update shahokokuho")
    println(d)
    val op = sql"""
      update hoken_shahokokuho set
        patient_id = ${d.patientId}, hokensha_bangou = ${d.hokenshaBangou},
        hihokensha_kigou = ${d.hihokenshaKigou}, 
        hihokensha_bangou = ${d.hihokenshaBangou},
        honnin = ${d.honninStore},
        valid_from = ${d.validFrom},
        valid_upto = ${d.validUpto},
        kourei = ${d.koureiStore},
        edaban = ${d.edaban}
        where shahokokuho_id = ${d.shahokokuhoId}
    """
    for
      affected <- op.update.run
      _ = if affected != 1 then
        throw new RuntimeException("Update shahokokuho failed.")
      updated <- getShahokokuho(d.shahokokuhoId).unique
      event <- DbEventPrim.logShahokokuhoUpdated(updated)
    yield event

  def deleteShahokokuho(shahokokuhoId: Int): ConnectionIO[AppEvent] =
    val op = sql"""
      delete from hoken_shahokokuho where shahokokuho_id = ${shahokokuhoId}
    """
    for
      target <- getShahokokuho(shahokokuhoId).unique
      affected <- op.update.run
      _ = if affected != 1 then
        throw new RuntimeException(
          s"Failed to delete shahokokuho: ${shahokokuhoId}"
        )
      event <- DbEventPrim.logShahokokuhoDeleted(target)
    yield event

  def countShahokokuhoUsage(shahokokuhoId: Int): ConnectionIO[Int] =
    sql"""
      select count(*) from visit where shahokokuho_id = ${shahokokuhoId}
    """.query[Int].unique

  def enterOrUpdateShahokokuho(shahokokuho: Shahokokuho): ConnectionIO[(Shahokokuho, AppEvent)] =
    if shahokokuho.shahokokuhoId == 0 then 
      enterShahokokuho(shahokokuho)
    else 
      updateShahokokuho(shahokokuho).map((shahokokuho, _))

  def countShahokokuhoUsageBefore(shahokokuhoId: Int, date: LocalDate): ConnectionIO[Int] =
    sql"""
      select count(*) from visit where shahokokuho_id = ${shahokokuhoId}
      and DATE(v_datetime) < ${date}
    """.query[Int].unique

  def countShahokokuhoUsageAfter(shahokokuhoId: Int, date: LocalDate): ConnectionIO[Int] =
    sql"""
      select count(*) from visit where shahokokuho_id = ${shahokokuhoId}
      and DATE(v_datetime) > ${date}
    """.query[Int].unique

