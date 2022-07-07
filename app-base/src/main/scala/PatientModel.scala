package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.domq.prop.*
import dev.myclinic.scala.model.*
import PatientValidator.*
import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.Implicits.*
import java.time.LocalDate

object PatientProps:
  object patientIdProp extends ModelProp("患者番号") with DataGetter[Patient, Int]:
    def getFrom(m: Patient): Int = m.patientId

  object lastNameProp extends ModelProp("姓") with DataGetter[Patient, String]:
    def getFrom(m: Patient): String = m.lastName

  object firstNameProp extends ModelProp("名") with DataGetter[Patient, String]:
    def getFrom(m: Patient): String = m.firstName

  object lastNameYomiProp
      extends ModelProp("姓（よみ）")
      with DataGetter[Patient, String]:
    def getFrom(m: Patient): String = m.lastNameYomi

  object firstNameYomiProp
      extends ModelProp("名（よみ）")
      with DataGetter[Patient, String]:
    def getFrom(m: Patient): String = m.firstNameYomi

  object sexProp extends ModelProp("性別") with DataGetter[Patient, Sex]:
    def getFrom(m: Patient): Sex = m.sex

  object birthdayProp
      extends ModelProp("生年月日")
      with DataGetter[Patient, LocalDate]:
    def getFrom(m: Patient): LocalDate = m.birthday

  object addressProp extends ModelProp("住所") with DataGetter[Patient, String]:
    def getFrom(m: Patient): String = m.address

  object phoneProp extends ModelProp("電話") with DataGetter[Patient, String]:
    def getFrom(m: Patient): String = m.phone

class PatientInputs(modelOpt: Option[Patient]):
  import PatientProps.*

  object lastNameInput
      extends LabelElementProvider
      with DataValidator[LastNameError.type, String]:
    val init = InitValue(lastNameProp, identity, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = lastNameProp.getLabel
    def getElement: HTMLElement = input.getElement
    def validate() = LastNameValidator.validate(input.getValue)

  object firstNameInput
      extends LabelElementProvider
      with DataValidator[FirstNameError.type, String]:
    val init = InitValue(firstNameProp, identity, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = firstNameProp.getLabel
    def getElement: HTMLElement = input.getElement
    def validate() = FirstNameValidator.validate(input.getValue)

  object lastNameYomiInput
      extends LabelElementProvider
      with DataValidator[LastNameYomiError.type, String]:
    val init = InitValue(lastNameYomiProp, identity, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = lastNameYomiProp.getLabel
    def getElement: HTMLElement = input.getElement
    def validate() = LastNameYomiValidator.validate(input.getValue)

  object firstNameYomiInput
      extends LabelElementProvider
      with DataValidator[FirstNameYomiError.type, String]:
    val init = InitValue(firstNameYomiProp, identity, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = firstNameYomiProp.getLabel
    def getElement: HTMLElement = input.getElement
    def validate() = FirstNameYomiValidator.validate(input.getValue)

  object sexInput
    extends LabelElementProvider
    with DataValidator[SexError.type, Sex]:
    val init = InitValue(sexProp, identity, Sex.Female)
    val input = new RadioInput(init.getInitValue(modelOpt), List("男" -> Sex.Male, "女" -> Sex.Female))
    def getLabel = sexProp.getLabel
    def getElement = input.getElement
    def validate() = SexValidator.validate(input.getValue)

  object birthdayInput
    extends LabelElementProvider
    with DataValidator[BirthdayError.type, LocalDate]:
    val init = InitValue(birthdayProp, Some(_), None)
    val input = new DateInput(init.getInitValue(modelOpt))
    def getLabel = birthdayProp.getLabel
    def getElement = input.getElement
    def validate() = BirthdayValidator.validateOption(input.getValue)

  object addressInput
      extends LabelElementProvider
      with DataValidator[AddressError.type, String]:
    val init = InitValue(addressProp, identity, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = addressProp.getLabel
    def getElement: HTMLElement = input.getElement
    def validate() = AddressValidator.validate(input.getValue)

  object phoneInput
      extends LabelElementProvider
      with DataValidator[PhoneError.type, String]:
    val init = InitValue(phoneProp, identity, "")
    val input = new StringInput(init.getInitValue(modelOpt))
    def getLabel: String = phoneProp.getLabel
    def getElement: HTMLElement = input.getElement
    def validate() = PhoneValidator.validate(input.getValue)

  val formInputs = (
    LabelElement("氏名", div(
      lastNameInput.getElement, " ", firstNameInput.getElement
    )),
    LabelElement("よみ", div(
      lastNameYomiInput.getElement, " ", firstNameYomiInput.getElement
    )),
    birthdayInput,
    sexInput,
    addressInput,
    phoneInput
  )

  def formPanel(tuple: Tuple): HTMLElement =
    ModelInputUtil.elementPanel(tuple)

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
