package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.all.{ElementProvider => _, *, given}
import scala.language.implicitConversions
import dev.fujiwara.domq.prop.*
import dev.myclinic.scala.model.{RepProvider => _, *}
import PatientValidator.*
import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.Implicits.*
import java.time.LocalDate
import dev.fujiwara.kanjidate.KanjiDate
import dev.fujiwara.kanjidate.DateUtil

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
    def getElement: HTMLElement =
      input.getElement(cls := "last-name-input", placeholder := "姓")
    def validate() = LastNameValidator.validate(input.getValue)

  object firstNameInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[FirstNameError.type, String]:
    val init = InitValue(firstNameProp, identity, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = firstNameProp.getLabel
    def getElement: HTMLElement =
      input.getElement(cls := "first-name-input", placeholder := "名")
    def validate() = FirstNameValidator.validate(input.getValue)

  object lastNameYomiInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[LastNameYomiError.type, String]:
    val init = InitValue(lastNameYomiProp, identity, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = lastNameYomiProp.getLabel
    def getElement: HTMLElement =
      input.getElement(cls := "last-name-yomi-input", placeholder := "姓（よみ）")
    def validate() = LastNameYomiValidator.validate(input.getValue)

  object firstNameYomiInput
      extends LabelProvider
      with ElementProvider
      with DataValidator[FirstNameYomiError.type, String]:
    val init = InitValue(firstNameYomiProp, identity, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = firstNameYomiProp.getLabel
    def getElement: HTMLElement =
      input.getElement(cls := "first-name-yomi-input", placeholder := "名（よみ）")
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
    ModelPropUtil.elementPanel(tuple)(cls := "patient-form")

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
          ModelPropUtil.resultsOf(inputs)
      )
      .asEither

  def validateForUpdate(): Either[String, Patient] =
    PatientValidator
      .validate(
        PatientIdValidator.validateOptionForUpdate(modelOpt.map(_.patientId)) *:
          ModelPropUtil.resultsOf(inputs)
      )
      .asEither

object PatientRepFactory:
  import PatientProps.*

  class PatientIdRep(modelOpt: Option[Patient])
      extends ModelPropRep(modelOpt, patientIdProp)
  class LastNameRep(modelOpt: Option[Patient])
      extends ModelPropRep(modelOpt, lastNameProp)
  class FirstNameRep(modelOpt: Option[Patient])
      extends ModelPropRep(modelOpt, firstNameProp)
  class LastNameYomiRep(modelOpt: Option[Patient])
      extends ModelPropRep(modelOpt, lastNameYomiProp)
  class FirstNameYomiRep(modelOpt: Option[Patient])
      extends ModelPropRep(modelOpt, firstNameYomiProp)
  class SexRep(modelOpt: Option[Patient])
      extends ModelPropRep(modelOpt, sexProp, stringify = _.rep + "性")
  class BirthdayRep(modelOpt: Option[Patient])
      extends ModelDatePropRep(
        modelOpt,
        birthdayProp,
        stringify = d => {
          val drep = KanjiDate.dateToKanji(d)
          val age = DateUtil.calcAge(d, LocalDate.now())
          s"${drep}（${age}才）"
        }
      )
  class AddressRep(modelOpt: Option[Patient])
      extends ModelPropRep(modelOpt, addressProp)
  class PhoneRep(modelOpt: Option[Patient])
      extends ModelPropRep(modelOpt, phoneProp)

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
    ModelPropUtil.elementPanel(disps)
