package dev.myclinic.scala.util

import java.time.LocalDate
import java.time.DayOfWeek.*

enum ClinicOperation:
  case InOperation extends ClinicOperation
  case RegularHoliday extends ClinicOperation
  case AdHocHoliday(name: String) extends ClinicOperation
  case NationalHoliday(name: String) extends ClinicOperation
end ClinicOperation

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
