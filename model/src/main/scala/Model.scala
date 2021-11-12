package dev.myclinic.scala.model

import java.time.LocalDate
import java.time.LocalTime
import java.time.LocalDateTime
import dev.myclinic.scala.util.DateTimeOrdering.{*, given}
import scala.math.Ordered.orderingToOrdered

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
    else as.sliding(2).forall(e => e(0).isAdjacentTo(e(1)))

  given Ordering[AppointTime] with
    def compare(a: AppointTime, b: AppointTime): Int =
      val cmp: Int = summon[Ordering[LocalDate]].compare(a.date, b.date)
      if cmp == 0 then summon[Ordering[LocalTime]].compare(a.fromTime, b.fromTime)
      else cmp

case class Appoint(
    appointId: Int,
    appointTimeId: Int,
    patientName: String,
    patientId: Int,
    memo: String
):
  private lazy val memoCache =
    val stop = memo.indexOf("}}")
    if memo.startsWith("{{") && stop >= 2 then
      val t = memo.substring(2, stop).split(",").toSet
      val m = memo.substring(stop + 2)
      (m, t)
    else (memo, Set.empty)
  def memoString: String = memoCache._1
  def tags: Set[String] = memoCache._2
  def hasTag(tag: String): Boolean = tags.contains(tag)
  def modifyMemoString(s: String): Appoint =
    copy(memo = Appoint.constructMemo(s, tags))
  def modifyTags(tags: Set[String]): Appoint =
    copy(memo = Appoint.constructMemo(memoString, tags))

object Appoint:
  def create(
      appointId: Int,
      appointTimeId: Int,
      patientName: String,
      patientId: Int,
      memoString: String,
      tags: Set[String]
  ): Appoint =
    Appoint(
      appointId,
      appointTimeId,
      patientName,
      patientId,
      constructMemo(memoString, tags)
    )
  def constructMemo(s: String, ts: Set[String]): String =
    if ts.isEmpty then s
    else "{{" + ts.mkString(",") + "}}" + s

case class Hotline(message: String, sender: String, recipient: String)


