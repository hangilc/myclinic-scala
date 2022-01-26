package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.DoobieMapping.*
import doobie.*
import doobie.implicits.*
import dev.myclinic.scala.util.DateTimeOrdering.{*, given}
import java.time.LocalDate

trait DbHotline extends Mysql:
  def postHotline(hotline: Hotline): IO[AppEvent] =
    mysql(DbEventPrim.logHotlineCreated(hotline))

  def listTodaysHotline(): IO[List[(Int, HotlineCreated)]] =
    mysql(
      DbEventPrim
        .listHotlineByDate(LocalDate.now())
        .map(evt => (evt.appEventId, AppModelEvent.from(evt).asInstanceOf[HotlineCreated]))
        .to[List]
    )