package dev.myclinic.scala.util

import java.time.LocalDate
import java.time.DayOfWeek.*

sealed trait ClinicOperation
case object InOperation extends ClinicOperation
case object RegularHoliday extends ClinicOperation
case class AdHocHoliday(name: String) extends ClinicOperation
case class NationalHoliday(name: String) extends ClinicOperation

object ClinicOperation:
  extension [A, B >: A](opt: Option[A])
    def ||>(other: Option[B]): Option[B] = opt.orElse(other)

  def getClinicOperationAt(date: LocalDate): ClinicOperation =
    import ClinicOperation.*
    def isAdHocWorkday: Option[ClinicOperation] = None
    def isAdHocHoliday: Option[ClinicOperation] = None
    def isRegularHoliday: Option[ClinicOperation] =
      val dow = date.getDayOfWeek
      if dow == SUNDAY || dow == WEDNESDAY then Some(RegularHoliday)
      else None
    (isAdHocWorkday ||> isAdHocHoliday ||> isRegularHoliday).getOrElse(
      InOperation
    )
