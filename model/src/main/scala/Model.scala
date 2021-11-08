package dev.myclinic.scala.model

import java.time.LocalDate
import java.time.LocalTime
import java.time.LocalDateTime
import dev.myclinic.scala.util.DateTimeOrdering.{*, given}
import scala.math.Ordered.orderingToOrdered

given Ordering[AppointTime] with
  def compare(a: AppointTime, b: AppointTime): Int =
    val cmp: Int = summon[Ordering[LocalDate]].compare(a.date, b.date)
    if cmp == 0 then summon[Ordering[LocalTime]].compare(a.fromTime, b.fromTime)
    else cmp

case class AppointTime(
    appointTimeId: Int,
    date: LocalDate,
    fromTime: LocalTime,
    untilTime: LocalTime,
    kind: String,
    capacity: Int
):
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
    appointTimeId: Int,
    patientName: String,
    patientId: Int,
    memo: String,
    tags: Set[String]
)

case class AppEvent(
    appEventId: Int,
    createdAt: LocalDateTime,
    model: String,
    kind: String,
    data: String
)
