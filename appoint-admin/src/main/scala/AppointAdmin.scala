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

object AppointAdmin:

  def fillAppointTimesUpto(from: LocalDate, upto: LocalDate): IO[Unit] =
    def ensureNoExistingDate(dates: List[LocalDate]): Unit =
      if !dates.forall(d => d.isBefore(from) || d.isAfter(upto)) then
        throw new RuntimeException("Appoint time for the date already exists.")
    def filterDate(date: LocalDate): Boolean =
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
        val until = from.plus(20, ChronoUnit.SECONDS)
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
    def insertDates: List[LocalDate] =
      DateUtil
        .datesFrom(from)
        .takeWhile(d => d.isBefore(upto) || d.isEqual(upto))
        .filter(filterDate(_))
        .toList
    def appointTimesForDate(date: LocalDate): List[AppointTime] =
      val kind = "regular"
      val capacity = 1
      val tuples = pickTimes(date).sortBy(_._1)
      assert(
        !AppointTime.timeIntervalOverlaps(tuples),
        "AppointTime overlaps."
      )
      tuples.map { case (from, upto) => {
        val kind = "regular"
        val capacity = 1
        AppointTime(0, 0, date, from, upto, kind, capacity)
      }}
    def appointTimes: List[AppointTime] =
      insertDates.map(appointTimesForDate(_)).flatten

    for
      existingDates <- Db.listExistingAppointTimeDates(from, upto)
      _ = ensureNoExistingDate(existingDates)
      _ <- Db.batchEnterAppointTimes(appointTimes).void
    yield ()

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
