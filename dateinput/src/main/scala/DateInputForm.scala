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

object DateInputFormValidator


