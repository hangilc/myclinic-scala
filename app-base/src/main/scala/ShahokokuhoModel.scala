package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.all.{ElementProvider => _, *, given}
import scala.language.implicitConversions
import dev.fujiwara.domq.prop.*
import dev.myclinic.scala.model.{RepProvider => _, *}
import ShahokokuhoValidator.*
import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.Implicits.*
import java.time.LocalDate
import dev.myclinic.scala.util.ZenkakuUtil
import cats.data.Validated.Valid
import cats.data.Validated.Invalid
import dev.fujiwara.domq.dateinput.DateOptionInput

object ShahokokuhoProps:
  object shahokokuhoIdProp
      extends ModelProp[Shahokokuho, Int]("shahokokuho-id", _.shahokokuhoId)
  object hokenshaBangouProp
      extends ModelProp[Shahokokuho, Int]("保険者番号", _.hokenshaBangou)
  object hihokenshaKigouProp
      extends ModelProp[Shahokokuho, String]("被保険者記号", _.hihokenshaKigou)
  object hihokenshaBangouProp
      extends ModelProp[Shahokokuho, String]("被保険者番号", _.hihokenshaBangou)
  object honninProp extends ModelProp[Shahokokuho, Int]("本人・家族", _.honninStore)
  object validFromProp
      extends ModelProp[Shahokokuho, LocalDate]("期限開始", _.validFrom)
  object validUptoProp
      extends ModelProp[Shahokokuho, ValidUpto]("期限終了", _.validUpto)
  object koureiProp extends ModelProp[Shahokokuho, Int]("高齢", _.koureiStore)
  object edabanProp extends ModelProp[Shahokokuho, String]("枝番", _.edaban)

