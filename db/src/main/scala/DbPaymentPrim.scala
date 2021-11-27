package dev.myclinic.scala.db

import dev.myclinic.scala.model.*
import cats._
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.util.log.LogHandler.jdkLogHandler
import dev.myclinic.scala.db.DoobieMapping._


object DbPaymentPrim:
  def enterPayment(payment: Payment): ConnectionIO[AppEvent] =
    val op = sql"""
      insert into visit_payment (visit_id, amount, paytime)
        values (${payment.visitId}, ${payment.amount}, ${payment.paytime})
    """
    for
      _ <- op.update.run
      event <- DbEventPrim.logPaymentCreated(payment)
    yield event