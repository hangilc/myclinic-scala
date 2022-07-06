package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.ModelProp
import dev.fujiwara.domq.ModelInput
import dev.fujiwara.domq.ModelInputs
import dev.fujiwara.domq.ModelInputProcs
import dev.myclinic.scala.model.*
import RoujinValidator.*
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

object RoujinProps:
  object shichousonProp extends ModelProp("市町村番号")
  object jukyuushaProp extends ModelProp("受給者番号")
  object futanWariProp extends ModelProp("負担割")
  object validFromProp extends ModelProp("期限開始")
  object validUptoProp extends ModelProp("期限終了")

  val props = List(
    shichousonProp,
    jukyuushaProp,
    futanWariProp,
    validFromProp,
    validUptoProp
  )

case class RoujinInputs(modelOpt: Option[Roujin])
    extends ModelInput[Roujin]
    with ModelInputs[Roujin]
    with ModelInputProcs[Roujin]:
  class ShichousonInput
      extends ModelTextInput[ShichousonError.type, Int](
        _.shichouson.toString,
        ShichousonValidator.validateInput
      )
  val shichousonInput:ShichousonInput = new ShichousonInput

  class JukyuushaInput
      extends ModelTextInput[JukyuushaError.type, Int](
        _.jukyuusha.toString,
        JukyuushaValidator.validateInput
      )
  val jukyuushaInput: JukyuushaInput = new JukyuushaInput

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
    case RoujinProps.ShichousonProp => ShichousonInput
    case RoujinProps.JukyuushaProp => JukyuushaInput
    case RoujinProps.FutanWariProp => FutanWariInput
    case RoujinProps.ValidFromProp => ValidFromInput
    case RoujinProps.ValidUptoProp => ValidUptoInput
  }

  def fCreate[P](p: P): Create[P] = p match {
    case _: RoujinProps.ShichousonProp => shichousonInput
    case _: RoujinProps.JukyuushaProp => jukyuushaInput
    case _: RoujinProps.FutanWariProp => futanWariInput
    case _: RoujinProps.ValidFromProp => validFromInput
    case _: RoujinProps.ValidUptoProp => validUptoInput
  }

  def create(props: Tuple): Tuple.Map[props.type, Create] =
    props.map[Create]([T] => (t: T) => fCreate(t))

  val inputs = create(RoujinProps.props)

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
    createForm(RoujinProps.props, inputs)

  def validatedForEnter(patientId: Int): Either[String, Roujin] =
    val rs = resultsOf(inputs)
    RoujinValidator
      .validate(
        RoujinIdValidator.validateForEnter *:
          PatientIdValidator.validate(patientId) *: rs
      )
      .asEither

  def validatedForUpdate(): Either[String, Roujin] =
    val rs = resultsOf(inputs)
    RoujinValidator
      .validate(
        RoujinIdValidator.validateOptionForUpdate(modelOpt.map(_.roujinId)) *:
          PatientIdValidator.validateOption(modelOpt.map(_.patientId)) *: rs
      )
      .asEither

  











