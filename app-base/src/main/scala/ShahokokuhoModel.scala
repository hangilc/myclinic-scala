package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.prop.*
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
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
import java.time.LocalDate

object ShahokokuhoProps:
  object hokenshaBangouProp extends ModelProp("保険者番号")
  object hihokenshaKigouProp extends ModelProp("被保険者記号")
  object hihokenshaBangouProp extends ModelProp("被保険者番号")
  object honninProp extends ModelProp("本人・家族")
  object validFromProp extends ModelProp("期限開始")
  object validUptoProp extends ModelProp("期限終了")
  object koureiProp extends ModelProp("高齢")
  object edabanProp extends ModelProp("枝番")

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

class ShahokokuhoInputs(modelOpt: Option[Shahokokuho]) extends BoundInputProcs:
  import ShahokokuhoProps.*

  object hokenshaBangouInput
      extends BoundInput[Shahokokuho, String, HokenshaBangouError.type, Int](
        hokenshaBangouProp,
        modelOpt,
        _.hokenshaBangou.toString,
        () => "",
        HokenshaBangouValidator.validateInput
      ):
    val inputUI = new TextInputUI(resolveInitValue())

  object hihokenshaKigouInput
      extends BoundInput[
        Shahokokuho,
        String,
        HihokenshaKigouError.type,
        String
      ](
        hihokenshaKigouProp,
        modelOpt,
        _.hihokenshaKigou,
        () => "",
        HihokenshaKigouValidator.validate
      ):
    val inputUI = new TextInputUI(resolveInitValue())

  object hihokenshaBangouInput
      extends BoundInput[
        Shahokokuho,
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

  object honninInput
      extends BoundInput[Shahokokuho, Int, HonninError.type, Int](
        honninProp,
        modelOpt,
        _.honninStore,
        () => 0,
        HonninValidator.validate
      ):
    val inputUI = new RadioInputUI(
      List("本人" -> 1, "家族" -> 0),
      resolveInitValue()
    )

  object validFromInput
      extends BoundInput[Shahokokuho, Option[
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
        Shahokokuho,
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

  object koureiInput
      extends BoundInput[Shahokokuho, Int, KoureiError.type, Int](
        koureiProp,
        modelOpt,
        _.koureiStore,
        () => 0,
        KoureiValidator.validate
      ):
    val inputUI = new RadioInputUI(
      ShahokokuhoValidator.validKoureiValues.map(k =>
        val label = k match {
          case 0 => "高齢でない"
          case i => ZenkakuUtil.toZenkaku(s"${i}割")
        }
        (label, k)
      ),
      resolveInitValue()
    )

  object edabanInput
      extends BoundInput[Shahokokuho, String, EdabanError.type, String](
        edabanProp,
        modelOpt,
        _.edaban,
        () => "",
        EdabanValidator.validate
      ):
    val inputUI = new TextInputUI(resolveInitValue())

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

  private val validUptoSuggest: ValidUpto =
    validFromInput.validate() match {
      case Valid(d)   => ValidUpto(Some(d.plusYears(1).minusDays(1)))
      case Invalid(_) => ValidUpto(None)
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
        ShahokokuhoIdValidator.validateOptionForUpdate(
          modelOpt.map(_.shahokokuhoId)
        ) *:
          PatientIdValidator.validateOption(modelOpt.map(_.patientId)) *: rs
      )
      .asEither
