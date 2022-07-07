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
  object patientIdProp extends ModelProp("患者番号")
  object lastNameProp extends ModelProp("姓")
  object firstNameProp extends ModelProp("名")
  object lastNameYomiProp extends ModelProp("姓（よみ）")
  object firstNameYomiProp extends ModelProp("名（よみ）")
  object sexProp extends ModelProp("性別")
  object birthdayProp extends ModelProp("生年月日")
  object addressProp extends ModelProp("住所")
  object phoneProp extends ModelProp("電話")

class PatientInputs(modelOpt: Option[Patient]) extends BoundInputProcs:
  import PatientProps.*

  object lastNameInput
      extends BoundInput[Patient, String, LastNameError.type, String](
        lastNameProp,
        modelOpt,
        _.lastName,
        () => "",
        LastNameValidator.validate
      ):
    val inputUI = new TextInputUI(resolveInitValue())

  object firstNameInput
      extends BoundInput[Patient, String, FirstNameError.type, String](
        firstNameProp,
        modelOpt,
        _.firstName,
        () => "",
        FirstNameValidator.validate
      ):
    val inputUI = new TextInputUI(resolveInitValue())

  object lastNameYomiInput
      extends BoundInput[Patient, String, LastNameYomiError.type, String](
        lastNameYomiProp,
        modelOpt,
        _.lastNameYomi,
        () => "",
        LastNameYomiValidator.validate
      ):
    val inputUI = new TextInputUI(resolveInitValue())

  object firstNameYomiInput
      extends BoundInput[Patient, String, FirstNameYomiError.type, String](
        firstNameYomiProp,
        modelOpt,
        _.firstNameYomi,
        () => "",
        FirstNameYomiValidator.validate
      ):
    val inputUI = new TextInputUI(resolveInitValue())

  object sexInput
      extends BoundInput[Patient, Sex, SexError.type, Sex](
        sexProp,
        modelOpt,
        _.sex,
        () => Sex.Female,
        SexValidator.validate
      ):
    val inputUI = new RadioInputUI(
      List("男" -> Sex.Male, "女" -> Sex.Female),
      resolveInitValue()
    )

  object birthdayInput
      extends BoundInput[Patient, Option[LocalDate], BirthdayError.type, LocalDate](
        birthdayProp,
        modelOpt,
        patient => Some(patient.birthday),
        () => None,
        BirthdayValidator.validateOption
      ):
    val inputUI = new DateOptionInputUI(resolveInitValue())

  object addressInput
      extends BoundInput[Patient, String, AddressError.type, String](
        addressProp,
        modelOpt,
        _.address,
        () => "",
        AddressValidator.validate
      ):
    val inputUI = new TextInputUI(resolveInitValue())

  object phoneInput
      extends BoundInput[Patient, String, PhoneError.type, String](
        phoneProp,
        modelOpt,
        _.phone,
        () => "",
        PhoneValidator.validate
      ):
    val inputUI = new TextInputUI(resolveInitValue())

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

  val formInputs = (
    LabelElement("氏名", div(
      lastNameInput.getElement, " ", firstNameInput.getElement
    )),
    LabelElement("よみ", div(
      lastNameYomiInput.getElement, " ", firstNameYomiInput.getElement
    )),
    sexInput,
    birthdayInput,
    addressInput,
    phoneInput
  )

  def formPanel(inputs: Tuple): HTMLElement =
    formPanel(inputs)

  def formPanel: HTMLElement = formPanel(formInputs)

  def validateForEnter(): Either[String, Patient] =
    PatientValidator
      .validate(
        PatientIdValidator.validateForEnter *:
          resultsOf(inputs)
      )
      .asEither

  def validateForUpdate: Either[String, Patient] =
    PatientValidator
      .validate(
        PatientIdValidator.validateOptionForUpdate(modelOpt.map(_.patientId)) *:
          resultsOf(inputs)
      )
      .asEither

// object PatientReps
//     extends ModelRep[Patient]
//     with ModelReps[Patient]
//     with ModelRepOps[Patient]:
//   object patientIdRep extends ModelSimpleRep[Int](_.patientId)
//   object lastNameRep extends ModelSimpleRep[String](_.lastName)
//   object firstNameRep extends ModelSimpleRep[String](_.firstName)
//   object lastNameYomiRep extends ModelSimpleRep[String](_.lastNameYomi)
//   object firstNameYomiRep extends ModelSimpleRep[String](_.firstNameYomi)
//   object sexRep extends ModelConvertRep[Sex](_.sex, _.rep + "性")
//   object birthdayRep extends ModelDateRep(_.birthday)
//   object addressRep extends ModelSimpleRep[String](_.address)
//   object phoneRep extends ModelSimpleRep[String](_.phone)

//   type Create[P] = P match {
//     case PatientProps.patientIdProp.type     => patientIdRep.type
//     case PatientProps.lastNameProp.type      => lastNameRep.type
//     case PatientProps.firstNameProp.type     => firstNameRep.type
//     case PatientProps.lastNameYomiProp.type  => lastNameYomiRep.type
//     case PatientProps.firstNameYomiProp.type => firstNameYomiRep.type
//     case PatientProps.sexProp.type           => sexRep.type
//     case PatientProps.birthdayProp.type      => birthdayRep.type
//     case PatientProps.addressProp.type       => addressRep.type
//     case PatientProps.phoneProp.type         => phoneRep.type
//   }

//   def fCreate[P](p: P): Create[P] = p match {
//     case _: PatientProps.patientIdProp.type     => patientIdRep
//     case _: PatientProps.lastNameProp.type      => lastNameRep
//     case _: PatientProps.firstNameProp.type     => firstNameRep
//     case _: PatientProps.lastNameYomiProp.type  => lastNameYomiRep
//     case _: PatientProps.firstNameYomiProp.type => firstNameYomiRep
//     case _: PatientProps.sexProp.type           => sexRep
//     case _: PatientProps.birthdayProp.type      => birthdayRep
//     case _: PatientProps.addressProp.type       => addressRep
//     case _: PatientProps.phoneProp.type         => phoneRep
//   }

//   def create(props: Tuple): Tuple.Map[props.type, Create] =
//     props.map([T] => (t: T) => fCreate(t))
