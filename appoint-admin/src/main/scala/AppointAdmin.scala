package dev.myclinic.scala.appoint.admin

import dev.myclinic.scala.util.DateUtil
import java.time.{LocalDate, LocalTime, DayOfWeek}
import java.time.DayOfWeek.*

object AppointAdmin:

  def enterRegularAppointTimes(year: Int, month: Int): Unit = 
    val lastDay = DateUtil.lastDayOfMonth(year, month)
    for day <- 1 to lastDay do
      val date = LocalDate.of(year, month, day)
      val dow = date.getDayOfWeek
      val times = regularAppointTimes(dow)
      if !times.isEmpty then
        println(s"$year $month $day")
        println(times)
  
  def regularAppointTimes(dayOfWeek: DayOfWeek): List[LocalTime] =
    dayOfWeek match {
        case SUNDAY | WEDNESDAY => List.empty
        case SATURDAY => saturdayAppointTimes
        case _ => regularDayAppointTimes
    }
  
  def saturdayAppointTimes: List[LocalTime] = 
    List(9, 10, 11).flatMap(regularAppointTimes(_))

  def regularDayAppointTimes: List[LocalTime] =
    List(9, 10, 11, 14, 15, 16, 17).flatMap(regularAppointTimes(_))

  private def regularAppointTimes(hour: Int): List[LocalTime] =
    hour match {
      case 9 => List(time(9, 40))
      case 17 => List(time(17, 0))
      case _ => List(time(hour, 0), time(hour, 20), time(hour, 40))
    }

  private def time(hour: Int, minute: Int): LocalTime =
    LocalTime.of(hour, minute, 0)
