package dev.myclinic.scala.clinicop

import java.time.LocalDate
import java.time.DayOfWeek.*
import dev.myclinic.scala.util.DateUtil
import dev.fujiwara.holidayjp.NationalHolidays

sealed trait ClinicOperation:
  def code: String
case class InOperation() extends ClinicOperation:
  def code = "in-operation"
case class RegularHoliday() extends ClinicOperation:
  def code = "regular-holiday"
case class AdHocHoliday(name: String) extends ClinicOperation:
  def code = "ad-hoc-holiday"
case class AdHocWorkday(name: String) extends ClinicOperation:
  def code = "ad-hoc-workday"
case class NationalHoliday(name: String) extends ClinicOperation:
  def code = "national-holiday"

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
      if dow == SUNDAY || dow == WEDNESDAY then Some(RegularHoliday())
      else None
    (isAdHocWorkday ||> isRegularHoliday ||> isAdHocHoliday ||> isNationalHoliday)
      .getOrElse(
        InOperation()
      )

  def getLabel(op: ClinicOperation): String =
    op match {
      case AdHocHoliday(name)    => name
      case AdHocWorkday(name)    => name
      case NationalHoliday(name) => name
      case _                     => ""
    }

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
