package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.fujiwara.dateinput.DateOptionInput
import dev.fujiwara.domq.DispPanel
import dev.myclinic.scala.web.appbase.PatientValidator
import dev.fujiwara.dateinput.InitNoneConverter
import java.time.LocalDate
import dev.myclinic.scala.web.appbase.validator.ShahokokuhoValidator
import org.scalajs.dom.HTMLElement
import org.scalajs.dom.HTMLInputElement

class KoukikoureiForm(init: Option[Koukikourei]):

  case class Prop(label: String, element: HTMLElement)

  object Prop:
    def apply(label: String, e: HTMLInputElement): Prop =
      new Prop(label, e)

    def radio[T](label: String, data: List[(String, T)], init: T): Prop =
      val radioGroup = RadioGroup(data, initValue = Some(init))
      Prop(label, radioGroup.ele)

    def date(label: String, init: Option[LocalDate]): Prop =
      val dateInput = DateOptionInput(init)
      Prop(label, dateInput.ele)

  val futanWariData = List(
    "１割" -> 1,
    "２割" -> 2,
    "３割" -> 3
  )

  val hokenshaBangouProp = Prop("保険者番号", input)
  val hihokenshaBangouProp = Prop("被保険者番号", input)
  val futanshaWariProp = Prop.radio("負担割", futanWariData, 1)
  val validFromProp = Prop.date("期限開始", None)
  val validUptoProp = Prop.date("期限終了", None)

  val props = List(
    hokenshaBangouProp,
    hihokenshaBangouProp,
    futanshaWariProp,
    validFromProp,
    validUptoProp
  )

  val panel = DispPanel(form = true)
  props.foreach(prop => panel.add(prop.label, prop.element))

  val ele = panel.ele
