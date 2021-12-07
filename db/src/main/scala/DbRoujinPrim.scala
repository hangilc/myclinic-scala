package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.DoobieMapping.*
import doobie.*
import doobie.implicits.*
import dev.myclinic.scala.util.DateTimeOrdering.{*, given}
import scala.math.Ordered.orderingToOrdered

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object DbRoujinPrim:
  def getRoujin(roujinId: Int): Query0[Roujin] =
    sql"""
      select * from hoken_roujin where roujin_id = $roujinId
    """.query[Roujin]


  def listAvailableRoujin(patientId: Int, at: LocalDate): ConnectionIO[List[Roujin]] =
    sql"""
      select * from hoken_roujin where patient_id = ${patientId} 
        and valid_from <= ${at}
        and (valid_upto = '0000-00-00' || ${at} <= valid_upto)
        order by roujin_id desc
    """.query[Roujin].to[List]
 
  def listRoujin(patientId: Int): ConnectionIO[List[Roujin]] =
    sql"""
      select * from hoken_roujin where patient_id = ${patientId}
      order by roujin_id desc
    """.query[Roujin].to[List]   

  def enterRoujin(d: Roujin): ConnectionIO[AppEvent] =
    val op = sql"""
      insert into hoken_roujin 
        (patient_id, shichouson, jukyuusha, futan_wari,
        valid_from, valid_upto)
        values (${d.patientId}, ${d.shichouson}, ${d.jukyuusha},
        ${d.validFrom}, ${d.validUpto})
    """
    for
      id <- op.update.withUniqueGeneratedKeys[Int]("roujin_id")
      entered <- getRoujin(id).unique
      event <- DbEventPrim.logRoujinCreated(entered)
    yield event  
    
  def updateRoujin(d: Roujin): ConnectionIO[AppEvent] =
    val op = sql"""
      update hoken_roujin set
        patient_id = ${d.patientId},
        shichouson = ${d.shichouson},
        jukyuusha = ${d.jukyuusha},
        futan_wari = ${d.futanWari},
        valid_from = ${d.validFrom},
        valid_upto = ${d.validUpto}
        where roujin_id = ${d.roujinId}
    """
    for
      _ <- op.update.run
      updated <- getRoujin(d.roujinId).unique
      event <- DbEventPrim.logRoujinUpdated(updated)
    yield event

  def deleteRoujin(roujinId: Int): ConnectionIO[AppEvent] =
    val op = sql"""
      delete from hoken_roujin where roujin_id = ${roujinId}
    """
    for
      target <- getRoujin(roujinId).unique
      affected <- op.update.run
      _ = if affected != 1 then throw new RuntimeException(s"Failed to delete roujin: ${roujinId}")
      event <- DbEventPrim.logRoujinDeleted(target)
    yield event