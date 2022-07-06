package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.ModelProp
import dev.fujiwara.domq.ModelInput
import dev.fujiwara.domq.ModelInputs
import dev.fujiwara.domq.ModelInputProcs
import dev.myclinic.scala.model.*
import KouhiValidator.*
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

object KouhiProps:
  class FutanshaProp extends ModelProp("負担者番号")
  class JukyuushaProp extends ModelProp("受給者番号")
  class ValidFromProp extends ModelProp("期限開始")
  class ValidUptoProp extends ModelProp("期限終了")

  val futanshaProp: FutanshaProp = new FutanshaProp
  val jukyuushaProp: JukyuushaProp = new JukyuushaProp
  val validFromProp: ValidFromProp = new ValidFromProp
  val validUptoProp: ValidUptoProp = new ValidUptoProp

  val props = (
    futanshaProp,
    jukyuushaProp,
    validFromProp,
    validUptoProp
  )

case class KouhiInputs(modelOpt: Option[Kouhi])
    extends ModelInput[Kouhi]
    with ModelInputs[Kouhi]
    with ModelInputProcs[Kouhi]:
  class FutanshaInput
      extends ModelTextInput[FutanshaError.type, Int](
        _.futansha.toString,
        FutanshaValidator.validateInput
      )
  val futanshaInput:FutanshaInput = new FutanshaInput

  class JukyuushaInput
      extends ModelTextInput[JukyuushaError.type, Int](
        _.jukyuusha.toString,
        JukyuushaValidator.validateInput
      )
  val jukyuushaInput: JukyuushaInput = new JukyuushaInput

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
    case KouhiProps.FutanshaProp => FutanshaInput
    case KouhiProps.JukyuushaProp => JukyuushaInput
    case KouhiProps.ValidFromProp => ValidFromInput
    case KouhiProps.ValidUptoProp => ValidUptoInput
  }

  def fCreate[P](p: P): Create[P] = p match {
    case _: KouhiProps.FutanshaProp => futanshaInput
    case _: KouhiProps.JukyuushaProp => jukyuushaInput
    case _: KouhiProps.ValidFromProp => validFromInput
    case _: KouhiProps.ValidUptoProp => validUptoInput
  }

  def create(props: Tuple): Tuple.Map[props.type, Create] =
    props.map[Create]([T] => (t: T) => fCreate(t))

  val inputs = create(KouhiProps.props)

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
    createForm(KouhiProps.props, inputs)

  def validatedForEnter(patientId: Int): Either[String, Kouhi] =
    val rs = resultsOf(inputs)
    KouhiValidator
      .validate(
        KouhiIdValidator.validateForEnter *: rs ++
          Tuple(PatientIdValidator.validate(patientId))
      )
      .asEither

  def validatedForUpdate(): Either[String, Kouhi] =
    val rs = resultsOf(inputs)
    KouhiValidator
      .validate(
        KouhiIdValidator.validateOptionForUpdate(modelOpt.map(_.kouhiId)) *: rs ++
          Tuple(PatientIdValidator.validateOption(modelOpt.map(_.patientId)))
      )
      .asEither

  











