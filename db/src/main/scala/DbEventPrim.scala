package dev.myclinic.scala.db

import java.time._
import cats.implicits._
import doobie._
import doobie.implicits._
import dev.myclinic.scala.model._
import dev.myclinic.scala.db.DoobieMapping._

object DbEventPrim:
  def getAppEvent(id: Int): Query0[AppEvent] =
    sql"select * from app_event where id = ${id}".query[AppEvent]

  def enterAppEvent(
      eventId: Int,
      model: String,
      kind: String,
      data: String
  ): ConnectionIO[AppEvent] =
    val createdAt = LocalDateTime.now()
    for
      id <- sql"""
          insert into app_event (created_at, event_id, model, kind, data) values (
            ${createdAt}, ${eventId}, ${model}, ${kind}, ${data}
          )
        """.update.withUniqueGeneratedKeys[Int]("id")
      entered <- getAppEvent(id).unique
    yield entered

  private def setGlobalEventId(eventId: Int): ConnectionIO[Unit] =
      (sql"""
          update event_id_store set event_id = ${eventId} where id = 0
        """.update.run) >>= Helper.confirmUpdate("Failed to set global event ID.")

  private def getGlobalEventId(): ConnectionIO[Int] =
    sql"""
      select event_id from event_id_store where id = 0
    """.query[Int].unique

  def listGlobalEventSince(eventId: Int): Query0[AppEvent] =
    val sql = sql""" 
      select * from app_event where event_id >= ${eventId}
    """"
    sql.query[AppEvent]

  def listGlobalEventInRange(fromEventId: Int, untilEventId: Int): Query0[AppEvent] =
    val sql = sql""" 
      select * from app_event where event_id >= ${fromEventId} and event_id < ${untilEventId}
    """"
    sql.query[AppEvent]

  def nextGlobalEventId(): ConnectionIO[Int] =
    for
      currId <- getGlobalEventId()
    yield currId + 1

  def withEventId[A](f: Int => ConnectionIO[A]): ConnectionIO[A] =
    for
      eventId <- nextGlobalEventId()
      result <- f(eventId)
      _ <- setGlobalEventId(eventId)
    yield result

