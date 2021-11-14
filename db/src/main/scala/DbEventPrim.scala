package dev.myclinic.scala.db

import java.time._
import cats.implicits._
import doobie._
import doobie.implicits._
import dev.myclinic.scala.model.*
import dev.myclinic.scala.model.jsoncodec.Implicits.{given}
import io.circe._
import io.circe.syntax._
import dev.myclinic.scala.db.DoobieMapping._

object DbEventPrim:

  val CREATED: String = "created"
  val UPDATED: String = "updated"
  val DELETED: String = "deleted"

  def getAppEvent(id: Int): Query0[AppEvent] =
    sql"select * from app_event where app_event_id = ${id}".query[AppEvent]

  def enterAppEvent(
      model: String,
      kind: String,
      data: String
  ): ConnectionIO[AppEvent] =
    val createdAt = LocalDateTime.now()
    for
      id <- sql"""
          insert into app_event (created_at, model, kind, data) values (
            ${createdAt}, ${model}, ${kind}, ${data}
          )
        """.update.withUniqueGeneratedKeys[Int]("app_event_id")
      entered <- getAppEvent(id).unique
    yield entered

  def listGlobalEventSince(appEventId: Int): Query0[AppEvent] =
    val sql = sql""" 
      select * from app_event where app_event_id >= ${appEventId}
    """
    sql.query[AppEvent]

  def listGlobalEventInRange(
      fromEventId: Int,
      untilEventId: Int
  ): Query0[AppEvent] =
    val sql = sql""" 
      select * from app_event where app_event_id >= ${fromEventId} 
        and app_event_id < ${untilEventId}
    """
    sql.query[AppEvent]

  def nextGlobalEventId(): ConnectionIO[Int] =
    sql"""
      select app_event_id from app_event order by app_event_id desc limit 1
    """.query[Int].option.map(intOpt => intOpt match {
      case Some(id) => id + 1
      case None => 1
    })

  def listHotlineByDate(date: LocalDate): Query0[AppEvent] =
    sql"""
      select * from app_event where model = 'hotline' and kind = 'created'
        and date(created_at) = ${date}
        order by app_event_id
    """.query[AppEvent]

  def logAppointTimeCreated(a: AppointTime): ConnectionIO[AppEvent] =
    enterAppEvent("appoint-time", CREATED, a.asJson.toString)

  def logAppointTimeUpdated(a: AppointTime): ConnectionIO[AppEvent] =
    enterAppEvent("appoint-time", UPDATED, a.asJson.toString)

  def logAppointTimeDeleted(a: AppointTime): ConnectionIO[AppEvent] =
    enterAppEvent("appoint-time", DELETED, a.asJson.toString)

  def logAppointCreated(a: Appoint): ConnectionIO[AppEvent] =
    enterAppEvent("appoint", CREATED, a.asJson.toString)

  def logAppointUpdated(a: Appoint): ConnectionIO[AppEvent] =
    enterAppEvent("appoint", UPDATED, a.asJson.toString)

  def logAppointDeleted(a: Appoint): ConnectionIO[AppEvent] =
    enterAppEvent("appoint", DELETED, a.asJson.toString)

  def logHotlineCreated(hotline: Hotline): ConnectionIO[AppEvent] =
    enterAppEvent("hotline", CREATED, hotline.asJson.toString)
