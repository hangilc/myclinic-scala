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
    def getElement: HTMLElement = input.getElement
    def validate() = HokenshaBangouValidator.validate(input.getValue)
 
  object hihokenshaBangouInput extends LabelProvider with ElementProvider with DataValidator[HihokenshaBangouError.type, String]:
    val init = InitValue(hihokenshaBangouProp, identity, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = hihokenshaBangouProp.getLabel
    def getElement: HTMLElement = input.getElement
    def validate() = HihokenshaBangouValidator.validate(input.getValue)
 
  object futanWariInput extends LabelProvider with ElementProvider with DataValidator[FutanWariError.type, Int]:
    val init = InitValue(futanWariProp, identity, 0)
    val input = new RadioInput[Int](init.getInitValue(modelOpt), List("１割" -> 1, "２割" -> 2, "３割" -> 3))
    def getLabel: String = futanWariProp.getLabel
    def getElement: HTMLElement = input.getElement
    def validate() = FutanWariValidator.validate(input.getValue)

  object validFromInput extends LabelProvider with ElementProvider with DataValidator[ValidFromError.type, LocalDate]:
    val init = InitValue[Koukikourei, Option[LocalDate], LocalDate](validFromProp, Some(_), None)
    val input = new DateInput(init.getInitValue(modelOpt))
    def getLabel: String = validFromProp.getLabel
    def getElement: HTMLElement = input.getElement
    def validate() = ValidFromValidator.validateOption(input.getValue)

  object validUptoInput extends LabelProvider with ElementProvider with DataValidator[ValidUptoError.type, ValidUpto]:
    val init = InitValue(validUptoProp, identity, validUptoSuggest)
    val input = new ValidUptoInput(init.getInitValue(modelOpt))
    def getLabel: String = validUptoProp.getLabel
    def getElement: HTMLElement = input.getElement
    def validate() = ValidUptoValidator.validate(input.getValue)


//   type Create[P] = P match {
//     case KoukikoureiProps.HokenshaBangouProp   => HokenshaBangouInput
//     case KoukikoureiProps.HihokenshaBangouProp => HihokenshaBangouInput
//     case KoukikoureiProps.FutanWariProp        => FutanWariInput
//     case KoukikoureiProps.ValidFromProp        => ValidFromInput
//     case KoukikoureiProps.ValidUptoProp        => ValidUptoInput
//   }

//   def fCreate[P](p: P): Create[P] = p match {
//     case _: KoukikoureiProps.HokenshaBangouProp   => hokenshaBangouInput
//     case _: KoukikoureiProps.HihokenshaBangouProp => hihokenshaBangouInput
//     case _: KoukikoureiProps.FutanWariProp        => futanWariInput
//     case _: KoukikoureiProps.ValidFromProp        => validFromInput
//     case _: KoukikoureiProps.ValidUptoProp        => validUptoInput
//   }

//   def create(props: Tuple): Tuple.Map[props.type, Create] =
//     props.map[Create]([T] => (t: T) => fCreate(t))

//   val inputs = create(KoukikoureiProps.props)

  private val validUptoSuggest: ValidUpto =
    val anchor = validFromInput.validate() match {
      case Valid(d)   => d
      case Invalid(_) => LocalDate.now()
    }
    ValidUpto(Some(DateUtil.nextDateOf(7, 31, anchor)))

//   def update(): Unit =
//     update(inputs, modelOpt)

//   def inputForm: HTMLElement =
//     createForm(KoukikoureiProps.props, inputs)

//   def validatedForEnter(patientId: Int): Either[String, Koukikourei] =
//     val rs = resultsOf(inputs)
//     KoukikoureiValidator
//       .validate(
//         KoukikoureiIdValidator.validateForEnter *:
//           PatientIdValidator.validate(patientId) *: rs
//       )
//       .asEither

//   def validatedForUpdate(): Either[String, Koukikourei] =
//     val rs = resultsOf(inputs)
//     KoukikoureiValidator
//       .validate(
//         KoukikoureiIdValidator.validateOptionForUpdate(
//           modelOpt.map(_.koukikoureiId)
//         ) *:
//           PatientIdValidator.validateOption(modelOpt.map(_.patientId)) *: rs
//       )
//       .asEither
