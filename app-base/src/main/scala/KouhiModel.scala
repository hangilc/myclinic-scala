package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.all.{ElementProvider => _, *, given}
import scala.language.implicitConversions
import dev.fujiwara.domq.prop.*
import dev.myclinic.scala.model.{RepProvider => _, *}
import KouhiValidator.*
import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.Implicits.*
import java.time.LocalDate
import dev.myclinic.scala.util.ZenkakuUtil
import cats.data.Validated.Valid
import cats.data.Validated.Invalid
import dev.fujiwara.kanjidate.DateUtil

object KouhiProps:
  object futanshaProp extends ModelProp[Kouhi, Int]("負担者番号", _.futansha)
  object jukyuushaProp extends ModelProp[Kouhi, Int]("受給者番号", _.jukyuusha)
  object validFromProp extends ModelProp[Kouhi, LocalDate]("期限開始", _.validFrom)
  object validUptoProp extends ModelProp[Kouhi, ValidUpto]("期限終了", _.validUpto)

  val props = (
    futanshaProp,
    jukyuushaProp,
    validFromProp,
    validUptoProp
  )

class KouhiInputs(modelOpt: Option[Kouhi]):
  import KouhiProps.*

  object futanshaInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[FutanshaError.type, Int]:
    val init = InitValue(futanshaProp, _.toString, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = futanshaProp.getLabel
    def getElement: HTMLElement = input.getElement(cls := "futansha-input")
    def validate() = FutanshaValidator.validateInput(input.getValue)

  object jukyuushaInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[JukyuushaError.type, Int]:
    val init = InitValue(jukyuushaProp, _.toString, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = jukyuushaProp.getLabel
    def getElement: HTMLElement = input.getElement(cls := "jukyuusha-input")
    def validate() = JukyuushaValidator.validateInput(input.getValue)

  object validFromInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[ValidFromError.type, LocalDate]
      with ValueProvider[Option[LocalDate]]
      with OnChangePublisher[Option[LocalDate]]:
    val init = InitValue[Kouhi, Option[LocalDate], LocalDate](
      validFromProp,
      Some(_),
      None
    )
    val input = new DateInput(init.getInitValue(modelOpt))
    def getLabel: String = validFromProp.getLabel
    def getElement: HTMLElement = input.getElement(cls := "valid-from-input")
    def validate() = ValidFromValidator.validateOption(input.getValue)
    def getValue: Option[LocalDate] = input.getValue
    def onChange(handler: Option[LocalDate] => Unit): Unit =
      input.dateInput.onChange(handler)

  object validUptoInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[ValidUptoError.type, ValidUpto]:
    val init = InitValue(validUptoProp, identity, ValidUpto(None))
    import dev.fujiwara.domq.dateinput.DateInput.Suggest
    given Suggest = Suggest(() => validUptoSuggest)
    val input = new ValidUptoInput(init.getInitValue(modelOpt))
    def getLabel: String = validUptoProp.getLabel
    def getElement: HTMLElement = input.getElement(cls := "valid-upto-input")
    def validate() = ValidUptoValidator.validate(input.getValue)

  private def validUptoSuggest: Option[LocalDate] =
    validFromInput.getValue match {
      case Some(d) => Some(d.plusYears(1).minusDays(1))
      case None    => None
    }

  val inputs = (
    futanshaInput,
    jukyuushaInput,
    validFromInput,
    validUptoInput
  )

  def formPanel: HTMLElement =
    ModelInputUtil.elementPanel(inputs)(cls := "kouhi-form")

  def validateForEnter(patientId: Int): Either[String, Kouhi] =
    val rs = ModelInputUtil.resultsOf(inputs)
    KouhiValidator
      .validate(
        KouhiIdValidator.validateForEnter *: rs
          ++ Tuple(PatientIdValidator.validate(patientId))
      )
      .asEither

  def validateForUpdate(): Either[String, Kouhi] =
    val rs = ModelInputUtil.resultsOf(inputs)
    KouhiValidator
      .validate(
        KouhiIdValidator.validateOptionForUpdate(
          modelOpt.map(_.kouhiId)
        ) *: rs ++
          Tuple(PatientIdValidator.validateOption(modelOpt.map(_.patientId)))
      )
      .asEither

object KouhiRepFactory:
  import KouhiProps.*

  class FutanshaRep(modelOpt: Option[Kouhi]) extends ModelPropRep(modelOpt, futanshaProp)
  class JukyuushaRep(modelOpt: Option[Kouhi]) extends ModelPropRep(modelOpt, jukyuushaProp)
  class ValidFromRep(modelOpt: Option[Kouhi]) extends ModelDatePropRep(modelOpt, validFromProp)
  class ValidUptoRep(modelOpt: Option[Kouhi]) extends ModelValidUptoPropRep(modelOpt, validUptoProp)


class KouhiReps(modelOpt: Option[Kouhi]):
  import KouhiRepFactory.*

  val futanshaRep = new FutanshaRep(modelOpt)
  val jukyuushaRep = new JukyuushaRep(modelOpt)
  val validFromRep = new ValidFromRep(modelOpt)
  val validUptoRep = new ValidUptoRep(modelOpt)

  val dispReps = (
    futanshaRep,
    jukyuushaRep,
    validFromRep,
    validUptoRep
  )

  def dispPanel: HTMLElement =
    ModelInputUtil.elementPanel(dispReps)


