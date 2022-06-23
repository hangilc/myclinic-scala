package dev.fujiwara.dateinput

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.kanjidate.KanjiDate.{Gengou, Seireki, Era, eraName}
import java.time.LocalDate
import dev.fujiwara.kanjidate.KanjiDate
import dev.fujiwara.validator.ValidatorUtil.*

class DateInputForm(
    var value: Option[LocalDate] = None,
    gengouList: List[Era] = Gengou.values.toList.reverse
):
  val gengouSelect = SelectProxy(gengouList, (opt, e) => opt(eraName(e)))
  val nenInput = input
  val monthInput = input
  val dayInput = input
  val ele = div(
    gengouSelect.ele(cls := "domq-date-input-form-gengou"),
    nenInput(cls := "domq-date-input-form-nen"),
    "年",
    monthInput(cls := "domq-date-input-form-month"),
    "月",
    dayInput(cls := "domq-date-input-form-day"),
    "日"
  )
  updateUI()

  private def updateUI(): Unit =
    value match {
      case None =>
        nenInput.value = ""
        monthInput.value = ""
        dayInput.value = ""
      case Some(d) =>
        val di = KanjiDate.DateInfo(d)
        if !gengouSelect.contains(di.era) then
          gengouSelect.prepend(di.era)
        gengouSelect.simulateSelect(di.era)
        nenInput.value = di.nen.toString
        monthInput.value = di.month.toString
        dayInput.value = di.day.toString
    }

object DateInputFormValidator:
  import cats.data.Validated
  import dev.fujiwara.validator.ValidatorUtil.*

  type GroupedError[G] = (G, ValidationError)

  class SectionValidator[G](name: String):
    type Result[T] = Validated[List[E], T]
    def error(g: G, msg: String): GroupedError[G] =
      (g, )
      new GroupedError(g, msg)

    def validateIsSome[T](src: Option[T]): Result[T] =
      isSome
    def validateNotEmpty[T](src: String): Result[T] =
      ???

  enum ErrorGroup(name: String) extends SectionValidator[ErrorGroup, GroupedError[ErrorGroup]](name):
    case GengouError extends ErrorGroup("元号")
    case NenError extends ErrorGroup("年")
    case MonthError extends ErrorGroup("月")
    case DayError extends ErrorGroup("日")
    case InvalidValueError extends ErrorGroup("指定されて日付が無効な日付です。")


