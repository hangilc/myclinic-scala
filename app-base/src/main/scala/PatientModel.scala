package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.*
import dev.myclinic.scala.model.*
import PatientValidator.*
import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.Implicits.*

enum PatientProp(val label: String) extends ModelProp(label):
  case PatientIdProp extends PatientProp("患者番号")
  case LastNameProp extends PatientProp("姓")
  case FirstNameProp extends PatientProp("名")
  case LastNameYomiProp extends PatientProp("姓（よみ）")
  case FirstNameYomiProp extends PatientProp("名（よみ）")
  case SexProp extends PatientProp("性別")
  case BirthdayProp extends PatientProp("生年月日")
  case AddressProp extends PatientProp("住所")
  case PhoneProp extends PatientProp("電話")

class PatientInputs(modelOpt: Option[Patient])
    extends ModelInput[Patient]
    with ModelInputs[Patient]
    with ModelInputProcs[Patient]:

  object lastNameInput
      extends ModelTextInput[LastNameError.type, String](
        _.lastName,
        LastNameValidator.validate _
      )

  object firstNameInput
      extends ModelTextInput[FirstNameError.type, String](
        _.firstName,
        FirstNameValidator.validate _
      )

  object lastNameYomiInput
      extends ModelTextInput[LastNameYomiError.type, String](
        _.lastNameYomi,
        LastNameYomiValidator.validate _
      )

  object firstNameYomiInput
      extends ModelTextInput[FirstNameYomiError.type, String](
        _.firstNameYomi,
        FirstNameYomiValidator.validate _
      )

  object sexInput
      extends ModelRadioInput[SexError.type, Sex](
        _.sex,
        SexValidator.validate,
        List("男" -> Sex.Male, "女" -> Sex.Female),
        Sex.Female
      )

  object birthdayInput
      extends ModelDateInput[BirthdayError.type](
        _.birthday,
        BirthdayValidator.validate,
        None
      )

  object addressInput
      extends ModelTextInput[AddressError.type, String](
        _.address,
        AddressValidator.validate
      )

  object phoneInput
      extends ModelTextInput[PhoneError.type, String](
        _.phone,
        PhoneValidator.validate
      )

  type Create[P] = P match {
    case PatientProps.lastNameProp.type      => lastNameInput.type
    case PatientProps.firstNameProp.type     => firstNameInput.type
    case PatientProps.lastNameYomiProp.type  => lastNameYomiInput.type
    case PatientProps.firstNameYomiProp.type => firstNameYomiInput.type
    case PatientProps.sexProp.type           => sexInput.type
    case PatientProps.birthdayProp.type      => birthdayInput.type
    case PatientProps.addressProp.type       => addressInput.type
    case PatientProps.phoneProp.type         => phoneInput.type
  }

  def fCreate[P](p: P): Create[P] = p match {
    case _: PatientProps.lastNameProp.type      => lastNameInput
    case _: PatientProps.firstNameProp.type     => firstNameInput
    case _: PatientProps.lastNameYomiProp.type  => lastNameYomiInput
    case _: PatientProps.firstNameYomiProp.type => firstNameYomiInput
    case _: PatientProps.sexProp.type           => sexInput
    case _: PatientProps.birthdayProp.type      => birthdayInput
    case _: PatientProps.addressProp.type       => addressInput
    case _: PatientProps.phoneProp.type         => phoneInput
  }

  def f[E, T](prop: ModelProp): Input[E, T] = fCreate(prop)

  def create(props: Tuple): Tuple.Map[props.type, Create] =
    props.map[Create]([T] => (t: T) => fCreate(t))

  val inputs = create(PatientProps.props)

  def update(): Unit =
    update(inputs, modelOpt)

  def inputForm: HTMLElement =
    createForm(PatientProps.props, inputs)

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

object PatientReps extends ModelRep[Patient]
    with ModelReps[Patient]
    with ModelRepOps[Patient]:
  object patientIdRep extends ModelSimpleRep[Int](_.patientId)
  object lastNameRep extends ModelSimpleRep[String](_.lastName)
  object firstNameRep extends ModelSimpleRep[String](_.firstName)
  object lastNameYomiRep extends ModelSimpleRep[String](_.lastNameYomi)
  object firstNameYomiRep extends ModelSimpleRep[String](_.firstNameYomi)
  object sexRep extends ModelConvertRep[Sex](_.sex, _.rep + "性")
  object birthdayRep extends ModelDateRep(_.birthday)
  object addressRep extends ModelSimpleRep[String](_.address)
  object phoneRep extends ModelSimpleRep[String](_.phone)

  type Create[P] = P match {
    case PatientProps.patientIdProp.type     => patientIdRep.type
    case PatientProps.lastNameProp.type      => lastNameRep.type
    case PatientProps.firstNameProp.type     => firstNameRep.type
    case PatientProps.lastNameYomiProp.type  => lastNameYomiRep.type
    case PatientProps.firstNameYomiProp.type => firstNameYomiRep.type
    case PatientProps.sexProp.type           => sexRep.type
    case PatientProps.birthdayProp.type      => birthdayRep.type
    case PatientProps.addressProp.type       => addressRep.type
    case PatientProps.phoneProp.type         => phoneRep.type
  }

  def fCreate[P](p: P): Create[P] = p match {
    case _: PatientProps.patientIdProp.type     => patientIdRep
    case _: PatientProps.lastNameProp.type      => lastNameRep
    case _: PatientProps.firstNameProp.type     => firstNameRep
    case _: PatientProps.lastNameYomiProp.type  => lastNameYomiRep
    case _: PatientProps.firstNameYomiProp.type => firstNameYomiRep
    case _: PatientProps.sexProp.type           => sexRep
    case _: PatientProps.birthdayProp.type      => birthdayRep
    case _: PatientProps.addressProp.type       => addressRep
    case _: PatientProps.phoneProp.type         => phoneRep
  }

  def create(props: Tuple): Tuple.Map[props.type, Create] =
    props.map([T] => (t: T) => fCreate(t))



