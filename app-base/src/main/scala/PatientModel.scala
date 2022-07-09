package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.all.{ElementProvider => _, *, given}
import scala.language.implicitConversions
import dev.fujiwara.domq.prop.*
import dev.myclinic.scala.model.{RepProvider => _, *}
import PatientValidator.*
import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.Implicits.*
import java.time.LocalDate

object PatientProps:
  object patientIdProp extends ModelProp[Patient, Int]("患者番号", _.patientId)
  object lastNameProp extends ModelProp[Patient, String]("姓", _.lastName)
  object firstNameProp extends ModelProp[Patient, String]("名", _.firstName)
  object lastNameYomiProp
      extends ModelProp[Patient, String]("姓（よみ）", _.lastNameYomi)
  object firstNameYomiProp
      extends ModelProp[Patient, String]("名（よみ）", _.firstNameYomi)
  object sexProp extends ModelProp[Patient, Sex]("性別", _.sex)
  object birthdayProp extends ModelProp[Patient, LocalDate]("生年月日", _.birthday)
  object addressProp extends ModelProp[Patient, String]("住所", _.address)
  object phoneProp extends ModelProp[Patient, String]("電話", _.phone)

class PatientInputs(modelOpt: Option[Patient]):
  import PatientProps.*

  object lastNameInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[LastNameError.type, String]:
    val init = InitValue(lastNameProp, identity, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = lastNameProp.getLabel
    def getElement: HTMLElement = input.getElement(cls := "last-name-input")
    def validate() = LastNameValidator.validate(input.getValue)

  object firstNameInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[FirstNameError.type, String]:
    val init = InitValue(firstNameProp, identity, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = firstNameProp.getLabel
    def getElement: HTMLElement = input.getElement(cls := "first-name-input")
    def validate() = FirstNameValidator.validate(input.getValue)

  object lastNameYomiInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[LastNameYomiError.type, String]:
    val init = InitValue(lastNameYomiProp, identity, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = lastNameYomiProp.getLabel
    def getElement: HTMLElement = input.getElement(cls := "last-name-yomi-input")
    def validate() = LastNameYomiValidator.validate(input.getValue)

  object firstNameYomiInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[FirstNameYomiError.type, String]:
    val init = InitValue(firstNameYomiProp, identity, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = firstNameYomiProp.getLabel
    def getElement: HTMLElement = input.getElement(cls := "first-name-yomi-input")
    def validate() = FirstNameYomiValidator.validate(input.getValue)

  object sexInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[SexError.type, Sex]:
    val init = InitValue(sexProp, identity, Sex.Female)
    val input = new RadioInput(
      init.getInitValue(modelOpt),
      List("男" -> Sex.Male, "女" -> Sex.Female)
    )
    def getLabel = sexProp.getLabel
    def getElement = input.getElement(cls := "sex-input")
    def validate() = SexValidator.validate(input.getValue)

  object birthdayInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[BirthdayError.type, LocalDate]:
    val init = InitValue(birthdayProp, Some(_), None)
    val input = new DateInput(init.getInitValue(modelOpt))
    def getLabel = birthdayProp.getLabel
    def getElement = input.getElement(cls := "birthday-input")
    def validate() = BirthdayValidator.validateOption(input.getValue)

  object addressInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[AddressError.type, String]:
    val init = InitValue(addressProp, identity, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = addressProp.getLabel
    def getElement: HTMLElement = input.getElement(cls := "address-input")
    def validate() = AddressValidator.validate(input.getValue)

  object phoneInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[PhoneError.type, String]:
    val init = InitValue(phoneProp, identity, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = phoneProp.getLabel
    def getElement: HTMLElement = input.getElement(cls := "phone-input")
    def validate() = PhoneValidator.validate(input.getValue)

  val formInputsEnter = (
    LabelElement(
      "氏名",
      div(
        lastNameInput.getElement,
        " ",
        firstNameInput.getElement
      )
    ),
    LabelElement(
      "よみ",
      div(
        lastNameYomiInput.getElement,
        " ",
        firstNameYomiInput.getElement
      )
    ),
    birthdayInput,
    sexInput,
    addressInput,
    phoneInput
  )

  val formInputs = 
    modelOpt.fold(formInputsEnter)(m =>
      LabelElement(
        patientIdProp.getLabel,
        span(PatientRepFactory.PatientIdRep(Some(m)).getRep)
      ) *: formInputsEnter
    )

  def formPanel(tuple: Tuple): HTMLElement =
    ModelInputUtil.elementPanel(tuple)(cls := "patient-form")

  def formPanel: HTMLElement =
    formPanel(formInputs)

  val inputs = (
    lastNameInput,
    firstNameInput,
    lastNameYomiInput,
    firstNameYomiInput,
    sexInput,
    birthdayInput,
    addressInput,
    phoneInput
  )

  def validateForEnter(): Either[String, Patient] =
    PatientValidator
      .validate(
        PatientIdValidator.validateForEnter *:
          ModelInputUtil.resultsOf(inputs)
      )
      .asEither

  def validateForUpdate: Either[String, Patient] =
    PatientValidator
      .validate(
        PatientIdValidator.validateOptionForUpdate(modelOpt.map(_.patientId)) *:
          ModelInputUtil.resultsOf(inputs)
      )
      .asEither

object PatientRepFactory:
  import PatientProps.*

  class PatientIdRep(modelOpt: Option[Patient]) extends LabelProvider with RepProvider with RepToSpan:
    val prop = patientIdProp
    val rep = ModelPropRep(modelOpt, patientIdProp)
    def getLabel = prop.getLabel
    def getRep = rep.getRep

  class LastNameRep(modelOpt: Option[Patient]) extends LabelProvider with RepProvider with RepToSpan:
    val prop = lastNameProp
    val rep = ModelPropRep(modelOpt, lastNameProp)
    def getLabel = prop.getLabel
    def getRep = rep.getRep

  class FirstNameRep(modelOpt: Option[Patient]) extends LabelProvider with RepProvider with RepToSpan:
    val prop = firstNameProp
    val rep = ModelPropRep(modelOpt, firstNameProp)
    def getLabel = prop.getLabel
    def getRep = rep.getRep

  class LastNameYomiRep(modelOpt: Option[Patient]) extends LabelProvider with RepProvider with RepToSpan:
    val prop = lastNameYomiProp
    val rep = ModelPropRep(modelOpt, lastNameYomiProp)
    def getLabel = prop.getLabel
    def getRep = rep.getRep

  class FirstNameYomiRep(modelOpt: Option[Patient]) extends LabelProvider with RepProvider with RepToSpan:
    val prop = firstNameYomiProp
    val rep = ModelPropRep(modelOpt, firstNameYomiProp)
    def getLabel = prop.getLabel
    def getRep = rep.getRep

  class SexRep(modelOpt: Option[Patient]) extends LabelProvider with RepProvider with RepToSpan:
    val prop = sexProp
    val rep = ModelPropRep(modelOpt, sexProp, _.rep + "性")
    def getLabel = prop.getLabel
    def getRep = rep.getRep

  class BirthdayRep(modelOpt: Option[Patient]) extends LabelProvider with RepProvider with RepToSpan:
    val prop = birthdayProp
    val rep = ModelDatePropRep(modelOpt, birthdayProp)
    def getLabel = prop.getLabel
    def getRep = rep.getRep

  class AddressRep(modelOpt: Option[Patient]) extends LabelProvider with RepProvider with RepToSpan:
    val prop = addressProp
    val rep = ModelPropRep(modelOpt, addressProp)
    def getLabel = prop.getLabel
    def getRep = rep.getRep

  class PhoneRep(modelOpt: Option[Patient]) extends LabelProvider with RepProvider with RepToSpan:
    val prop = phoneProp
    val rep = ModelPropRep(modelOpt, phoneProp)
    def getLabel = prop.getLabel
    def getRep = rep.getRep


class PatientReps(modelOpt: Option[Patient]):
  import PatientProps.*
  import PatientRepFactory.*

  val patientIdRep = new PatientIdRep(modelOpt)
  val lastNameRep = new LastNameRep(modelOpt)
  val firstNameRep = new FirstNameRep(modelOpt)
  val lastNameYomiRep = new LastNameYomiRep(modelOpt)
  val firstNameYomiRep = new FirstNameYomiRep(modelOpt)
  val sexRep = new SexRep(modelOpt)
  val birthdayRep = new BirthdayRep(modelOpt)
  val addressRep = new AddressRep(modelOpt)
  val phoneRep = new PhoneRep(modelOpt)

  val disps = (
    patientIdRep,
    LabelElement(
      "氏名",
      div(
        lastNameRep.getElement,
        " ",
        firstNameRep.getElement
      )
    ),
    LabelElement(
      "よみ",
      div(
        lastNameYomiRep.getElement,
        " ",
        firstNameYomiRep.getElement
      )
    ),
    birthdayRep,
    sexRep,
    addressRep,
    phoneRep
  )

  def dispPanel: HTMLElement =
    ModelInputUtil.elementPanel(disps)
