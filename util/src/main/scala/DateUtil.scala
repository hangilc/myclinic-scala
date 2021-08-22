package dev.myclinic.scala.util

import java.time.{LocalDate, LocalTime}

object DateUtil {

  def startDayOfWeek(at: LocalDate): LocalDate = {
    val n = at.getDayOfWeek().getValue()
    at.minusDays(n)
  }

}