package dev.myclinic.scala.util

import java.time.{LocalDate, LocalTime, LocalDateTime, DayOfWeek}
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.ChronoUnit.DAYS

object DateUtil:

  def startDayOfWeek(at: LocalDate): LocalDate =
    val n = at.getDayOfWeek().getValue() % 7
    at.minusDays(n)

  val sqlDateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("uuuu-MM-dd")

  val sqlTimeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("HH:mm:ss")

  val sqlDateTimeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")

  def stringToDate(str: String): LocalDate =
    LocalDate.parse(str, sqlDateFormatter)

  def dateToString(d: LocalDate): String = d.format(sqlDateFormatter)

  def stringToTime(str: String): LocalTime =
    LocalTime.parse(str, sqlTimeFormatter)

  def timeToString(t: LocalTime): String = t.format(sqlTimeFormatter)

  def stringtoDateTime(str: String): LocalDateTime =
    LocalDateTime.parse(str, sqlDateTimeFormatter)

  def dateTimeToString(dt: LocalDateTime): String =
    dt.format(sqlDateTimeFormatter)

  def lastDayOfMonth(year: Int, month: Int): LocalDate =
    val nextMonthFirst = LocalDate.of(year, month, 1).plus(1, ChronoUnit.MONTHS)
    nextMonthFirst.minus(1, DAYS)

  def firstDayOfWeek(year: Int, month: Int, dayOfWeek: DayOfWeek): LocalDate =
    val firstDay = LocalDate.of(year, month, 1)
    val offset = dayOfWeek.getValue() - firstDay.getDayOfWeek().getValue()
    if offset == 0 then firstDay
    else if offset > 0 then firstDay.plus(offset, DAYS)
    else firstDay.plus(7 + offset, DAYS)

  def nthDayOfWeek(
      year: Int,
      month: Int,
      dayOfWeek: DayOfWeek,
      nthOneBased: Int
  ): LocalDate =
    firstDayOfWeek(year, month, dayOfWeek).plus((nthOneBased - 1) * 7, DAYS)

  def shunbun(year: Int): LocalDate = year match {
    
  }
