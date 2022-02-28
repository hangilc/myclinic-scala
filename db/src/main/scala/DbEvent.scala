package dev.myclinic.scala.db

import cats.effect.IO
import cats.effect.implicits._
import cats.implicits._
import doobie._
import doobie.implicits._
import dev.myclinic.scala.model._
import dev.myclinic.scala.db.DoobieMapping._

trait DbEvent extends Mysql {

  def nextGlobalEventId(): IO[Int] =
    mysql(DbEventPrim.nextGlobalEventId())

  def currentEventId(): IO[Int] =
    mysql(DbEventPrim.currentEventId())

  def listGlobalEventSince(eventId: Int): IO[List[AppEvent]] =
    mysql(DbEventPrim.listGlobalEventSince(eventId).to[List])

  def listGlobalEventInRange(
      fromEventId: Int,
      untilEventId: Int
  ): IO[List[AppEvent]] =
    mysql(
      DbEventPrim.listGlobalEventInRange(fromEventId, untilEventId).to[List]
    )

  def listAppointEvents(limit: Int, offset: Int): IO[List[AppEvent]] =
    val op = sql"""
      select * from app_event where model = 'appoint' order by app_event_id desc
        limit ${limit} offset ${offset}
    """.query[AppEvent].to[List]
    mysql(op)

}
