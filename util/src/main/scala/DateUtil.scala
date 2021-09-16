package dev.myclinic.scala.util

import java.time.{LocalDate, LocalTime, LocalDateTime}
import java.time.format.DateTimeFormatter

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

