package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.{DbAppointPrim => Prim}
import doobie.*
import doobie.implicits.*
import dev.myclinic.scala.util.DateTimeOrdering.{*, given}
import scala.math.Ordered.orderingToOrdered

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

trait DbAppoint extends Mysql:

  def safeCreateAppointTime(appointTime: AppointTime): ConnectionIO[AppEvent] =
    assert(appointTime.appointTimeId == 0, "Non-zero appoint time to create.")
    assert(
      appointTime.fromTime <= appointTime.untilTime,
      s"Invalid appoint time values. s{appointTime.fromTime} - s{appointTime.untilTime}"
    )
    def confirmNoOverlap(appointTimes: List[AppointTime]): Unit =
      if AppointTime.overlaps(appointTimes.sortBy(_.fromTime)) then
        throw new RuntimeException(s"AppointTime overlaps. ${appointTimes}")
    for 
      ats <- Prim.listAppointTimesForDate(appointTime.date).to[List]
      _ = confirmNoOverlap(appointTime :: ats)
      created <- Prim.enterAppointTime(appointTime)
      event <- DbEventPrim.logAppointTimeCreated(created)
    yield event

  def batchEnterAppointTimes(
      appointTimes: List[AppointTime]
  ): IO[List[AppEvent]] =
    sqlite(appointTimes.map(safeCreateAppointTime(_)).sequence)

  def createAppointTime(appointTime: AppointTime): IO[AppEvent] =
    sqlite(safeCreateAppointTime(appointTime))

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

  private def batchDeleteAppointTimes(
      appointTimeIds: List[Int]
  ): ConnectionIO[List[AppEvent]] =
    appointTimeIds.map(id => safeDeleteAppointTime(id)).sequence

  def deleteAppointTime(appointTimeId: Int): IO[AppEvent] =
    sqlite(safeDeleteAppointTime(appointTimeId))

  private def safeUpdateAppointTime(
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
        s"Appoint time overlaps with other.\n${appointTime} <=> ${others}"
      )
      _ <- Prim.updateAppointTime(appointTime)
      event <- DbEventPrim.logAppointTimeUpdated(appointTime)
    yield event

  def updateAppointTime(appointTime: AppointTime): IO[AppEvent] =
    sqlite(safeUpdateAppointTime(appointTime))

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
      sqlite {
        for
          target <- Prim.getAppointTime(targetId).unique
          follows <- batchGetAppointTimes(followIds)
          _ = assert(
            AppointTime.isAdjacentRun(target :: follows),
            "Non-contiguous appoint times"
          )
          appointCounts <- followIds
            .map(Prim.countAppointsByAppointTime(_))
            .sequence
          _ = assert(
            appointCounts.forall(_ == 0),
            "Cannot combine appoint times (appoint exists)."
          )
          delEvents <- batchDeleteAppointTimes(followIds)
          updateEvent <- safeUpdateAppointTime(
            target.copy(
              untilTime = newUntilTime(follows),
              capacity = target.capacity + capacityInc(follows)
            )
          )
        yield delEvents ++ List(updateEvent)
      }

  def splitAppointTime(appointTimeId: Int, at: LocalTime): IO[List[AppEvent]] =
    def confirmAt(appointTime: AppointTime): Unit =
      if appointTime.fromTime <= at && at <= appointTime.untilTime then ()
      else throw new RuntimeException(s"Invalid split time. (${at})")
    def divideCapacity(
        capacity: Int,
        from: LocalTime,
        at: LocalTime,
        until: LocalTime
    ): (Int, Int) =
      val span = ChronoUnit.MINUTES.between(from, until).toInt
      val left = ChronoUnit.MINUTES.between(from, at).toInt
      val leftCap = capacity * left / span
      (leftCap, capacity - leftCap)

    val op = for
      appointTime <- Prim.getAppointTime(appointTimeId).unique
      _ = confirmAt(appointTime)
      (capA, capB) = divideCapacity(
        appointTime.capacity,
        appointTime.fromTime,
        at,
        appointTime.untilTime
      )
      update = appointTime.copy(untilTime = at, capacity = capA)
      follow = appointTime.copy(
        appointTimeId = 0,
        fromTime = at,
        capacity = capB
      )
      updateEvent <- safeUpdateAppointTime(update)
      createEvent <- safeCreateAppointTime(follow)
    yield List(updateEvent, createEvent)
    sqlite(op)

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

  def listAppointTimesForDate(date: LocalDate): IO[List[AppointTime]] =
    sqlite(Prim.listAppointTimesForDate(date).to[List])

  def getAppointTimeById(appointTimeId: Int): IO[AppointTime] =
    sqlite(Prim.getAppointTime(appointTimeId).unique)

  private def enterAppointWithEvent(
      a: Appoint
  ): ConnectionIO[(Appoint, AppEvent)] =
    for
      created <- Prim.enterAppoint(a)
      event <- DbEventPrim.logAppointCreated(created)
    yield (created, event)

  private def safeUpdateAppoint(appoint: Appoint): ConnectionIO[(Appoint, AppEvent)] =
    for
      updated <- Prim.updateAppoint(appoint)
      event <- DbEventPrim.logAppointUpdated(updated)
    yield (updated, event)

  def addAppoint(a: Appoint): IO[(Appoint, AppEvent)] =
    sqlite({
      for
        at <- Prim.getAppointTime(a.appointTimeId).unique
        existing <- Prim.listAppointsForAppointTime(at.appointTimeId).to[List]
        _ = if existing.size >= at.capacity then
          throw new RuntimeException("Overbooking")
        result <- enterAppointWithEvent(a)
      yield result
    })

  def cancelAppoint(appointId: Int): IO[AppEvent] =
    sqlite({
      for
        appoint <- Prim.getAppoint(appointId).unique
        _ <- Prim.deleteAppoint(appointId)
        event <- DbEventPrim.logAppointDeleted(appoint)
      yield event
    })

  def updateAppoint(appoint: Appoint): IO[AppEvent] =
    sqlite({
      for
        result <- safeUpdateAppoint(appoint)
        (updated, event) = result
      yield event
    })

  def getAppoint(appointId: Int): IO[Appoint] =
    sqlite(Prim.getAppoint(appointId).unique)

  def listAppointsForAppointTime(appointTimeId: Int): IO[List[Appoint]] =
    sqlite(Prim.listAppointsForAppointTime(appointTimeId).to[List])

  def listAppointsForDate(date: LocalDate): IO[List[Appoint]] =
    sqlite(Prim.listAppointsForDate(date).to[List])
