package dev.fujiwara.holidayjp

import dev.fujiwara.kanjidate.DateUtil
import java.time.DayOfWeek.*
import java.time.*
import java.time.temporal.ChronoUnit.*
import scala.collection.mutable.ListBuffer

case class Holiday(date: LocalDate, name: String):
  override def toString(): String =
    s"${date}（${DateUtil.youbi(date)}） ${name}"

case class NationalHolidays(year: Int):
  val list: List[Holiday] = Adjust
    .adjust(
      NationalHolidayEnum.values
        .flatMap(e => e.date(year).map(d => Holiday(d, e.name)))
        .toList
    )
    .sortBy(h => h.date)
  val map: Map[LocalDate, Holiday] = Map(list.map(h => (h.date, h))*)

  def findByDate(d: LocalDate): Option[Holiday] = map.get(d)

  def print(): Unit = list.foreach(println)

type Year = Int

object NationalHolidays:
  val yearMap: Map[Year, NationalHolidays] = Map(
    2021 -> NationalHolidays(2021),
    2022 -> NationalHolidays(2022),
    2023 -> NationalHolidays(2023),
  )

  def findByDate(date: LocalDate): Option[Holiday] =
    val year = date.getYear
    if yearMap.contains(year) then yearMap(year).findByDate(date)
    else NationalHolidays(year).findByDate(date)
    //yearMap.get(date.getYear).flatMap(_.findByDate(date))

enum NationalHolidayEnum(val name: String, val date: Year => Option[LocalDate]):
  case Ganjitsu extends NationalHolidayEnum("元日", Spec.at(1, 1))
  case Seijin extends NationalHolidayEnum("成人の日", Spec.secondMonday(1))
  case Kenkoku extends NationalHolidayEnum("建国記念の日", Spec.at(2, 11))
  case Tennou extends NationalHolidayEnum("天皇誕生日", Spec.at(2, 23))
  case Shunbun
      extends NationalHolidayEnum("春分の日", year => Some(Orbit.shunbun(year)))
  case Shouwa extends NationalHolidayEnum("昭和の日", Spec.at(4, 29))
  case Kenpou extends NationalHolidayEnum("憲法記念日", Spec.at(5, 3))
  case Midori extends NationalHolidayEnum("みどりの日", Spec.at(5, 4))
  case Kodomo extends NationalHolidayEnum("こどもの日", Spec.at(5, 5))
  case Uminohi extends NationalHolidayEnum("海の日", Spec.uminohi)
  case Yamanohi extends NationalHolidayEnum("山の日", Spec.yamanohi)
  case Keirou extends NationalHolidayEnum("敬老の日", Spec.thirdMonday(9))
  case Shuubun
      extends NationalHolidayEnum("秋分の日", year => Some(Orbit.shuubun(year)))
  case Sports extends NationalHolidayEnum("スポーツの日", Spec.sports)
  case Bunka extends NationalHolidayEnum("文化の日", Spec.at(11, 3))
  case Kinrou extends NationalHolidayEnum("勤労感謝の日", Spec.at(11, 23))

object Spec:
  def at(month: Int, day: Int): Year => Option[LocalDate] = year =>
    Some(LocalDate.of(year, month, day))

  def nthMonday(month: Int, nthOneBased: Int): Year => Option[LocalDate] =
    year => Some(DateUtil.nthDayOfWeek(year, month, MONDAY, nthOneBased))

  def secondMonday(month: Int): Year => Option[LocalDate] = nthMonday(month, 2)
  def thirdMonday(month: Int): Year => Option[LocalDate] = nthMonday(month, 3)

  val uminohi: Year => Option[LocalDate] = year =>
    year match {
      case 2021 => at(7, 22)(year)
      case _    => thirdMonday(7)(year)
    }

  val yamanohi: Year => Option[LocalDate] = year =>
    year match {
      case 2021 => at(8, 8)(year)
      case _    => at(8, 11)(year)
    }

  val sports: Year => Option[LocalDate] = year =>
    year match {
      case 2021 => at(7, 23)(year)
      case _    => secondMonday(10)(year)
    }

object Orbit:
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
      case _ =>
        throw new RuntimeException(s"Cannot calculate shuubun for $year")
    }
    LocalDate.of(year, 9, d)

  private def mod4Map(year: Int, r0: Int, r1: Int, r2: Int, r3: Int): Int =
    year % 4 match {
      case 0 => r0
      case 1 => r1
      case 2 => r2
      case 3 => r3
    }

object Adjust:
  def adjust(holidays: List[Holiday]): List[Holiday] =
    val f = furikae(holidays).map(d => Holiday(d, "振替休日"))
    val s = sandwiched(holidays).map(d => Holiday(d, "休日"))
    f ++ s ++ holidays

  def furikae(holidays: Seq[Holiday]): List[LocalDate] =
    def isSunday(d: LocalDate): Boolean = d.getDayOfWeek == SUNDAY
    def pickFurikae(d: LocalDate): LocalDate =
      if holidays.contains(d) then pickFurikae(d.plus(1, DAYS)) else d
    val buf = ListBuffer[LocalDate]()
    holidays.foreach(h => {
      if isSunday(h.date) then buf.append(pickFurikae(h.date.plus(1, DAYS)))
    })
    buf.toList

  def sandwiched(holidays: Seq[Holiday]): List[LocalDate] =
    def isSunday(d: LocalDate): Boolean = d.getDayOfWeek == SUNDAY
    val buf = ListBuffer[LocalDate]()
    holidays.foreach(h => {
      val d1 = h.date.plus(1, DAYS)
      val d2 = h.date.plus(2, DAYS)
      if holidays.contains(d2) && !holidays.contains(d1) && !isSunday(d1) then
        buf.append(d1)
    })
    buf.toList
