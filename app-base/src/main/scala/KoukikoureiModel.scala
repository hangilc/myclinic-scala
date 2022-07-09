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
  object koukikoureiIdProp extends ModelProp[Koukikourei, Int]("koukikourei-id", _.koukikoureiId)
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

object KoukikoureiRepFactory:
  import KoukikoureiProps.*

  class KoukikoureiIdRep(modelOpt: Option[Koukikourei]) extends ModelPropRep(modelOpt, koukikoureiIdProp)
  class HokenshaBangouRep(modelOpt: Option[Koukikourei]) extends ModelPropRep(modelOpt, hokenshaBangouProp)
  class HihokenshaBangouRep(modelOpt: Option[Koukikourei]) extends ModelPropRep(modelOpt, hihokenshaBangouProp)
  class FutanWariRep(modelOpt: Option[Koukikourei]) extends ModelPropRep(modelOpt, futanWariProp)
  class ValidFromRep(modelOpt: Option[Koukikourei]) extends ModelPropRep(modelOpt, validFromProp)
  class ValidUptoRep(modelOpt: Option[Koukikourei]) extends ModelPropRep(modelOpt, validUptoProp)

class KoukikoureiReps(modelOpt: Option[Koukikourei]):
  import KoukikoureiRepFactory.*

  val koukikoureiIdRep = new KoukikoureiIdRep(modelOpt)
  val hokenshaBangouRep = new HokenshaBangouRep(modelOpt)
  val hihokenshaBangouRep = new HihokenshaBangouRep(modelOpt)
  val futanWariRep = new FutanWariRep(modelOpt)
  val validFromRep = new ValidFromRep(modelOpt)
  val validUptoRep = new ValidUptoRep(modelOpt)

  val dispReps = (
    hokenshaBangouRep,
    hihokenshaBangouRep,
    futanWariRep,
    validFromRep,
    validUptoRep
  )

  def dispPanel: HTMLElement =
    ModelInputUtil.elementPanel(dispReps)




