package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.ModelProp
import dev.fujiwara.domq.ModelInput
import dev.fujiwara.domq.ModelInputs
import dev.fujiwara.domq.ModelInputProcs
import dev.myclinic.scala.model.*
import PatientValidator.*
import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.DispPanel

object PatientProps:
  class LastNameProp extends ModelProp("姓")
  class FirstNameProp extends ModelProp("名")
  class LastNameYomiProp extends ModelProp("姓（よみ）")
  class FirstNameYomiProp extends ModelProp("名（よみ）")
  class SexProp extends ModelProp("性別")
  class BirthdayProp extends ModelProp("生年月日")
  class AddressProp extends ModelProp("住所")
  class PhoneProp extends ModelProp("電話")

  object lastNameProp extends LastNameProp
  object firstNameProp extends FirstNameProp
  object lastNameYomiProp extends LastNameYomiProp
  object firstNameYomiProp extends FirstNameYomiProp
  object sexProp extends SexProp
  object birthdayProp extends BirthdayProp
  object addressProp extends AddressProp
  object phoneProp extends PhoneProp

  val props = (
    lastNameProp,
    firstNameProp,
    lastNameYomiProp,
    firstNameYomiProp,
    sexProp,
    birthdayProp,
    addressProp,
    phoneProp
  )

case class PatientInputs(modelOpt: Option[Patient])
    extends ModelInput[Patient]
    with ModelInputs[Patient]
    with ModelInputProcs[Patient]:

  class LastNameInput
      extends TextInput[LastNameError.type, String](
        _.lastName,
        LastNameValidator.validate _,
      )

  class FirstNameInput
      extends TextInput[FirstNameError.type, String](
        _.firstName,
        FirstNameValidator.validate _,
      )

  class LastNameYomiInput
      extends TextInput[LastNameYomiError.type, String](
        _.lastNameYomi,
        LastNameYomiValidator.validate _,
      )

  class FirstNameYomiInput
      extends TextInput[FirstNameYomiError.type, String](
        _.firstNameYomi,
        FirstNameYomiValidator.validate _,
      )

  class SexInput 
      extends RadioInput[SexError.type, Sex](
        _.sex,
        SexValidator.validate,
        List("男" -> Sex.Male, "女" -> Sex.Female),
        Sex.Female
    )

  class BirthdayInput extends DateInput[BirthdayError.type](
    _.birthday,
    BirthdayValidator.validate,
    None
  )

  class AddressInput extends TextInput[AddressError.type, String](
    _.address,
    AddressValidator.validate
  )

  class PhoneInput extends TextInput[PhoneError.type, String](
    _.phone,
    PhoneValidator.validate
  )

  type Create[P] = P match {
    case PatientProps.LastNameProp => LastNameInput
    case PatientProps.FirstNameProp => FirstNameInput
    case PatientProps.LastNameYomiProp => LastNameYomiInput
    case PatientProps.FirstNameYomiProp => FirstNameYomiInput
    case PatientProps.SexProp => SexInput
    case PatientProps.BirthdayProp => BirthdayInput
    case PatientProps.AddressProp => AddressInput
    case PatientProps.PhoneProp => PhoneInput
  }

  def fCreate[P](p: P): Create[P] = p match {
    case _: PatientProps.LastNameProp => new LastNameInput()
    case _: PatientProps.FirstNameProp => new FirstNameInput()
    case _: PatientProps.LastNameYomiProp => new LastNameYomiInput()
    case _: PatientProps.FirstNameYomiProp => new FirstNameYomiInput()
    case _: PatientProps.SexProp => new SexInput()
    case _: PatientProps.BirthdayProp => new BirthdayInput()
    case _: PatientProps.AddressProp => new AddressInput()
    case _: PatientProps.PhoneProp => new PhoneInput()
  }

  def create(props: Tuple): Tuple.Map[props.type, Create] =
    props.map[Create]([T] => (t: T) => fCreate(t))

  val inputs = create(PatientProps.props)

  def update(): Unit =
    update(inputs, modelOpt)

  def inputForm: HTMLElement =
    createForm(PatientProps.props, inputs)

  def validateForEnter(): Either[String, Patient] =
    PatientValidator.validate(
      PatientIdValidator.validateForEnter *:
        resultsOf(props)
    ).asEither

  def validateForUpdate: Either[String, Patient] =
    PatientValidator.validate(
      PatientIdValidator.validateOptionForUpdate(modelOpt.map(_.patientId)) *:
        resultsOf(props)
    ).asEither
    




