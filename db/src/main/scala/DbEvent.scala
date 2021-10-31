package dev.myclinic.scala.db

import cats.effect.IO
import cats.effect.implicits._
import doobie._
import doobie.implicits._
import dev.myclinic.scala.model._

trait DbEvent extends Mysql {

  def nextGlobalEventId(): IO[Int] =
    sqlite(DbEventPrim.nextGlobalEventId())

  def listGlobalEventSince(eventId: Int): IO[List[AppEvent]] =
    sqlite(DbEventPrim.listGlobalEventSince(eventId).to[List])

  def listGlobalEventInRange(
      fromEventId: Int,
      untilEventId: Int
  ): IO[List[AppEvent]] =
    sqlite(
      DbEventPrim.listGlobalEventInRange(fromEventId, untilEventId).to[List]
    )

}
