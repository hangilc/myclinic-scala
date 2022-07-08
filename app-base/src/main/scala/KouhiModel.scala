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
    def getElement: HTMLElement = input.getElement
    def validate() = FutanshaValidator.validateInput(input.getValue)

  object jukyuushaInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[JukyuushaError.type, Int]:
    val init = InitValue(jukyuushaProp, _.toString, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = jukyuushaProp.getLabel
    def getElement: HTMLElement = input.getElement
    def validate() = JukyuushaValidator.validateInput(input.getValue)

  object validFromInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[ValidFromError.type, LocalDate]:
    val init = InitValue[Kouhi, Option[LocalDate], LocalDate](
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
    futanshaInput,
    jukyuushaInput,
    validFromInput,
    validUptoInput
  )

  def formPanel: HTMLElement =
    ModelInputUtil.elementPanel(inputs)

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

class KouhiReps(modelOpt: Option[Kouhi]):
  import KouhiProps.*

  object futanshaRep extends LabelProvider with RepProvider with RepToSpan:
    val prop = futanshaProp
    val rep = ModelPropRep(modelOpt, futanshaProp)
    def getLabel = prop.getLabel
    def getRep = rep.getRep

  object jukyuushaRep extends LabelProvider with RepProvider with RepToSpan:
    val prop = jukyuushaProp
    val rep = ModelPropRep(modelOpt, jukyuushaProp)
    def getLabel = prop.getLabel
    def getRep = rep.getRep

  object validFromRep extends LabelProvider with RepProvider with RepToSpan:
    val prop = validFromProp
    val rep = ModelPropRep(modelOpt, validFromProp)
    def getLabel = prop.getLabel
    def getRep = rep.getRep

  object validUptoRep extends LabelProvider with RepProvider with RepToSpan:
    val prop = validUptoProp
    val rep = ModelPropRep(modelOpt, validUptoProp)
    def getLabel = prop.getLabel
    def getRep = rep.getRep

  val dispReps = (
    futanshaRep,
    jukyuushaRep,
    validFromRep,
    validUptoRep
  )

  def dispPanel: HTMLElement =
    ModelInputUtil.elementPanel(dispReps)


