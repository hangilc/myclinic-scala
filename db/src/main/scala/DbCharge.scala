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