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

  case class Prop[T, E <: KoukikoureiError](
      label: String,
      element: HTMLElement,
      validator: () => SectionResult[E, T]
  ):
    def keyElement: (String, HTMLElement) = (label, element)

  object Prop:
    def apply[E](
        label: String,
        e: HTMLInputElement,
        validator: String => SectionResult[E, String]
    ): Prop[String, E] =
      new Prop[String, E](label, e, () => validator(e.value))

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
        validator: Option[LocalDate] => SectionResult[E, Option[LocalDate]]
    ): Prop[Option[LocalDate], E] =
      val dateInput = DateOptionInput(init)
      new Prop[Option[LocalDate], E](label, dateInput.ele, () => validator(dateInput.value))

  val futanWariData = List(
    "１割" -> 1,
    "２割" -> 2,
    "３割" -> 3
  )

  val hokenshaBangouProp = Prop[HokenshaBangouError]("保険者番号", input)
  val hihokenshaBangouProp = Prop("被保険者番号", input)
  val futanshaWariProp = Prop.radio("負担割", futanWariData, 1)
  val validFromProp = Prop.date("期限開始", None)
  val validUptoProp = Prop.date("期限終了", None)

  val props = List(
    hokenshaBangouProp.keyElement,
    hihokenshaBangouProp.keyElement,
    futanshaWariProp.keyElement,
    validFromProp.keyElement,
    validUptoProp.keyElement
  )

  val panel = DispPanel(form = true)
  props.foreach { (key, ele) =>
    panel.add(key, ele)
  }

  val ele = panel.ele
