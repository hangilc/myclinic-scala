package dev.myclinic.scala.appoint.admin

import cats.*
import cats.syntax.all.*
import cats.effect.IO
import dev.myclinic.scala.util.DateUtil
import dev.myclinic.scala.util.ClinicOperation
import dev.myclinic.scala.db.Db
import dev.myclinic.scala.model.*
import java.time.{LocalDate, LocalTime, DayOfWeek}
import java.time.DayOfWeek.*
import java.time.temporal.ChronoUnit
import cats.effect.unsafe.implicits.global

object AppointAdmin:
  extension [T](io: IO[T]) def run(): T = io.unsafeRunSync()

  def fillAppointTimesUpto(from: LocalDate, upto: LocalDate): IO[Unit] =
    def requestedDates: List[LocalDate] =
      DateUtil
        .datesFrom(from)
        .takeWhile(d => d.isBefore(upto) || d.isEqual(upto))
        .toList
    def excludeExistingDates(
        dates: List[LocalDate],
        existing: List[LocalDate]
    ): List[LocalDate] =
      dates.filter(d => !existing.contains(d))
    def filterOperationDate(date: LocalDate): Boolean =
      import ClinicOperation.*
      ClinicOperation.getClinicOperationAt(date) match {
        case InOperation        => true
        case RegularHoliday     => false
        case AdHocHoliday(_)    => false
        case NationalHoliday(_) => false
      }
    def pickTimes(date: LocalDate): List[(LocalTime, LocalTime)] =
      def t(hour: Int, minute: Int): (LocalTime, LocalTime) =
        val from = LocalTime.of(hour, minute)
        val until = from.plus(20, ChronoUnit.MINUTES)
        (from, until)
      date.getDayOfWeek match {
        case SATURDAY =>
          List(
            t(9, 40),
            t(10, 0),
            t(10, 20),
            t(10, 40),
            t(11, 0),
            t(11, 20),
            t(11, 40)
          )
        case SUNDAY | WEDNESDAY => List.empty
        case _ =>
          List(
            t(9, 40),
            t(10, 0),
            t(10, 20),
            t(10, 40),
            t(11, 0),
            t(11, 20),
            t(11, 40),
            t(14, 0),
            t(14, 20),
            t(14, 40),
            t(15, 0),
            t(15, 20),
            t(15, 40),
            t(16, 0),
            t(16, 20),
            t(16, 40),
            t(17, 0)
          )
      }
    def appointTimesForDate(date: LocalDate): List[AppointTime] =
      val kind = "regular"
      val capacity = 1
      val tuples = pickTimes(date).sortBy(_._1)
      assert(
        !AppointTime.timeIntervalOverlaps(tuples),
        "AppointTime overlaps."
      )
      tuples.map {
        case (from, upto) => {
          val kind = "regular"
          val capacity = 1
          AppointTime(0, 0, date, from, upto, kind, capacity)
        }
      }

    for
      existingDates <- Db.listExistingAppointTimeDates(from, upto)
      existingDatesExcluded = excludeExistingDates(
        requestedDates,
        existingDates
      )
      targetDates = existingDatesExcluded.filter(filterOperationDate(_))
      appointTimes = targetDates.map(appointTimesForDate(_)).flatten
      _ <- Db.batchEnterAppointTimes(appointTimes).void
    yield ()

  def listAppointTimes(
      from: LocalDate,
      upto: LocalDate
  ): IO[List[AppointTime]] =
    Db.listAppointTimes(from, upto)

  def listAppointTimesForDate(date: LocalDate): IO[List[AppointTime]] =
    listAppointTimes(date, date)

  def printAppointTimes(date: LocalDate): IO[Unit] =
    def format(a: AppointTime): String =
      val detail =
        if a.kind == "regular" then ""
        else s" ${a.kind}(${a.capacity})"
      s"${a.appointTimeId} ${a.date} ${a.fromTime}-${a.untilTime} ${detail}"

    for
      times <- listAppointTimesForDate(date)
      _ <- times.map(a => IO.println(format(a))).sequence
    yield ()

  def convertAppointTime(
      appointTimes: List[AppointTime],
      kind: String,
      capacity: Int
  ): IO[AppointTime] =
    assert(appointTimes.size > 0, "Empty appoint time list.")
    assert(
      AppointTime.isAdjacentRun(appointTimes),
      "Adjacent appoint times expected."
    )
    def create(): AppointTime = 
      val first: AppointTime = appointTimes.head
      val last: AppointTime = appointTimes.last
      AppointTime(0, 0, first.date, first.fromTime, last.untilTime, kind, capacity)
    for 
      _ <- Db.batchDeleteAppointTimes(appointTimes)
      result <- Db.createAppointTime(create())
    yield result

  def getAppointTimeById(appointTimeId: Int): IO[AppointTime] =
    Db.getAppointTimeById(appointTimeId)

// def enterRegularAppointTimes(year: Int, month: Int): Unit =
//   val lastDay = DateUtil.lastDayOfMonth(year, month)
//   for day <- 1 to lastDay do
//     val date = LocalDate.of(year, month, day)
//     val dow = date.getDayOfWeek
//     val times = regularAppointTimes(dow)
//     if !times.isEmpty then
//       println(s"$year $month $day")
//       println(times)

// def regularAppointTimes(dayOfWeek: DayOfWeek): List[LocalTime] =
//   dayOfWeek match {
//       case SUNDAY | WEDNESDAY => List.empty
//       case SATURDAY => saturdayAppointTimes
//       case _ => regularDayAppointTimes
//   }

// def saturdayAppointTimes: List[LocalTime] =
//   List(9, 10, 11).flatMap(regularAppointTimes(_))

// def regularDayAppointTimes: List[LocalTime] =
//   List(9, 10, 11, 14, 15, 16, 17).flatMap(regularAppointTimes(_))

// private def regularAppointTimes(hour: Int): List[LocalTime] =
//   hour match {
//     case 9 => List(time(9, 40))
//     case 17 => List(time(17, 0))
//     case _ => List(time(hour, 0), time(hour, 20), time(hour, 40))
//   }

// private def time(hour: Int, minute: Int): LocalTime =
//   LocalTime.of(hour, minute, 0)
