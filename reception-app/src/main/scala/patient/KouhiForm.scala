package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form, ErrorBox, Modifier}
import scala.language.implicitConversions
import dev.fujiwara.kanjidate.KanjiDate
import dev.fujiwara.dateinput.{DateInput, DateOptionInput}
import org.scalajs.dom.{HTMLElement, HTMLInputElement}
import dev.myclinic.scala.util.DateUtil
import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.appbase.validator.KouhiValidator
import dev.myclinic.scala.web.appbase.validator.KouhiValidator.*
import java.time.LocalDate

class KouhiForm:
  val eFutanshaBangou = inputText()
  val eJukyuushaBangou = inputText()
  val eValidFrom = DateOptionInput()
  val eValidUpto = DateOptionInput()
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
    eValidFrom.init(Some(data.validFrom))
    eValidUpto.init(data.validUpto.value)

  def validateForEnter(
      patientId: Int
  ): KouhiValidator.Result[Kouhi] =
    import cats.data.Validated.*
    import KouhiValidator.*
    KouhiValidator.validateKouhiForEnter(
      validatePatientId(patientId),
      validateFutanshaBangou(eFutanshaBangou.value),
      validateJukyuushaBangou(eJukyuushaBangou.value),
      validateValidFrom(eValidFrom.value),
      eValidUpto.value
    )

  def validateForUpdate(
      kouhiId: Int,
      patientId: Int
  ): KouhiValidator.Result[Kouhi] =
    import cats.data.Validated.*
    import KouhiValidator.*
    KouhiValidator.validateKouhiForUpdate(
      validateKouhiIdForUpdate(kouhiId),
      validatePatientId(patientId),
      validateFutanshaBangou(eFutanshaBangou.value),
      validateJukyuushaBangou(eJukyuushaBangou.value),
      validateValidFrom(eValidFrom.value),
      eValidUpto.value
    )
