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

object DbChargePrim:
  def getCharge(visitId: Int): Query0[Charge] =
    sql"""
      select * from visit_charge where visit_id = ${visitId}
    """.query[Charge]

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