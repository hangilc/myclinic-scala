package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.{DbChargePrim => Prim}
import doobie.*
import doobie.implicits.*

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

trait DbCharge extends Mysql:
  def countChargeForVisit(visitId: Int): IO[Int] =
    mysql(sql"""
      select count(*) from visit_charge where visit_id = ${visitId}
    """.query[Int].unique)

  def findCharge(visitId: Int): IO[Option[Charge]] =
    mysql(Prim.getCharge(visitId).option)

  def getCharge(visitId: Int): IO[Charge] =
    mysql(Prim.getCharge(visitId).unique)

  def updateChargeValue(visitId: Int, chargeValue: Int): IO[(AppEvent, Charge)] =
    mysql(Prim.updateChargeValue(visitId, chargeValue))

  def setChargeValue(visitId: Int, chargeValue: Int): IO[(AppEvent, Charge)] =
    mysql(Prim.setChargeValue(visitId, chargeValue))

  def enterChargeValue(visitId: Int, chargeValue: Int): IO[(AppEvent, Charge)] =
    mysql(Prim.enterChargeValue(visitId, chargeValue))


  