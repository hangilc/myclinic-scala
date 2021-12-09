package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form, ErrorBox, Modifier}
import scala.language.implicitConversions
import dev.fujiwara.kanjidate.KanjiDate
import dev.fujiwara.dateinput.DateInput
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}
import dev.myclinic.scala.util.DateUtil
import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.appbase.validator.KouhiValidator
import dev.myclinic.scala.web.appbase.validator.KouhiValidator.*
import java.time.LocalDate

class KouhiForm:
  val eFutanshaBangou = inputText()
  val eJukyuushaBangou = inputText()
  val eValidFrom = DateInput()
  val eValidUpto = DateInput()
  val ele = Form.rows(
    span("負担者番号") -> eFutanshaBangou(
      cls := "futansha-bangou-input"
    ),
    span("受給者番号") -> eJukyuushaBangou,
    span("期限開始") -> eValidFrom.ele,
    span("期限終了") -> eValidUpto.ele
  )
  ele(cls := "kouhi-form")

  def setData(data: Kouhi): Unit =
    eFutanshaBangou.value = data.futansha.toString
    eJukyuushaBangou.value = data.jukyuusha.toString
    eValidFrom.eInput.value = KanjiDate.dateToKanji(data.validFrom)
    eValidUpto.eInput.value =
      data.validUpto.value.fold("")(KanjiDate.dateToKanji(_))

  def validateForEnter(
      patientId: Int
  ): KouhiValidator.Result[Kouhi] =
    KouhiValidator.validateKouhiForEnter(
      validatePatientId(patientId),
      validateFutanshaBangou(eFutanshaBangou.value),
      validateJukyuushaBangou(eJukyuushaBangou.value),
      validateFutanWari(eFutanWariForm.getCheckedRadioValue),
      validateValidFrom(eValidFrom.validate(), _.message),
      validateValidUpto(
        eValidUpto.validateOption().map(ValidUpto(_)),
        _.message
      )
    )

  def validateForUpdate(
      kouhiId: Int,
      patientId: Int
  ): KouhiValidator.Result[Kouhi] =
    KouhiValidator.validateKouhiForUpdate(
      kouhiId,
      validatePatientId(patientId),
      validateFutanshaBangou(eFutanshaBangou.value),
      validateJukyuushaBangou(eJukyuushaBangou.value),
      validateFutanWari(eFutanWariForm.getCheckedRadioValue),
      validateValidFrom(eValidFrom.validate(), _.message),
      validateValidUpto(
        eValidUpto.validateOption().map(ValidUpto(_)),
        _.message
      )
    )
