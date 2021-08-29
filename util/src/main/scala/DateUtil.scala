package dev.myclinic.scala.util

import java.time.LocalDate

object DateUtil {

  def startDayOfWeek(at: LocalDate): LocalDate = {
    val n = at.getDayOfWeek().getValue() % 7
    at.minusDays(n)
  }

}