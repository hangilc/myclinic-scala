package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form, ErrorBox, Modifier, DateInput}
import scala.language.implicitConversions
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}
import dev.myclinic.scala.util.{KanjiDate, DateUtil}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.appbase.validator.ShahokokuhoValidator
import dev.myclinic.scala.web.appbase.validator.ShahokokuhoValidator.*
import java.time.LocalDate

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
      radio("honnin", "1"), "本人",
      radio("honnin", "0")( checked := true ), "家族"
    ),
    span("高齢") -> eKoureiForm(
      radio("kourei", "0")(checked := true), "高齢でない",
      radio("kourei", "2"), "２割",
      radio("kourei", "3"), "３割"
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
    eHonninForm.setRadioGroupValue(data.honninStore.toString)
    eKoureiForm.setRadioGroupValue(data.koureiStore.toString)
    eValidFrom.eInput.value = KanjiDate.dateToKanji(data.validFrom)
    eValidUpto.eInput.value = data.validUpto.value.fold("")(KanjiDate.dateToKanji(_))

  def validateForEnter(patientId: Int): ShahokokuhoValidator.Result[Shahokokuho] =
    ShahokokuhoValidator.validateShahokokuhoForEnter(
      validatePatientId(patientId),
      validateHokenshaBangouInput(eHokenshaBangou.value),
      validateHihokenshaKigou(eHihokenshaKigou.value),
      validateHihokenshaBangou(eHihokenshaBangou.value),
      validateHonnin(eHonninForm.getCheckedRadioValue),
      validateValidFrom(eValidFrom.eInput.value),
      validateValidUpto(eValidUpto.eInput.value),
      validateKourei(eKoureiForm.getCheckedRadioValue),
      validateEdaban(eEdaban.value)
    )

  def validateForUpdate(shahokokuhoId: Int, patientId: Int): ShahokokuhoValidator.Result[Shahokokuho] =
    ShahokokuhoValidator.validateShahokokuhoForUpdate(
      shahokokuhoId,
      validatePatientId(patientId),
      validateHokenshaBangouInput(eHokenshaBangou.value),
      validateHihokenshaKigou(eHihokenshaKigou.value),
      validateHihokenshaBangou(eHihokenshaBangou.value),
      validateHonnin(eHonninForm.getCheckedRadioValue),
      validateValidFrom(eValidFrom.eInput.value),
      validateValidUpto(eValidUpto.eInput.value),
      validateKourei(eKoureiForm.getCheckedRadioValue),
      validateEdaban(eEdaban.value)
    )

