package dev.myclinic.scala.util

import java.time.{LocalDate, LocalTime, LocalDateTime, DayOfWeek}
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

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
    nextMonthFirst.minus(1, ChronoUnit.DAYS)
  
  def firstSunday(year: Int, month: Int): LocalDate =
    val firstDay = LocalDate.of(year, month, 1)
    val firstDayDow = firstDay.getDayOfWeek.getValue()
    if firstDayDow == 0 then firstDay
    else
      firstDay.plus(7 - firstDayDow, ChronoUnit.DAYS)
  
  def nthDayOfWeek(nthZeroBased: Int, dayOfWeek: DayOfWeek): LocalDate = ???



