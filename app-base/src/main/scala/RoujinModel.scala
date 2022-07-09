package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.all.{ElementProvider => _, *, given}
import scala.language.implicitConversions
import dev.fujiwara.domq.prop.*
import dev.myclinic.scala.model.{RepProvider => _, *}
import RoujinValidator.*
import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.Implicits.*
import java.time.LocalDate
import dev.myclinic.scala.util.ZenkakuUtil
import cats.data.Validated.Valid
import cats.data.Validated.Invalid
import dev.fujiwara.kanjidate.DateUtil

object RoujinProps:
  object shichousonProp extends ModelProp[Roujin, Int]("市町村番号", _.shichouson)
  object jukyuushaProp extends ModelProp[Roujin, Int]("受給者番号", _.jukyuusha)
  object futanWariProp extends ModelProp[Roujin, Int]("負担割", _.futanWari)
  object validFromProp extends ModelProp[Roujin, LocalDate]("期限開始", _.validFrom)
  object validUptoProp extends ModelProp[Roujin, ValidUpto]("期限終了", _.validUpto)

  val props = (
    shichousonProp,
    jukyuushaProp,
    futanWariProp,
    validFromProp,
    validUptoProp
  )

class RoujinInputs(modelOpt: Option[Roujin]):
  import RoujinProps.*

  object shichousonInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[ShichousonError.type, Int]:
    val init = InitValue(shichousonProp, _.toString, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = shichousonProp.getLabel
    def getElement: HTMLElement = input.getElement
    def validate() = ShichousonValidator.validateInput(input.getValue)

  object jukyuushaInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[JukyuushaError.type, Int]:
    val init = InitValue(jukyuushaProp, _.toString, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = jukyuushaProp.getLabel
    def getElement: HTMLElement = input.getElement
    def validate() = JukyuushaValidator.validateInput(input.getValue)

  object futanWariInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[FutanWariError.type, Int]:
    val init = InitValue(futanWariProp, identity, 0)
    val input = new RadioInput[Int](
      init.getInitValue(modelOpt),
      List("１割" -> 1, "２割" -> 2, "３割" -> 3)
    )
    def getLabel: String = futanWariProp.getLabel
    def getElement: HTMLElement = input.getElement
    def validate() = FutanWariValidator.validate(input.getValue)

  object validFromInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[ValidFromError.type, LocalDate]:
    val init = InitValue[Roujin, Option[LocalDate], LocalDate](
      validFromProp,
      Some(_),
      None
    )
    val input = new DateInput(init.getInitValue(modelOpt))
    def getLabel: String = validFromProp.getLabel
    def getElement: HTMLElement = input.getElement
    def validate() = ValidFromValidator.validateOption(input.getValue)

  object validUptoInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[ValidUptoError.type, ValidUpto]:
    val init = InitValue(validUptoProp, identity, validUptoSuggest)
    val input = new ValidUptoInput(init.getInitValue(modelOpt))
    def getLabel: String = validUptoProp.getLabel
    def getElement: HTMLElement = input.getElement
    def validate() = ValidUptoValidator.validate(input.getValue)

  private val validUptoSuggest: ValidUpto =
    val anchor = validFromInput.validate() match {
      case Valid(d)   => d
      case Invalid(_) => LocalDate.now()
    }
    ValidUpto(Some(DateUtil.nextDateOf(7, 31, anchor)))

  val inputs = (
    shichousonInput,
    jukyuushaInput,
    futanWariInput,
    validFromInput,
    validUptoInput
  )

  def formPanel: HTMLElement =
    ModelInputUtil.elementPanel(inputs)

  def validatedForEnter(patientId: Int): Either[String, Roujin] =
    val rs = ModelInputUtil.resultsOf(inputs)
    RoujinValidator
      .validate(
        RoujinIdValidator.validateForEnter *:
          PatientIdValidator.validate(patientId) *: rs
      )
      .asEither

  def validatedForUpdate(): Either[String, Roujin] =
    val rs = ModelInputUtil.resultsOf(inputs)
    RoujinValidator
      .validate(
        RoujinIdValidator.validateOptionForUpdate(
          modelOpt.map(_.roujinId)
        ) *:
          PatientIdValidator.validateOption(modelOpt.map(_.patientId)) *: rs
      )
      .asEither

object RoujinRepFactory:
  import RoujinProps.*

  class ShichousonRep(modelOpt: Option[Roujin]) extends ModelPropRep(modelOpt, shichousonProp)
  class JukyuushaRep(modelOpt: Option[Roujin]) extends ModelPropRep(modelOpt, jukyuushaProp)
  class FutanWariRep(modelOpt: Option[Roujin]) extends ModelPropRep(modelOpt, futanWariProp)
  class ValidFromRep(modelOpt: Option[Roujin]) extends ModelPropRep(modelOpt, validFromProp)
  class ValidUptoRep(modelOpt: Option[Roujin]) extends ModelPropRep(modelOpt, validUptoProp)


class RoujinReps(modelOpt: Option[Roujin]):
  import RoujinRepFactory.*

  val shichousonRep = new  ShichousonRep(modelOpt)
  val jukyuushaRep = new  JukyuushaRep(modelOpt)
  val futanWariRep = new  FutanWariRep(modelOpt)
  val validFromRep = new  ValidFromRep(modelOpt)
  val validUptoRep = new  ValidUptoRep(modelOpt)

  val dispReps = (
    shichousonRep,
    jukyuushaRep,
    futanWariRep,
    validFromRep,
    validUptoRep
  )

  def dispPanel: HTMLElement =
    ModelInputUtil.elementPanel(dispReps)








