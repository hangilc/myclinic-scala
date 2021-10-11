package dev.myclinic.scala.model

import java.time.LocalDate
import java.time.LocalTime
import java.time.LocalDateTime
import dev.myclinic.scala.model.DateTimeOrder.{given}
import localTimeOrder.mkOrderingOps

case class AppointTime(
    appointTimeId: Int,
    eventId: Int,
    date: LocalDate,
    fromTime: LocalTime,
    untilTime: LocalTime,
    kind: String,
    capacity: Int
) extends Evented:
  def isAdjacentTo(other: AppointTime): Boolean =
    date == other.date && untilTime == other.fromTime

  def overlapsWith(other: AppointTime): Boolean =
    date == other.date && 
      untilTime > other.fromTime &&
      fromTime < other.untilTime

object AppointTime:
  def overlaps(ats: List[AppointTime]): Boolean =
    val byDate: Map[LocalDate, List[AppointTime]] = ats.groupBy(at => at.date)
    byDate.values
      .map(ls => ls.sortBy(at => at.fromTime))
      .map(ls => ls.map(at => (at.fromTime, at.untilTime)))
      .map(timeIntervalOverlaps(_))
      .reduce(_ || _)

  def timeIntervalOverlaps(
      sortedIntervals: List[(LocalTime, LocalTime)]
  ): Boolean =
    sortedIntervals match {
      case Nil => false
      case l @ _ :: t =>
        (l.zip(t)
          .map { case ((_, a), (b, _)) =>
            a.isAfter(b)
          })
          .exists(identity)
    }

  def isAdjacentRun(as: List[AppointTime]): Boolean =
    if as.size < 2 then true
    else
      as.sliding(2).forall(e => e(0).isAdjacentTo(e(1)))

case class Appoint(
    appointId: Int,
    eventId: Int,
    appointTimeId: Int,
    patientName: String,
    patientId: Int,
    memo: String
) extends Evented

case class AppEvent(
    id: Int,
    eventId: Int,
    createdAt: LocalDateTime,
    model: String,
    kind: String,
    data: String
)