class ShahokokuhoInputs(modelOpt: Option[Shahokokuho]):
  import ShahokokuhoProps.*

  object hokenshaBangouInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[HokenshaBangouError.type, Int]:
    val init = InitValue(hokenshaBangouProp, _.toString, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = hokenshaBangouProp.getLabel
    def getElement: HTMLElement =
      input.getElement(cls := "hokensha-bangou-input")
    def validate() = HokenshaBangouValidator.validateInput(input.getValue)

  object hihokenshaKigouInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[HihokenshaKigouError.type, String]:
    val init = InitValue(hihokenshaKigouProp, identity, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = hihokenshaKigouProp.getLabel
    def getElement: HTMLElement =
      input.getElement(cls := "hihokensha-kigou-input")
    def validate() = HihokenshaKigouValidator.validate(input.getValue)

  object hihokenshaBangouInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[HihokenshaBangouError.type, String]:
    val init = InitValue(hihokenshaBangouProp, identity, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = hihokenshaBangouProp.getLabel
    def getElement: HTMLElement =
      input.getElement(cls := "hihokensha-bangou-input")
    def validate() = HihokenshaBangouValidator.validate(input.getValue)

  object honninInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[HonninError.type, Int]:
    val init = InitValue(honninProp, identity, 0)
    val input = new RadioInput[Int](
      init.getInitValue(modelOpt),
      List("本人" -> 1, "家族" -> 0)
    )
    def getLabel: String = honninProp.getLabel
    def getElement: HTMLElement = input.getElement(cls := "honnin-input")
    def validate() = HonninValidator.validate(input.getValue)

  object validFromInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[ValidFromError.type, LocalDate]
      with ValueProvider[Option[LocalDate]]
      with OnChangePublisher[Option[LocalDate]]:
    val init = InitValue[Shahokokuho, Option[LocalDate], LocalDate](
      validFromProp,
      Some(_),
      None
    )
    val input = new DateInput(init.getInitValue(modelOpt))
    def getLabel: String = validFromProp.getLabel
    def getElement: HTMLElement = input.getElement(cls := "valid-from-input")
    def validate() = ValidFromValidator.validateOption(input.getValue)
    def getValue: Option[LocalDate] = input.getValue
    def onChange(handler: Option[LocalDate] => Unit): Unit =
      input.dateInput.onChange(handler)

  object validUptoInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[ValidUptoError.type, ValidUpto]
      with ValueProvider[ValidUpto]
      with OnChangePublisher[ValidUpto]:
    val init = InitValue(validUptoProp, identity, ValidUpto(None))
    import dev.fujiwara.domq.dateinput.DateInput.Suggest
    given Suggest = Suggest(validUptoSuggest)
    val input = new ValidUptoInput(init.getInitValue(modelOpt))
    def getLabel: String = validUptoProp.getLabel
    def getElement: HTMLElement = input.getElement(cls := "valid-upto-input")
    def validate() = ValidUptoValidator.validate(input.getValue)
    def getValue: ValidUpto = input.getValue
    def onChange(handler: ValidUpto => Unit): Unit =
      input.onChange(handler)

  object koureiInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[KoureiError.type, Int]:
    val init = InitValue(koureiProp, identity, 0)
    val input = new RadioInput(init.getInitValue(modelOpt), koureiData):
      override def layout: RadioGroup.Layout[Int] =
        RadioGroup.Layout[Int](g =>
          div(
            displayBlock,
            div(g.getRadioLabel(0).ele),
            div(
              g.radioLabels.filter(l => l.value != 0).map(_.ele)
            )
          )
        )
    def getLabel: String = koureiProp.getLabel
    def getElement: HTMLElement = input.getElement(cls := "kourei-input")
    def validate() = KoureiValidator.validate(input.getValue)

  object edabanInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[EdabanError.type, String]:
    val init = InitValue(edabanProp, identity, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = edabanProp.getLabel
    def getElement: HTMLElement = input.getElement(cls := "edaban-input")
    def validate() = EdabanValidator.validate(input.getValue)

  def koureiData: List[(String, Int)] =
    ShahokokuhoValidator.validKoureiValues.map(k =>
      val label = k match {
        case 0 => "高齢でない"
        case i => ZenkakuUtil.toZenkaku(s"${i}割")
      }
      (label, k)
    )

  val inputs = (
    hokenshaBangouInput,
    hihokenshaKigouInput,
    hihokenshaBangouInput,
    honninInput,
    validFromInput,
    validUptoInput,
    koureiInput,
    edabanInput
  )

  private def validUptoSuggest(): Option[LocalDate] =
    println(("validUptoSuggest", validFromInput.getValue))
    validFromInput.getValue match {
      case Some(d) => Some(d.plusYears(1).minusDays(1))
      case None    => Some(LocalDate.now())
    }

  val formInputs = (
    hokenshaBangouInput,
    LabelElement(
      "記号・番号",
      div(
        hihokenshaKigouInput.getElement,
        "・",
        hihokenshaBangouInput.getElement
      )
    ),
    edabanInput,
    honninInput,
    validFromInput,
    validUptoInput,
    koureiInput
  )

  def formPanel: HTMLElement =
    ModelPropUtil.elementPanel(formInputs)(cls := "shahokokuho-form")

  def validateForEnter(patientId: Int): Either[String, Shahokokuho] =
    val rs = ModelPropUtil.resultsOf(inputs)
    ShahokokuhoValidator
      .validate(
        ShahokokuhoIdValidator.validateForEnter *:
          PatientIdValidator.validate(patientId) *: rs
      )
      .asEither

  def validateForUpdate(): Either[String, Shahokokuho] =
    val rs = ModelPropUtil.resultsOf(inputs)
    ShahokokuhoValidator
      .validate(
        ShahokokuhoIdValidator.validateOptionForUpdate(
          modelOpt.map(_.shahokokuhoId)
        ) *:
          PatientIdValidator.validateOption(modelOpt.map(_.patientId)) *: rs
      )
      .asEither

object ShahokokuhoRepFactory:
  import ShahokokuhoProps.*

  class HokenshaBangouRep(modelOpt: Option[Shahokokuho])
      extends ModelPropRep(modelOpt, hokenshaBangouProp)
  class HihokenshaKigouRep(modelOpt: Option[Shahokokuho])
      extends ModelPropRep(modelOpt, hihokenshaKigouProp)
  class HihokenshaBangouRep(modelOpt: Option[Shahokokuho])
      extends ModelPropRep(modelOpt, hihokenshaBangouProp)
  class EdabanRep(modelOpt: Option[Shahokokuho])
      extends ModelPropRep(modelOpt, edabanProp)
  class HonninRep(modelOpt: Option[Shahokokuho])
      extends ModelPropRep(modelOpt, honninProp, stringify = honninRep)
  class KoureiRep(modelOpt: Option[Shahokokuho])
      extends ModelPropRep(modelOpt, koureiProp, stringify = koureiRep)
  class ValidFromRep(modelOpt: Option[Shahokokuho])
      extends ModelDatePropRep(modelOpt, validFromProp)
  class ValidUptoRep(modelOpt: Option[Shahokokuho])
      extends ModelValidUptoPropRep(modelOpt, validUptoProp)

  def koureiRep(i: Int): String =
    i match {
      case 0 => "高齢でない"
      case i => ZenkakuUtil.toZenkaku(s"${i}割")
    }

  def honninRep(honnin: Int): String =
    honnin match {
      case 0 => "家族"
      case _ => "本人"
    }

class ShahokokuhoReps(modelOpt: Option[Shahokokuho]):
  import ShahokokuhoRepFactory.*

  val hokenshaBangouRep = new HokenshaBangouRep(modelOpt)
  val hihokenshaKigouRep = new HihokenshaKigouRep(modelOpt)
  val hihokenshaBangouRep = new HihokenshaBangouRep(modelOpt)
  val edabanRep = new EdabanRep(modelOpt)
  val honninRep = new HonninRep(modelOpt)
  val validFromRep = new ValidFromRep(modelOpt)
  val validUptoRep = new ValidUptoRep(modelOpt)
  val koureiRep = new KoureiRep(modelOpt)

  val dispReps = (
    hokenshaBangouRep,
    LabelElement(
      "記号・番号",
      div(
        hihokenshaKigouRep.getElement,
        "・",
        hihokenshaBangouRep.getElement
      )
    ),
    edabanRep,
    honninRep,
    validFromRep,
    validUptoRep,
    koureiRep
  )

  val detailReps = (
    hokenshaBangouRep,
    hihokenshaKigouRep,
    hihokenshaBangouRep,
    edabanRep,
    honninRep,
    validFromRep,
    validUptoRep,
    koureiRep
  )

  val dispPanel: HTMLElement =
    ModelPropUtil.elementPanel(dispReps)

  val detailPairs: List[(String, String)] =
    ModelPropUtil.labelRep(detailReps)
