package dev.myclinic.scala.db

import dev.myclinic.scala.model.{Onshi, AppEvent}
import cats._
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.util.log.LogHandler.jdkLogHandler
import dev.myclinic.scala.db.DoobieMapping._

object DbOnshiPrim:
  def getOnshi(visitId: Int): Query0[Onshi] =
    sql"""
      select * from onshi where visit_id = ${visitId}
    """.query[Onshi]

  def enterOnshi(onshi: Onshi): ConnectionIO[AppEvent] =
    val op = sql"""
      insert into onshi (visit_id, kakunin) values (${onshi.visitId}, ${onshi.kakunin})
    """
    for 
      _ <- op.update.run
      entered <- getOnshi(onshi.visitId).unique
      event <- DbEventPrim.logOnshiCreated(entered)
    yield event

  def updateOnshi(onshi: Onshi): ConnectionIO[AppEvent] =
    val op = sql"""
      update onshi set 
        kakunin = ${onshi.kakunin}
      where visit_id = ${onshi.visitId}
    """
    for 
      affected <- op.update.run
      _ = if affected != 1 then throw new RuntimeException("Updated onshi failed.")
      updated <- getOnshi(onshi.visitId).unique
      event <- DbEventPrim.logOnshiUpdated(updated)
    yield event

  def deleteOnshi(visitId: Int): ConnectionIO[AppEvent] =
    val op = sql"""
      delete from onshi where visit_id = ${visitId}
    """
    for 
      onshi <- getOnshi(visitId).unique
      affected <- op.update.run
      _ = if affected != 1 then throw new RuntimeException("Delete onshi failed.")
      event <- DbEventPrim.logOnshiDeleted(onshi)
    yield event
  