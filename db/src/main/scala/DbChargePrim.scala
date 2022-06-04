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
import doobie.free.resultset

object DbChargePrim:
  def getCharge(visitId: Int): Query0[Charge] =
    sql"""
      select * from visit_charge where visit_id = ${visitId}
    """.query[Charge]

  def enterChargeValue(visitId: Int, chargeValue: Int): ConnectionIO[(AppEvent, Charge)] =
    val op = sql"""
      insert into visit_charge (visit_id, charge) values (${visitId}, ${chargeValue})
    """
    for
      affected <- op.update.run
      _ = if affected != 1 then 
        throw new RuntimeException(s"Failed to enter charge: ${visitId}, ${chargeValue}")
      entered <- getCharge(visitId).unique
      event <- DbEventPrim.logChargeCreated(entered)
    yield (event, entered)

  def updateChargeValue(visitId: Int, chargeValue: Int): ConnectionIO[(AppEvent, Charge)] =
    val op = sql"""
      update visit_charge set charge = ${chargeValue} where visit_id = ${visitId}
    """
    for
      affected <- op.update.run
      _ = if affected != 1 then throw new RuntimeException(s"Failed to update charge: ${visitId}")
      updated <- getCharge(visitId).unique
      event <- DbEventPrim.logChargeUpdated(updated)
    yield (event, updated)

  def setChargeValue(visitId: Int, chargeValue: Int): ConnectionIO[(AppEvent, Charge)] =
    for
      chargeOption <- getCharge(visitId).option
      result <- chargeOption match {
        case None => enterChargeValue(visitId, chargeValue)
        case Some(_) => updateChargeValue(visitId, chargeValue)
      }
    yield result
