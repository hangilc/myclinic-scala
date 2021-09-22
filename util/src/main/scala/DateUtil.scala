package dev.myclinic.scala.util

import java.time.{LocalDate, LocalTime, LocalDateTime, DayOfWeek}
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.ChronoUnit.DAYS
import java.time.DayOfWeek.*

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

  def youbi(date: LocalDate): String = youbi(date.getDayOfWeek)

  def youbi(dayOfWeek: DayOfWeek): String = dayOfWeek match {
    case SUNDAY => "日"
    case MONDAY => "月"
    case TUESDAY => "火"
    case WEDNESDAY => "水"
    case THURSDAY => "木"
    case FRIDAY => "金"
    case SATURDAY => "土"
  }

  private def mod4Map(year: Int, r0: Int, r1: Int, r2: Int, r3: Int): Int =
    year % 4 match {
      case 0 => r0
      case 1 => r1
      case 2 => r2
      case 3 => r3
    }

  def shunbun(year: Int): LocalDate =
    val d = year match {
      case x if x >= 1992 && x <= 2023 => mod4Map(year, 20, 20, 21, 21)
      case x if x >= 2024 && x <= 2055 => mod4Map(year, 20, 20, 20, 21)
      case x if x >= 2056 && x <= 2091 => mod4Map(year, 20, 20, 20, 20)
      case x if x >= 2092 && x <= 2099 => mod4Map(year, 19, 20, 20, 20)
      case _ => throw new RuntimeException(s"Cannot calculate shubun for $year")
    }
    LocalDate.of(year, 3, d)

  def shuubun(year: Int): LocalDate =
    val d: Int = year match {
      case x if x >= 2012 && x <= 2043 => mod4Map(year, 22, 23, 23, 23)
      case x if x >= 2044 && x <= 2075 => mod4Map(year, 22, 22, 23, 23)
      case x if x >= 2076 && x <= 2099 => mod4Map(year, 22, 22, 22, 23)
      case _ => throw new RuntimeException(s"Cannot calculate shuubun for $year")
    }
    LocalDate.of(year, 9, d)
