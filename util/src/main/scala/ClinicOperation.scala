package dev.myclinic.scala.util

import java.time.LocalDate
import java.time.DayOfWeek.*

sealed trait ClinicOperation
case object InOperation extends ClinicOperation
case object RegularOutOfOperation extends ClinicOperation
case class ClinicHoliday(name: String) extends ClinicOperation
case class NationalHoliday(name: String) extends ClinicOperation

object ClinicOperation:
  def getClinicOperationAt(date: LocalDate): ClinicOperation = 
    import ClinicOperation.*
    val dow = date.getDayOfWeek
    if dow == SUNDAY || dow == WEDNESDAY then RegularOutOfOperation
    else InOperation
