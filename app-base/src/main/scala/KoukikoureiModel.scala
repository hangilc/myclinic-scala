package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.all.{ElementProvider => _, *, given}
import scala.language.implicitConversions
import dev.fujiwara.domq.prop.*
import dev.myclinic.scala.model.{RepProvider => _, *}
import KoukikoureiValidator.*
import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.Implicits.*
import java.time.LocalDate
import dev.myclinic.scala.util.ZenkakuUtil
import cats.data.Validated.Valid
import cats.data.Validated.Invalid
import dev.fujiwara.kanjidate.DateUtil

object KoukikoureiProps:
  object hokenshaBangouProp extends ModelProp[Koukikourei, String]("保険者番号", _.hokenshaBangou)
  object hihokenshaBangouProp extends ModelProp[Koukikourei, String]("被保険者番号", _.hihokenshaBangou)
  object futanWariProp extends ModelProp[Koukikourei, Int]("負担割", _.futanWari)
  object validFromProp extends ModelProp[Koukikourei, LocalDate]("期限開始", _.validFrom)
  object validUptoProp extends ModelProp[Koukikourei, ValidUpto]("期限終了", _.validUpto)

  val props = (
    hokenshaBangouProp,
    hihokenshaBangouProp,
    futanWariProp,
    validFromProp,
    validUptoProp
  )

class KoukikoureiInputs(modelOpt: Option[Koukikourei]):
  import KoukikoureiProps.*

  object hokenshaBangouInput extends LabelProvider with ElementProvider with DataValidator[HokenshaBangouError.type, String]:
    val init = InitValue(hokenshaBangouProp, identity, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = hokenshaBangouProp.getLabel
    def getElement: HTMLElement = input.getElement(cls := "hokensha-input")
    def validate() = HokenshaBangouValidator.validate(input.getValue)
 
  object hihokenshaBangouInput extends LabelProvider with ElementProvider with DataValidator[HihokenshaBangouError.type, String]:
    val init = InitValue(hihokenshaBangouProp, identity, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = hihokenshaBangouProp.getLabel
    def getElement: HTMLElement = input.getElement(cls := "hihokensha-input")
    def validate() = HihokenshaBangouValidator.validate(input.getValue)
 
  object futanWariInput extends LabelProvider with ElementProvider with DataValidator[FutanWariError.type, Int]:
    val init = InitValue(futanWariProp, identity, 1)
    val input = new RadioInput[Int](init.getInitValue(modelOpt), List("１割" -> 1, "２割" -> 2, "３割" -> 3))
    def getLabel: String = futanWariProp.getLabel
    def getElement: HTMLElement = input.getElement(cls := "futan-wari-input")
    def validate() = FutanWariValidator.validate(input.getValue)

  object validFromInput extends LabelProvider with ElementProvider with DataValidator[ValidFromError.type, LocalDate]:
    val init = InitValue[Koukikourei, Option[LocalDate], LocalDate](validFromProp, Some(_), None)
    val input = new DateInput(init.getInitValue(modelOpt))
    def getLabel: String = validFromProp.getLabel
    def getElement: HTMLElement = input.getElement(cls := "valid-from-input")
    def validate() = ValidFromValidator.validateOption(input.getValue)

  object validUptoInput extends LabelProvider with ElementProvider with DataValidator[ValidUptoError.type, ValidUpto]:
    val init = InitValue(validUptoProp, identity, validUptoSuggest)
    val input = new ValidUptoInput(init.getInitValue(modelOpt))
    def getLabel: String = validUptoProp.getLabel
    def getElement: HTMLElement = input.getElement(cls := "valid-upto-input")
    def validate() = ValidUptoValidator.validate(input.getValue)

  private def validUptoSuggest: ValidUpto =
    val anchor = validFromInput.validate() match {
      case Valid(d)   => d
      case Invalid(_) => LocalDate.now()
    }
    ValidUpto(Some(DateUtil.nextDateOf(7, 31, anchor)))

  val inputs = (
    hokenshaBangouInput,
    hihokenshaBangouInput,
    futanWariInput,
    validFromInput,
    validUptoInput
  )

  def formPanel: HTMLElement =
    ModelInputUtil.elementPanel(inputs)(cls := "koukikourei-form")

  def validateForEnter(patientId: Int): Either[String, Koukikourei] =
    val rs = ModelInputUtil.resultsOf(inputs)
    KoukikoureiValidator
      .validate(
        KoukikoureiIdValidator.validateForEnter *:
          PatientIdValidator.validate(patientId) *: rs
      )
      .asEither

  def validateForUpdate(): Either[String, Koukikourei] =
    val rs = ModelInputUtil.resultsOf(inputs)
    KoukikoureiValidator
      .validate(
        KoukikoureiIdValidator.validateOptionForUpdate(
          modelOpt.map(_.koukikoureiId)
        ) *:
          PatientIdValidator.validateOption(modelOpt.map(_.patientId)) *: rs
      )
      .asEither

class KoukikoureiReps(modelOpt: Option[Koukikourei]):
  import KoukikoureiProps.*

  object hokenshaBangouRep extends LabelProvider with RepProvider with RepToSpan:
    val prop = hokenshaBangouProp
    val rep = ModelPropRep(modelOpt, hokenshaBangouProp)
    def getLabel = prop.getLabel
    def getRep = rep.getRep

  object hihokenshaBangouRep extends LabelProvider with RepProvider with RepToSpan:
    val prop = hihokenshaBangouProp
    val rep = ModelPropRep(modelOpt, hihokenshaBangouProp)
    def getLabel = prop.getLabel
    def getRep = rep.getRep

  object futanWariRep extends LabelProvider with RepProvider with RepToSpan:
    val prop = futanWariProp
    val rep = ModelPropRep(modelOpt, futanWariProp)
    def getLabel = prop.getLabel
    def getRep = rep.getRep

  object validFromRep extends LabelProvider with RepProvider with RepToSpan:
    val prop = validFromProp
    val rep = ModelPropRep(modelOpt, validFromProp)
    def getLabel = prop.getLabel
    def getRep = rep.getRep

  object validUptoRep extends LabelProvider with RepProvider with RepToSpan:
    val prop = validUptoProp
    val rep = ModelPropRep(modelOpt, validUptoProp)
    def getLabel = prop.getLabel
    def getRep = rep.getRep

  val dispReps = (
    hokenshaBangouRep,
    hihokenshaBangouRep,
    futanWariRep,
    validFromRep,
    validUptoRep
  )

  def dispPanel: HTMLElement =
    ModelInputUtil.elementPanel(dispReps)




