package dev.myclinic.scala.util

import java.time.{LocalDate, LocalTime}
import dev.myclinic.scala.util.DateTimeOrdering
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
      formatYoubi: DateInfo => String = _.youbi
  ): String =
    val i: DateInfo = DateInfo(d)
    val year = formatYear(i)
    val month = formatMonth(i)
    val day = formatDay(i)
    val youbi = formatYoubi(i)
    s"${year}${month}${day}${youbi}"

  def timeToKanji(t: LocalTime): String =
    val hour = t.getHour
    val minute = t.getMinute
    s"${hour}時${minute}分"

  enum Gengou(val label: String, val alpha: String, val startDate: LocalDate):
    case Meiji extends Gengou("明治", "Meiji", LocalDate.of(1873, 1, 1))
    case Taishou extends Gengou("大正", "Taishou", LocalDate.of(1912, 7, 30))
    case Shouwa extends Gengou("昭和", "Shouwa", LocalDate.of(1926, 12, 25))
    case Heisei extends Gengou("平成", "Heisei", LocalDate.of(1989, 1, 8))
    case Reiwa extends Gengou("令和", "Reiwa", LocalDate.of(2019, 5, 1))

  object Gengou:
    val list: List[Gengou] =
      (0 until Gengou.values.size).reverse.map(Gengou.fromOrdinal(_)).toList

  case class Wareki(gengou: Gengou, nen: Int)

  object Wareki:
    def fromDate(date: LocalDate): Option[Wareki] =
      Gengou.list
        .find(g => g.startDate <= date)
        .map(g => Wareki(g, date.getYear - g.startDate.getYear + 1))

  class DateInfo(date: LocalDate):
    lazy val warekiOption: Option[Wareki] = Wareki.fromDate(date)
    def gengou: String = warekiOption.map(_.gengou.label).getOrElse("西暦")
    def nen: Int = warekiOption.map(_.nen).getOrElse(year)
    def year: Int = date.getYear
    def month: Int = date.getMonthValue
    def day: Int = date.getDayOfMonth
    def dayOfWeek: DayOfWeek = date.getDayOfWeek
    def dayOfWeekValue: Int = dayOfWeek.getValue() % 7
    def youbi: String = youbiList(dayOfWeekValue)
    def gengouAlpha: String = warekiOption.map(w => w.gengou.alpha).getOrElse("")
    def gengouAlphaChar: String = if gengouAlpha.isEmpty then "" else gengouAlpha.substring(0, 1)
