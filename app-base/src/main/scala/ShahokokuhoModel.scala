package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.all.{ElementProvider => _, *, given}
import scala.language.implicitConversions
import dev.fujiwara.domq.prop.*
import dev.myclinic.scala.model.{RepProvider => _, *}
import ShahokokuhoValidator.*
import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.Implicits.*
import java.time.LocalDate

object ShahokokuhoProps:
  object shahokokuhoIdProp extends ModelProp[Shahokokuho, Int]("shahokokuho-id", _.shahokokuhoId)
  object hokenshaBangouProp extends ModelProp[Shahokokuho, Int]("保険者番号", _.hokenshaBangou)
  object hihokenshaKigouProp extends ModelProp[Shahokokuho, String]("被保険者記号", _.hihokenshaKigou)
  object hihokenshaBangouProp extends ModelProp[Shahokokuho, String]("被保険者番号", _.hihokenshaBangou)
  object honninProp extends ModelProp[Shahokokuho, Int]("本人・家族", _.honninStore)
  object validFromProp extends ModelProp[Shahokokuho, LocalDate]("期限開始", _.validUpto)
  object validUptoProp extends ModelProp[Shahokokuho, ValidUpto]("期限終了",_.validUpto)
  object koureiProp extends ModelProp[Shahokokuho, Int]("高齢", _.koureiStore)
  object edabanProp extends ModelProp[Shahokokuho, String]("枝番", _.edaban)

class ShahokokuhoInputs(modelOpt: Option[Shahokokuho]):
  import ShahokokuhoProps.*

  object hokenshaBangouInput extends LabelProvider with ElementProvider with DataValidator[HokenshaBangouError.type, Int]:
    val init = InitValue(hokenshaBangouProp, _.toString, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = hokenshaBangouProp.getLabel
    def getElement: HTMLElement = input.getElement
    def validate() = HokenshaBangouValidator.validateInput(input.getValue)

  object hihokenshaKigouInput extends LabelProvider with ElementProvider with DataValidator[HihokenshaKigouError.type, String]:
    val init = InitValue(hihokenshaKigouProp, identity, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = hihokenshaKigouProp.getLabel
    def getElement: HTMLElement = input.getElement
    def validate() = HihokenshaKigouValidator.validate(input.getValue)


  object hihokenshaBangouInput extends LabelProvider with ElementProvider with DataValidator[HihokenshaBangouError.type, String]:
    val init = InitValue(hihokenshaBangouProp, identity, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = hihokenshaBangouProp.getLabel
    def getElement: HTMLElement = input.getElement
    def validate() = HihokenshaBangouValidator.validate(input.getValue)

  object honninInput extends LabelProvider with ElementProvider with DataValidator[HonninError.type, Int]:
    val init = InitValue(honninProp, identity, 0)
    val input = new RadioInput[Int](init.getInitValue(modelOpt), List("本人" -> 1, "家族" -> 0))
    def getLabel: String = honninProp.getLabel
    def getElement: HTMLElement = input.getElement
    def validate() = HonninValidator.validate(input.getValue)



//   object honninInput
//       extends BoundInput[Shahokokuho, Int, HonninError.type, Int](
//         honninProp,
//         modelOpt,
//         _.honninStore,
//         () => 0,
//         HonninValidator.validate
//       ):
//     val inputUI = new RadioInputUI(
//       List("本人" -> 1, "家族" -> 0),
//       resolveInitValue()
//     )

//   object validFromInput
//       extends BoundInput[Shahokokuho, Option[
//         LocalDate
//       ], ValidFromError.type, LocalDate](
//         validFromProp,
//         modelOpt,
//         m => Some(m.validFrom),
//         () => None,
//         ValidFromValidator.validateOption
//       ):
//     val inputUI = new DateOptionInputUI(resolveInitValue())

//   object validUptoInput
//       extends BoundInput[
//         Shahokokuho,
//         ValidUpto,
//         ValidUptoError.type,
//         ValidUpto
//       ](
//         validUptoProp,
//         modelOpt,
//         _.validUpto,
//         () => validUptoSuggest,
//         ValidUptoValidator.validate
//       ):
//     val inputUI = new ValidUptoInputUI(resolveInitValue())

//   object koureiInput
//       extends BoundInput[Shahokokuho, Int, KoureiError.type, Int](
//         koureiProp,
//         modelOpt,
//         _.koureiStore,
//         () => 0,
//         KoureiValidator.validate
//       ):
//     val inputUI = new RadioInputUI(
//       ShahokokuhoValidator.validKoureiValues.map(k =>
//         val label = k match {
//           case 0 => "高齢でない"
//           case i => ZenkakuUtil.toZenkaku(s"${i}割")
//         }
//         (label, k)
//       ),
//       resolveInitValue()
//     )

//   object edabanInput
//       extends BoundInput[Shahokokuho, String, EdabanError.type, String](
//         edabanProp,
//         modelOpt,
//         _.edaban,
//         () => "",
//         EdabanValidator.validate
//       ):
//     val inputUI = new TextInputUI(resolveInitValue())

//   val inputs = (
//     hokenshaBangouInput,
//     hihokenshaKigouInput,
//     hihokenshaBangouInput,
//     honninInput,
//     validFromInput,
//     validUptoInput,
//     koureiInput,
//     edabanInput
//   )

//   private val validUptoSuggest: ValidUpto =
//     validFromInput.validate() match {
//       case Valid(d)   => ValidUpto(Some(d.plusYears(1).minusDays(1)))
//       case Invalid(_) => ValidUpto(None)
//     }

//   val formInputs = (
//     hokenshaBangouInput,
//     LabelElement(
//       "記号・番号",
//       div(
//         hihokenshaKigouInput.getElement,
//         "・",
//         hihokenshaBangouInput.getElement
//       )
//     ),
//     edabanInput,
//     honninInput,
//     validFromInput,
//     validUptoInput,
//     koureiInput
//   )

//   def validatedForEnter(patientId: Int): Either[String, Shahokokuho] =
//     val rs = resultsOf(inputs)
//     ShahokokuhoValidator
//       .validate(
//         ShahokokuhoIdValidator.validateForEnter *:
//           PatientIdValidator.validate(patientId) *: rs
//       )
//       .asEither

//   def validatedForUpdate(): Either[String, Shahokokuho] =
//     val rs = resultsOf(inputs)
//     ShahokokuhoValidator
//       .validate(
//         ShahokokuhoIdValidator.validateOptionForUpdate(
//           modelOpt.map(_.shahokokuhoId)
//         ) *:
//           PatientIdValidator.validateOption(modelOpt.map(_.patientId)) *: rs
//       )
//       .asEither
