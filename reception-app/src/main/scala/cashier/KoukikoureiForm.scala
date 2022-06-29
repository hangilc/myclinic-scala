package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.fujiwara.dateinput.DateOptionInput
import dev.fujiwara.domq.DispPanel
import dev.fujiwara.dateinput.InitNoneConverter
import java.time.LocalDate
import dev.myclinic.scala.web.appbase.validator.KoukikoureiValidator
import dev.myclinic.scala.web.appbase.validator.KoukikoureiValidator.*
import org.scalajs.dom.HTMLElement
import org.scalajs.dom.HTMLInputElement
import dev.myclinic.scala.web.appbase.formprop.*
import dev.myclinic.scala.web.appbase.formprop.Prop.given

class KoukikoureiForm(init: Option[Koukikourei]):

  val futanWariData = List(
    "１割" -> 1,
    "２割" -> 2,
    "３割" -> 3
  )

  val hokenshaBangouProp =
    Prop("保険者番号", input, HokenshaBangouValidator.validate)
  val hihokenshaBangouProp =
    Prop("被保険者番号", input, HihokenshaBangouValidator.validate)
  val futanWariProp =
    Prop.radio("負担割", futanWariData, 1, FutanWariValidator.validate)
  val validFromProp = Prop.date("期限開始", None, ValidFromValidator.validateOption)
  val validUptoProp = Prop.validUpto("期限終了", None, ValidUptoValidator.validate)

  val props = (
    hokenshaBangouProp,
    hihokenshaBangouProp,
    futanWariProp,
    validFromProp,
    validUptoProp
  )

  val panel = DispPanel(form = true)
  Prop.labelElements(props).foreach { (key, ele) =>
    panel.add(key, ele)
  }

  val ele = panel.ele

  def validateForEnter(patientId: Int): Either[String, Koukikourei] =
    KoukikoureiValidator
      .validate(
        KoukikoureiIdValidator.validateForEnter,
        PatientIdValidator.validate(patientId),
        hokenshaBangouProp.validator(),
        hihokenshaBangouProp.validator(),
        futanWariProp.validator(),
        validFromProp.validator(),
        validUptoProp.validator()
      )
      .asEither
