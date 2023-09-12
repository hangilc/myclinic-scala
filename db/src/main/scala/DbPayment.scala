package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.{DbPatientPrim => Prim}
import doobie.*
import doobie.implicits.*

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

trait DbPayment extends Mysql:
  def findLastPayment(visitId: Int): IO[Option[Payment]] =
    mysql(DbPaymentPrim.getLastPayment(visitId).option)

  def enterPayment(payment: Payment): IO[AppEvent] =
    mysql(DbPaymentPrim.enterPayment(payment))

  def listPayment(visitId: Int): IO[List[Payment]] =
    mysql(DbPaymentPrim.listPayment(visitId).to[List])