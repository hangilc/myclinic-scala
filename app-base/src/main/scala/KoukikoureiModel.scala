package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.prop.*
import dev.fujiwara.domq.all.{*, given}
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
  object hokenshaBangouProp extends ModelProp("保険者番号")
  object hihokenshaBangouProp extends ModelProp("被保険者番号")
  object futanWariProp extends ModelProp("負担割")
  object validFromProp extends ModelProp("期限開始")
  object validUptoProp extends ModelProp("期限終了")

  val props = (
    hokenshaBangouProp,
    hihokenshaBangouProp,
    futanWariProp,
    validFromProp,
    validUptoProp
  )

class KoukikoureiInputs(modelOpt: Option[Koukikourei])
    extends BoundInputProcs
    with ModelUtil:
  import KoukikoureiProps.*

  object hokenshaBangouInput
      extends BoundInput[Koukikourei, String, HokenshaBangouError.type, String](
        hokenshaBangouProp,
        modelOpt,
        _.hokenshaBangou,
        () => "",
        HokenshaBangouValidator.validate
      ):
    val inputUI = new TextInputUI(resolveInitValue())

  object hihokenshaBangouInput
      extends BoundInput[
        Koukikourei,
        String,
        HihokenshaBangouError.type,
        String
      ](
        hihokenshaBangouProp,
        modelOpt,
        _.hihokenshaBangou,
        () => "",
        HihokenshaBangouValidator.validate
      ):
    val inputUI = new TextInputUI(resolveInitValue())

  object futanWariInput
      extends BoundInput[Koukikourei, Int, FutanWariError.type, Int](
        futanWariProp,
        modelOpt,
        _.futanWari,
        () => 1,
        FutanWariValidator.validate
      ):
    val inputUI = new RadioInputUI(
      List("１割" -> 1, "２割" -> 2, "３割" -> 3),
      resolveInitValue()
    )

  object validFromInput
      extends BoundInput[Koukikourei, Option[
        LocalDate
      ], ValidFromError.type, LocalDate](
        validFromProp,
        modelOpt,
        m => Some(m.validFrom),
        () => None,
        ValidFromValidator.validateOption
      ):
    val inputUI = new DateOptionInputUI(resolveInitValue())

  object validUptoInput
      extends BoundInput[
        Koukikourei,
        ValidUpto,
        ValidUptoError.type,
        ValidUpto
      ](
        validUptoProp,
        modelOpt,
        _.validUpto,
        () => validUptoSuggest,
        ValidUptoValidator.validate
      ):
    val inputUI = new ValidUptoInputUI(resolveInitValue())

  type Create[P] = P match {
    case KoukikoureiProps.HokenshaBangouProp   => HokenshaBangouInput
    case KoukikoureiProps.HihokenshaBangouProp => HihokenshaBangouInput
    case KoukikoureiProps.FutanWariProp        => FutanWariInput
    case KoukikoureiProps.ValidFromProp        => ValidFromInput
    case KoukikoureiProps.ValidUptoProp        => ValidUptoInput
  }

  def fCreate[P](p: P): Create[P] = p match {
    case _: KoukikoureiProps.HokenshaBangouProp   => hokenshaBangouInput
    case _: KoukikoureiProps.HihokenshaBangouProp => hihokenshaBangouInput
    case _: KoukikoureiProps.FutanWariProp        => futanWariInput
    case _: KoukikoureiProps.ValidFromProp        => validFromInput
    case _: KoukikoureiProps.ValidUptoProp        => validUptoInput
  }

  def create(props: Tuple): Tuple.Map[props.type, Create] =
    props.map[Create]([T] => (t: T) => fCreate(t))

  val inputs = create(KoukikoureiProps.props)

  private val validUptoSuggest: ValidUpto =
    val anchor = validFromInput.validate() match {
      case Valid(d)   => d
      case Invalid(_) => LocalDate.now()
    }
    ValidUpto(Some(DateUtil.nextDateOf(7, 31, anchor)))

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
        KoukikoureiIdValidator.validateOptionForUpdate(
          modelOpt.map(_.koukikoureiId)
        ) *:
          PatientIdValidator.validateOption(modelOpt.map(_.patientId)) *: rs
      )
      .asEither
