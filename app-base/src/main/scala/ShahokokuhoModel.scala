package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.ModelProp
import dev.fujiwara.domq.ModelInput
import dev.fujiwara.domq.ModelInputs
import dev.fujiwara.domq.ModelInputProcs
import dev.myclinic.scala.model.*
import ShahokokuhoValidator.*
import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.DispPanel
import dev.myclinic.scala.util.ZenkakuUtil
import dev.fujiwara.domq.dateinput.DateInput
import cats.data.Validated.Valid
import cats.data.Validated.Invalid.apply
import cats.data.Validated.Invalid
import dev.fujiwara.domq.dateinput.DateOptionInput
import dev.fujiwara.validator.section.Implicits.*

object ShahokokuhoProps:
  class HokenshaBangouProp extends ModelProp("保険者番号")
  class HihokenshaKigouProp extends ModelProp("被保険者記号")
  class HihokenshaBangouProp extends ModelProp("被保険者番号")
  class HonninProp extends ModelProp("本人・家族")
  class ValidFromProp extends ModelProp("期限開始")
  class ValidUptoProp extends ModelProp("期限終了")
  class KoureiProp extends ModelProp("高齢")
  class EdabanProp extends ModelProp("枝番")

  val hokenshaBangouProp: HokenshaBangouProp = new HokenshaBangouProp
  val hihokenshaKigouProp: HihokenshaKigouProp = new HihokenshaKigouProp
  val hihokenshaBangouProp: HihokenshaBangouProp = new HihokenshaBangouProp
  val honninProp: HonninProp = new HonninProp
  val validFromProp: ValidFromProp = new ValidFromProp
  val validUptoProp: ValidUptoProp = new ValidUptoProp
  val koureiProp: KoureiProp = new KoureiProp
  val edabanProp: EdabanProp = new EdabanProp

  val props = (
    hokenshaBangouProp,
    hihokenshaKigouProp,
    hihokenshaBangouProp,
    honninProp,
    validFromProp,
    validUptoProp,
    koureiProp,
    edabanProp
  )

case class ShahokokuhoInputs(modelOpt: Option[Shahokokuho])
    extends ModelInput[Shahokokuho]
    with ModelInputs[Shahokokuho]
    with ModelInputProcs[Shahokokuho]:
  class HokenshaBangouInput
      extends ModelTextInput[HokenshaBangouError.type, Int](
        _.hokenshaBangou.toString,
        HokenshaBangouValidator.validateInput
      )
  val hokenshaBangouInput:HokenshaBangouInput = new HokenshaBangouInput

  class HihokenshaKigouInput
      extends ModelTextInput[HihokenshaKigouError.type, String](
        _.hihokenshaKigou,
        HihokenshaKigouValidator.validate
      )
  val hihokenshaKigouInput: HihokenshaKigouInput = new HihokenshaKigouInput

  class HihokenshaBangouInput
      extends ModelTextInput[HihokenshaBangouError.type, String](
        _.hihokenshaBangou,
        HihokenshaBangouValidator.validate
      )
  val hihokenshaBangouInput: HihokenshaBangouInput = new HihokenshaBangouInput

  class HonninInput
      extends ModelRadioInput[HonninError.type, Int](
        _.honninStore,
        HonninValidator.validate,
        List("本人" -> 1, "家族" -> 0),
        0
      )
  val honninInput: HonninInput = new HonninInput

  class ValidFromInput
      extends ModelDateInput[ValidFromError.type](
        _.validFrom,
        ValidFromValidator.validateOption,
        None
      )
  val validFromInput: ValidFromInput = new ValidFromInput

  class ValidUptoInput
      extends ModelValidUptoInput[ValidUptoError.type](
        _.validUpto,
        ValidUptoValidator.validate,
        None
      )(using validUptoSuggest)
  val validUptoInput: ValidUptoInput = new ValidUptoInput

  class KoureiInput
      extends ModelRadioInput[KoureiError.type, Int](
        _.koureiStore,
        KoureiValidator.validate,
        ShahokokuhoValidator.validKoureiValues.map(k => 
          val label = k match {
            case 0 => "高齢でない"
            case i => ZenkakuUtil.toZenkaku(s"${i}割") 
          }
          (label, k)
        ),
        0
      )
  val koureiInput: KoureiInput = new KoureiInput

  class EdabanInput
      extends ModelTextInput[EdabanError.type, String](
        _.edaban,
        EdabanValidator.validate
      )
  val edabanInput: EdabanInput = new EdabanInput

  type Create[P] = P match {
    case ShahokokuhoProps.HokenshaBangouProp => HokenshaBangouInput
    case ShahokokuhoProps.HihokenshaKigouProp => HihokenshaKigouInput
    case ShahokokuhoProps.HihokenshaBangouProp => HihokenshaBangouInput
    case ShahokokuhoProps.HonninProp => HonninInput
    case ShahokokuhoProps.ValidFromProp => ValidFromInput
    case ShahokokuhoProps.ValidUptoProp => ValidUptoInput
    case ShahokokuhoProps.KoureiProp => KoureiInput
    case ShahokokuhoProps.EdabanProp => EdabanInput  
  }

  def fCreate[P](p: P): Create[P] = p match {
    case _: ShahokokuhoProps.HokenshaBangouProp => hokenshaBangouInput
    case _: ShahokokuhoProps.HihokenshaKigouProp => hihokenshaKigouInput
    case _: ShahokokuhoProps.HihokenshaBangouProp => hihokenshaBangouInput
    case _: ShahokokuhoProps.HonninProp => honninInput
    case _: ShahokokuhoProps.ValidFromProp => validFromInput
    case _: ShahokokuhoProps.ValidUptoProp => validUptoInput
    case _: ShahokokuhoProps.KoureiProp => koureiInput
    case _: ShahokokuhoProps.EdabanProp => edabanInput
  }

  def create(props: Tuple): Tuple.Map[props.type, Create] =
    props.map[Create]([T] => (t: T) => fCreate(t))

  val inputs = create(ShahokokuhoProps.props)

  private val validUptoSuggest: DateInput.Suggest =
    DateInput.Suggest(() => 
      validFromInput.validate() match {
        case Valid(d) => Some(d.plusYears(1).minusDays(1))
        case Invalid(_) => None
      }
    )

  def update(): Unit =
    update(inputs, modelOpt)

  def inputForm: HTMLElement =
    createForm(ShahokokuhoProps.props, inputs)

  def validatedForEnter(patientId: Int): Either[String, Shahokokuho] =
    val rs = resultsOf(inputs)
    ShahokokuhoValidator
      .validate(
        ShahokokuhoIdValidator.validateForEnter *:
          PatientIdValidator.validate(patientId) *: rs
      )
      .asEither

  def validatedForUpdate(): Either[String, Shahokokuho] =
    val rs = resultsOf(inputs)
    ShahokokuhoValidator
      .validate(
        ShahokokuhoIdValidator.validateOptionForUpdate(modelOpt.map(_.shahokokuhoId)) *:
          PatientIdValidator.validateOption(modelOpt.map(_.patientId)) *: rs
      )
      .asEither

  











