package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form, ErrorBox, Modifier}
import scala.language.implicitConversions
import org.scalajs.dom.{HTMLElement, HTMLInputElement}
import dev.myclinic.scala.util.DateUtil
import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.appbase.validator.ShahokokuhoValidator
import dev.myclinic.scala.web.appbase.validator.ShahokokuhoValidator.*
import java.time.LocalDate
import dev.fujiwara.dateinput.DateInput
import dev.fujiwara.kanjidate.KanjiDate

class ShahokokuhoForm:
  val eHokenshaBangou = inputText()
  val eHihokenshaKigou = inputText()
  val eHihokenshaBangou = inputText()
  val eHonninForm = form()
  val eKoureiForm = form()
  val eValidFrom = DateInput()
  val eValidUpto = DateInput()
  val eEdaban = inputText()
  val ele = Form.rows(
    span("保険者番号") -> eHokenshaBangou(
      cls := "hokensha-bangou-input"
    ),
    span("被保険者") -> div(
      eHihokenshaKigou(cls := "hihokensha-kigou", placeholder := "記号"),
      eHihokenshaBangou(cls := "hihokensha-bangou", placeholder := "番号")
    ),
    span("枝番") -> eEdaban,
    span("本人・家族") -> eHonninForm(
      radio(name := "honnin", value := "1"), "本人",
      radio(name := "honnin", value := "0")( checked := true ), "家族"
    ),
    span("高齢") -> eKoureiForm(
      radio(name := "kourei", value := "0")(checked := true), "高齢でない",
      radio(name := "kourei", value := "2"), "２割",
      radio(name := "kourei", value := "3"), "３割"
    ),
    span("期限開始") -> eValidFrom.ele,
    span("期限終了") -> eValidUpto.ele
  )
  ele(cls := "shahokokuho-form")

  def setData(data: Shahokokuho): Unit =
    eHokenshaBangou.value = data.hokenshaBangou.toString
    eHihokenshaKigou.value = data.hihokenshaKigou
    eHihokenshaBangou.value = data.hihokenshaBangou
    eEdaban.value = data.edaban
    eHonninForm.setRadioGroupValue("honnin", data.honninStore.toString)
    eKoureiForm.setRadioGroupValue("kourei", data.koureiStore.toString)
    eValidFrom.eInput.value = KanjiDate.dateToKanji(data.validFrom)
    eValidUpto.eInput.value = data.validUpto.value.fold("")(KanjiDate.dateToKanji(_))

  def validateForEnter(patientId: Int): ShahokokuhoValidator.Result[Shahokokuho] =
    ShahokokuhoValidator.validateShahokokuhoForEnter(
      validatePatientId(patientId),
      validateHokenshaBangouInput(eHokenshaBangou.value),
      validateHihokenshaKigou(eHihokenshaKigou.value),
      validateHihokenshaBangou(eHihokenshaBangou.value),
      validateHonnin(eHonninForm.getCheckedRadioValue("honnin")),
      validateValidFrom(eValidFrom.validate(), _.message),
      validateValidUpto(eValidUpto.validateOption().map(ValidUpto(_)), _.message),
      validateKourei(eKoureiForm.getCheckedRadioValue("kourei")),
      validateEdaban(eEdaban.value)
    )

  def validateForUpdate(shahokokuhoId: Int, patientId: Int): ShahokokuhoValidator.Result[Shahokokuho] =
    ShahokokuhoValidator.validateShahokokuhoForUpdate(
      shahokokuhoId,
      validatePatientId(patientId),
      validateHokenshaBangouInput(eHokenshaBangou.value),
      validateHihokenshaKigou(eHihokenshaKigou.value),
      validateHihokenshaBangou(eHihokenshaBangou.value),
      validateHonnin(eHonninForm.getCheckedRadioValue("honnin")),
      validateValidFrom(eValidFrom.validate(), _.message),
      validateValidUpto(eValidUpto.validateOption().map(ValidUpto(_)), _.message),
      validateKourei(eKoureiForm.getCheckedRadioValue("kourei")),
      validateEdaban(eEdaban.value)
    )

