package dev.myclinic.scala.util

import java.time.LocalDate
import java.time.DayOfWeek.*
import dev.myclinic.scala.util.DateUtil
import dev.myclinic.scala.util.holidayjp.NationalHolidays

trait ClinicOperation
object InOperation extends ClinicOperation
object RegularHoliday extends ClinicOperation
case class AdHocHoliday(name: String) extends ClinicOperation
case class AdHocWorkday(name: String) extends ClinicOperation
case class NationalHoliday(name: String) extends ClinicOperation

object ClinicOperation:
  import ClinicOperation.*

  extension [A, B >: A](opt: Option[A])
    def ||>(other: Option[B]): Option[B] = opt.orElse(other)

  def getClinicOperationAt(date: LocalDate): ClinicOperation =
    def isAdHocWorkday: Option[ClinicOperation] = 
      adHocWorkdayMap.get(date)
    def isAdHocHoliday: Option[ClinicOperation] = 
      adHocHolidayMap.get(date)
    def isNationalHoliday: Option[ClinicOperation] = 
      NationalHolidays.findByDate(date).map(h => NationalHoliday(h.name))
    def isRegularHoliday: Option[ClinicOperation] =
      val dow = date.getDayOfWeek
      if dow == SUNDAY || dow == WEDNESDAY then Some(RegularHoliday)
      else None
    (isAdHocWorkday ||> isAdHocHoliday ||> isRegularHoliday).getOrElse(
      InOperation
    )

  def adHocHolidayRange(
      from: LocalDate,
      upto: LocalDate,
      name: String
  ): Set[(LocalDate, AdHocHoliday)] =
    DateUtil.enumDates(from, upto).map(date => (date, AdHocHoliday(name))).toSet

  val adHocHolidayMap: Map[LocalDate, AdHocHoliday] =
    Map(
      adHocHolidayRange(
        LocalDate.of(2021, 12, 29),
        LocalDate.of(2022, 1, 5),
        "冬休み"
      ).toList: _*
    )

  val adHocWorkdayMap: Map[LocalDate, AdHocWorkday] = Map.empty
