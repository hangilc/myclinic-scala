package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.{DbAppointPrim => Prim}
import doobie.*
import doobie.implicits.*

import java.time.LocalDate
import java.time.LocalTime

trait DbAppoint extends Sqlite:
  private def withEventId[A](f: Int => ConnectionIO[A]): IO[A] =
    sqlite(DbEventPrim.withEventId(eventId => f(eventId)))

  private def enterWithEvent(
      a: AppointTime
  ): ConnectionIO[(AppointTime, AppEvent)] =
    for
      entered <- Prim.enterAppointTime(a)
      event <- DbEventPrim.logAppointTimeCreated(entered)
    yield (entered, event)

  def batchEnterAppointTimes(
      appointTimes: List[AppointTime]
  ): IO[List[AppEvent]] =
    withEventId(eventId => {
      val ats = appointTimes.map(_.copy(eventId = eventId))
      ats.map(at => enterWithEvent(at)).sequence.map(_.map(ae => ae._2))
    })

  def createAppointTime(appointTime: AppointTime): IO[(AppointTime, AppEvent)] =
    withEventId(eventId => {
      val a = appointTime.copy(eventId = eventId)
      enterWithEvent(a)
    })

  private def safeDeleteAppointTime(
      appointTimeId: Int
  ): ConnectionIO[AppEvent] =
    for
      at <- Prim.getAppointTime(appointTimeId).unique
      appoints <- Prim.listAppointsForAppointTime(appointTimeId).to[List]
      _ = if appoints.size > 0 then
        throw new RuntimeException("Appoint exists.")
      _ <- Prim.deleteAppointTime(appointTimeId)
      log <- DbEventPrim.logAppointTimeDeleted(at)
    yield log

  def batchDeleteAppointTimes(
      appointTimes: List[AppointTime]
  ): IO[List[AppEvent]] =
    val op =
      appointTimes.map(a => safeDeleteAppointTime(a.appointTimeId)).sequence
    sqlite(op)

  def deleteAppointTime(appointTimeId: Int): IO[AppEvent] =
    sqlite(safeDeleteAppointTime(appointTimeId))

  def listExistingAppointTimeDates(
      from: LocalDate,
      upto: LocalDate
  ): IO[List[LocalDate]] =
    sqlite(Prim.listExistingAppointTimeDates(from, upto).to[List])

  def listAppointTimes(
      from: LocalDate,
      upto: LocalDate
  ): IO[List[AppointTime]] =
    sqlite(Prim.listAppointTimes(from, upto).to[List])

  def getAppointTimeById(appointTimeId: Int): IO[AppointTime] =
    sqlite(Prim.getAppointTime(appointTimeId).unique)

  private def enterAppointWithEvent(
      a: Appoint
  ): ConnectionIO[(Appoint, AppEvent)] =
    for
      entered <- Prim.enterAppoint(a)
      event <- DbEventPrim.logAppointCreated(entered)
    yield (entered, event)

  def addAppoint(a: Appoint): IO[(Appoint, AppEvent)] =
    withEventId(eventId => {
      for
        at <- Prim.getAppointTime(a.appointTimeId).unique
        existing <- Prim.listAppointsForAppointTime(at.appointTimeId).to[List]
        _ = if existing.size >= at.capacity then
          throw new RuntimeException("Overbooking")
        result <- enterAppointWithEvent(a.copy(eventId = eventId))
      yield result
    })

  def listAppointsForAppointTime(appointTimeId: Int): IO[List[Appoint]] =
    sqlite(Prim.listAppointsForAppointTime(appointTimeId).to[List])

  def listAppointsForDate(date: LocalDate): IO[List[Appoint]] =
    sqlite(Prim.listAppointsForDate(date).to[List])
