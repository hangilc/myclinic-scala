package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.fujiwara.dateinput.DateOptionInput
import dev.fujiwara.domq.DispPanel
import dev.myclinic.scala.web.appbase.PatientValidator
import dev.fujiwara.dateinput.InitNoneConverter
import java.time.LocalDate
import dev.myclinic.scala.web.appbase.validator.KoukikoureiValidator
import dev.myclinic.scala.web.appbase.validator.KoukikoureiValidator.*
import org.scalajs.dom.HTMLElement
import org.scalajs.dom.HTMLInputElement

class KoukikoureiForm(init: Option[Koukikourei]):

  case class Prop[T, E](
      label: String,
      element: HTMLElement,
      validator: () => SectionResult[E, T]
  ):
    def keyElement: (String, HTMLElement) = (label, element)

  object Prop:
    def apply[T, E](
        label: String,
        e: HTMLInputElement,
        validator: String => SectionResult[E, T]
    ): Prop[T, E] =
      new Prop[T, E](label, e, () => validator(e.value))

    def radio[T, E](
        label: String,
        data: List[(String, T)],
        init: T,
        validator: T => SectionResult[E, T]
    ): Prop[T, E] =
      val radioGroup = RadioGroup(data, initValue = Some(init))
      new Prop[T, E](label, radioGroup.ele, () => validator(radioGroup.value))

    def date[E](
        label: String,
        init: Option[LocalDate],
        validator: Option[LocalDate] => SectionResult[E, LocalDate]
    ): Prop[LocalDate, E] =
      val dateInput = DateOptionInput(init)
      new Prop[LocalDate, E](
        label,
        dateInput.ele,
        () => validator(dateInput.value)
      )

    def validUpto[E](
        label: String,
        init: Option[LocalDate],
        validator: Option[LocalDate] => SectionResult[E, ValidUpto]
    ): Prop[ValidUpto, E] =
      val dateInput = DateOptionInput(init)
      new Prop[ValidUpto, E](
        label,
        dateInput.ele,
        () => validator(dateInput.value)
      )

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

  val props = List(
    hokenshaBangouProp.keyElement,
    hihokenshaBangouProp.keyElement,
    futanWariProp.keyElement,
    validFromProp.keyElement,
    validUptoProp.keyElement
  )

  val panel = DispPanel(form = true)
  props.foreach { (key, ele) =>
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
