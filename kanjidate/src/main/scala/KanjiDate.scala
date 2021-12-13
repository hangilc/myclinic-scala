package dev.fujiwara.kanjidate

import java.time.{LocalDate, LocalTime}
import dev.fujiwara.kanjidate.DateTimeOrdering
import scala.Ordered.orderingToOrdered
import java.time.DayOfWeek

object KanjiDate:

  val youbiList: List[String] = List("日", "月", "火", "水", "木", "金", "土")

  def youbi(d: LocalDate): String =
    val i = d.getDayOfWeek().getValue() % 7
    youbiList(i)

  def dateToKanji(
      d: LocalDate,
      formatYear: DateInfo => String = i => s"${i.gengou}${i.nen}年",
      formatMonth: DateInfo => String = i => s"${i.month}月",
      formatDay: DateInfo => String = i => s"${i.day}日",
      formatYoubi: DateInfo => String = _ => ""
  ): String =
    val i: DateInfo = DateInfo(d)
    val year = formatYear(i)
    val month = formatMonth(i)
    val day = formatDay(i)
    val youbi = formatYoubi(i)
    s"${year}${month}${day}${youbi}"

  def timeToKanji(
      t: LocalTime,
      formatHour: Int => String = hour => s"${hour}時",
      formatMinute: Int => String = minute => s"${minute}分",
      formatSecond: Int => String = second => ""
  ): String =
    val info = TimeInfo(t)
    val hour = formatHour(info.hour)
    val minute = formatMinute(info.minute)
    val second = formatSecond(info.second)
    s"${hour}${minute}${second}"

  enum Gengou(val name: String, val alpha: String, val startDate: LocalDate):
    case Meiji extends Gengou("明治", "Meiji", LocalDate.of(1873, 1, 1))
    case Taishou extends Gengou("大正", "Taishou", LocalDate.of(1912, 7, 30))
    case Shouwa extends Gengou("昭和", "Shouwa", LocalDate.of(1926, 12, 25))
    case Heisei extends Gengou("平成", "Heisei", LocalDate.of(1989, 1, 8))
    case Reiwa extends Gengou("令和", "Reiwa", LocalDate.of(2019, 5, 1))

    def next: Option[Gengou] =
      val nextOrd = ordinal + 1
      if nextOrd < Gengou.values.size then Some(Gengou.fromOrdinal(nextOrd))
      else None

    def lastDay: Option[LocalDate] =
      next.map(ng => ng.startDate.minusDays(1))

    def listNen: Range =
      val firstYear = startDate.getYear
      val lastYear: Int =
        lastDay.map(_.getYear).getOrElse(LocalDate.now().getYear)
      1 to (lastYear - firstYear + 1)

  object Gengou:
    val list: List[Gengou] =
      (0 until Gengou.values.size).reverse.map(Gengou.fromOrdinal(_)).toList

    val current: Gengou = Gengou.Reiwa
    val recent: List[Gengou] = List(Gengou.Reiwa, Gengou.Heisei)

    def findByName(name: String): Option[Gengou] =
      Gengou.values.find(_.name == name)

    def gengouToYear(g: Gengou, nen: Int): Int =
      g.startDate.getYear + nen - 1

  case class Wareki(gengou: Gengou, nen: Int)

  object Wareki:
    def fromDate(date: LocalDate): Option[Wareki] =
      Gengou.list
        .find(g => g.startDate <= date)
        .map(g => Wareki(g, date.getYear - g.startDate.getYear + 1))

  class DateInfo(date: LocalDate):
    lazy val warekiOption: Option[Wareki] = Wareki.fromDate(date)
    def gengou: String = warekiOption.map(_.gengou.name).getOrElse("西暦")
    def nen: Int = warekiOption.map(_.nen).getOrElse(year)
    def year: Int = date.getYear
    def month: Int = date.getMonthValue
    def day: Int = date.getDayOfMonth
    def dayOfWeek: DayOfWeek = date.getDayOfWeek
    def dayOfWeekValue: Int = dayOfWeek.getValue() % 7
    def youbi: String = youbiList(dayOfWeekValue)
    def gengouAlpha: String =
      warekiOption.map(w => w.gengou.alpha).getOrElse("")
    def gengouAlphaChar: String =
      if gengouAlpha.isEmpty then "" else gengouAlpha.substring(0, 1)

  class TimeInfo(time: LocalTime):
    def hour: Int = time.getHour
    def minute: Int = time.getMinute
    def second: Int = time.getSecond
