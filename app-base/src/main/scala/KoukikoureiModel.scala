package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.ModelProp
import dev.fujiwara.domq.ModelInput
import dev.fujiwara.domq.ModelInputs
import dev.fujiwara.domq.ModelInputProcs
import dev.myclinic.scala.model.*
import KoukikoureiValidator.*
import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.DispPanel
import dev.myclinic.scala.util.ZenkakuUtil
import dev.fujiwara.domq.dateinput.DateInput
import cats.data.Validated.Valid
import cats.data.Validated.Invalid.apply
import cats.data.Validated.Invalid
import dev.fujiwara.domq.dateinput.DateOptionInput
import dev.fujiwara.validator.section.Implicits.*
import dev.fujiwara.kanjidate.DateUtil
import java.time.LocalDate

object KoukikoureiProps:
  class HokenshaBangouProp extends ModelProp("保険者番号")
  class HihokenshaBangouProp extends ModelProp("被保険者番号")
  class FutanWariProp extends ModelProp("負担割")
  class ValidFromProp extends ModelProp("期限開始")
  class ValidUptoProp extends ModelProp("期限終了")

  val hokenshaBangouProp: HokenshaBangouProp = new HokenshaBangouProp
  val hihokenshaBangouProp: HihokenshaBangouProp = new HihokenshaBangouProp
  val futanWariProp: FutanWariProp = new FutanWariProp
  val validFromProp: ValidFromProp = new ValidFromProp
  val validUptoProp: ValidUptoProp = new ValidUptoProp

  val props = (
    hokenshaBangouProp,
    hihokenshaBangouProp,
    futanWariProp,
    validFromProp,
    validUptoProp
  )

case class KoukikoureiInputs(modelOpt: Option[Koukikourei])
    extends ModelInput[Koukikourei]
    with ModelInputs[Koukikourei]
    with ModelInputProcs[Koukikourei]:
  class HokenshaBangouInput
      extends ModelTextInput[HokenshaBangouError.type, String](
        _.hokenshaBangou.toString,
        HokenshaBangouValidator.validate
      )
  val hokenshaBangouInput:HokenshaBangouInput = new HokenshaBangouInput

  class HihokenshaBangouInput
      extends ModelTextInput[HihokenshaBangouError.type, String](
        _.hihokenshaBangou,
        HihokenshaBangouValidator.validate
      )
  val hihokenshaBangouInput: HihokenshaBangouInput = new HihokenshaBangouInput

  class FutanWariInput
      extends ModelRadioInput[FutanWariError.type, Int](
        _.futanWari,
        FutanWariValidator.validate,
        List("本人" -> 1, "家族" -> 0),
        0
      )
  val futanWariInput: FutanWariInput = new FutanWariInput

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

  type Create[P] = P match {
    case KoukikoureiProps.HokenshaBangouProp => HokenshaBangouInput
    case KoukikoureiProps.HihokenshaBangouProp => HihokenshaBangouInput
    case KoukikoureiProps.FutanWariProp => FutanWariInput
    case KoukikoureiProps.ValidFromProp => ValidFromInput
    case KoukikoureiProps.ValidUptoProp => ValidUptoInput
  }

  def fCreate[P](p: P): Create[P] = p match {
    case _: KoukikoureiProps.HokenshaBangouProp => hokenshaBangouInput
    case _: KoukikoureiProps.HihokenshaBangouProp => hihokenshaBangouInput
    case _: KoukikoureiProps.FutanWariProp => futanWariInput
    case _: KoukikoureiProps.ValidFromProp => validFromInput
    case _: KoukikoureiProps.ValidUptoProp => validUptoInput
  }

  def create(props: Tuple): Tuple.Map[props.type, Create] =
    props.map[Create]([T] => (t: T) => fCreate(t))

  val inputs = create(KoukikoureiProps.props)

  private val validUptoSuggest: DateInput.Suggest =
    DateInput.Suggest(() => 
      val anchor = validFromInput.validate() match {
        case Valid(d) => d
        case Invalid(_) => LocalDate.now()
      }
      Some(DateUtil.nextDateOf(7, 31, anchor))
    )

  def update(): Unit =
    update(inputs, modelOpt)

  def inputForm: HTMLElement =
    createForm(KoukikoureiProps.props, inputs)

  def validatedForEnter(patientId: Int): Either[String, Koukikourei] =
    val rs = resultsOf(inputs)
    KoukikoureiValidator
      .validate(
        KoukikoureiIdValidator.validateForEnter *:
          PatientIdValidator.validate(patientId) *: rs
      )
      .asEither

  def validatedForUpdate(): Either[String, Koukikourei] =
    val rs = resultsOf(inputs)
    KoukikoureiValidator
      .validate(
        KoukikoureiIdValidator.validateOptionForUpdate(modelOpt.map(_.koukikoureiId)) *:
          PatientIdValidator.validateOption(modelOpt.map(_.patientId)) *: rs
      )
      .asEither

  











