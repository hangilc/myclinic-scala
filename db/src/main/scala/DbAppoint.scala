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
import java.security.InvalidParameterException

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
      eventId: Int,
      appointTimeId: Int
  ): ConnectionIO[AppEvent] =
    for
      at <- Prim.getAppointTime(appointTimeId).unique
      appoints <- Prim.listAppointsForAppointTime(appointTimeId).to[List]
      _ = if appoints.size > 0 then
        throw new RuntimeException("Appoint exists.")
      _ <- Prim.deleteAppointTime(appointTimeId)
      log <- DbEventPrim.logAppointTimeDeleted(eventId, at)
    yield log

  def batchDeleteAppointTimes(
      appointTimes: List[AppointTime]
  ): IO[List[AppEvent]] =
    withEventId(eventId =>
      appointTimes
        .map(a => safeDeleteAppointTime(eventId, a.appointTimeId))
        .sequence
    )

  def deleteAppointTime(appointTimeId: Int): IO[AppEvent] =
    withEventId(eventId => safeDeleteAppointTime(eventId, appointTimeId))

  private def saveUpdateAppointTime(
      eventId: Int,
      appointTime: AppointTime
  ): ConnectionIO[AppEvent] =
    val appointTimeId: Int = appointTime.appointTimeId
    for
      appoints <- Prim.listAppointsForAppointTime(appointTimeId).to[List]
      _ = assert(
        appointTime.capacity >= appoints.size,
        "Too small capacity to update."
      )
      ats <- Prim.listAppointTimesForDate(appointTime.date).to[List]
      others = ats.filter(at => at.appointTimeId != appointTimeId)
      _ = assert(
        others.forall(!appointTime.overlapsWith(_)),
        "Appoint time overlaps with other."
      )
      updated <- Prim.updateAppointTime(appointTime.copy(eventId = eventId))
      event <- DbEventPrim.logAppointTimeUpdated(updated)
    yield event

  private def batchGetAppointTimes(
      appointTimeIds: List[Int]
  ): ConnectionIO[List[AppointTime]] =
    appointTimeIds.map(id => Prim.getAppointTime(id).unique).sequence

  def combineAppointTimes(
      appointTimeIds: List[Int]
  ): IO[List[AppEvent]] =
    if appointTimeIds.size <= 1 then IO.pure(List.empty)
    else
      val targetId = appointTimeIds.head
      val followIds = appointTimeIds.tail
      def newUntilTime(follows: List[AppointTime]): LocalTime =
        follows.last.untilTime
      def capacityInc(follows: List[AppointTime]): Int =
        follows.foldLeft(0)((acc, ele) => acc + ele.capacity)
      withEventId(eventId => {
        for
          target <- Prim.getAppointTime(targetId).unique
          follows <- batchGetAppointTimes(followIds)
          _ = assert(
            AppointTime.isAdjacentRun(target :: follows),
            "Non-contiguous appoint times"
          )
          delEvents <- followIds
            .map(id => safeDeleteAppointTime(eventId, id))
            .sequence
          updated <- Prim.updateAppointTime(
            target.copy(
              untilTime = newUntilTime(follows),
              capacity = target.capacity + capacityInc(follows),
              eventId = eventId
            )
          )
          updateEvent <- DbEventPrim.logAppointTimeUpdated(updated)
        yield delEvents ++ List(updateEvent)
      })

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

  def cancelAppoint(appointId: Int): IO[AppEvent] =
    withEventId(eventId => {
      for
        appoint <- Prim.getAppoint(appointId).unique
        _ <- Prim.deleteAppoint(appointId)
        event <- DbEventPrim.logAppointDeleted(eventId, appoint)
      yield event
    })

  def listAppointsForAppointTime(appointTimeId: Int): IO[List[Appoint]] =
    sqlite(Prim.listAppointsForAppointTime(appointTimeId).to[List])

  def listAppointsForDate(date: LocalDate): IO[List[Appoint]] =
    sqlite(Prim.listAppointsForDate(date).to[List])
