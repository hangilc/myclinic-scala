package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form, ErrorBox, Modifier}
import scala.language.implicitConversions
import dev.fujiwara.kanjidate.KanjiDate
import dev.fujiwara.dateinput.DateInput
import org.scalajs.dom.{HTMLElement, HTMLInputElement}
import dev.myclinic.scala.util.DateUtil
import dev.myclinic.scala.model.*
import java.time.LocalDate
import dev.myclinic.scala.web.appbase.validator.KoukikoureiValidator
import dev.myclinic.scala.web.appbase.validator.KoukikoureiValidator.*

class KoukikoureiForm:
  val eHokenshaBangou = inputText()
  val eHihokenshaBangou = inputText()
  val eFutanWariForm = form()
  val eValidFrom = DateInput()
  val eValidUpto = DateInput()
  val ele = Form.rows(
    span("保険者番号") -> eHokenshaBangou(
      cls := "hokensha-bangou-input"
    ),
    span("被保険者番号") -> eHihokenshaBangou,
    span("負担割") -> eFutanWariForm(
      radio(name := "futanwari", value := "1")(checked := true),
      "１割",
      radio(name := "futanwari", value := "2"),
      "２割",
      radio(name := "futanwari", value := "3"),
      "３割"
    ),
    span("期限開始") -> eValidFrom.ele,
    span("期限終了") -> eValidUpto.ele
  )
  ele(cls := "koukikourei-form")

  def setData(data: Koukikourei): Unit =
    eHokenshaBangou.value = data.hokenshaBangou.toString
    eHihokenshaBangou.value = data.hihokenshaBangou
    eFutanWariForm.setRadioGroupValue("futanwari", data.futanWari.toString)
    eValidFrom.set(Some(data.validFrom))
    eValidUpto.set(data.validUpto.value)

  def validateForEnter(
      patientId: Int
  ): Result[Koukikourei] =
    validateKoukikoureiForEnter(
      validatePatientId(patientId),
      validateHokenshaBangou(eHokenshaBangou.value),
      validateHihokenshaBangou(eHihokenshaBangou.value),
      validateFutanWari(eFutanWariForm.getCheckedRadioValue("futanwari")),
      validateValidFrom(eValidFrom.value),
      validateValidUpto(eValidUpto.value)
    )

  def validateForUpdate(
      koukikoureiId: Int,
      patientId: Int
  ): Result[Koukikourei] =
    validateKoukikourei(
      validateKoukikoureiIdForUpdate(koukikoureiId),
      validatePatientId(patientId),
      validateHokenshaBangou(eHokenshaBangou.value),
      validateHihokenshaBangou(eHihokenshaBangou.value),
      validateFutanWari(eFutanWariForm.getCheckedRadioValue("futanwari")),
      validateValidFrom(eValidFrom.value),
      validateValidUpto(eValidUpto.value)
    )
