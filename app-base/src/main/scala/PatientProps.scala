package dev.myclinic.scala.web.appbase

import dev.myclinic.scala.model.Patient
import dev.myclinic.scala.model.Sex
import dev.myclinic.scala.web.appbase.PatientValidator.{*, given}
import dev.myclinic.scala.web.appbase.PatientValidator
import dev.fujiwara.kanjidate.KanjiDate
import java.time.LocalDate
import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.Implicits.*
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.domq.ModelProps
import dev.myclinic.scala.web.appbase.PropUtil

case class PatientProps(modelOpt: Option[Patient]) extends ModelProps[Patient] with PropUtil[Patient]:

  val props = (
    TextProp[LastNameError.type, String](
      "姓",
      _.lastName,
      LastNameValidator.validate _
    ).inputElementClass("last-name-input"),
    TextProp[FirstNameError.type, String](
      "名",
      _.firstName,
      FirstNameValidator.validate _
    ).inputElementClass("first-name-input"),
    TextProp[LastNameYomiError.type, String](
      "姓（よみ）",
      _.lastNameYomi,
      LastNameYomiValidator.validate _
    ).inputElementClass("last-name-yomi-input"),
    TextProp[FirstNameYomiError.type, String](
      "名（よみ）",
      _.firstNameYomi,
      FirstNameYomiValidator.validate
    ).inputElementClass("first-name-yomi-input"),
    RadioProp[SexError.type, Sex](
      "性別",
      List("男" -> Sex.Male, "女" -> Sex.Female),
      Sex.Female,
      _.sex,
      SexValidator.validate,
      toDispValue = (t: Sex, g: RadioGroup[Sex]) => g.findLabel(t) + "性"
    ),
    DateProp[BirthdayError.type](
      "生年月日",
      _.birthday,
      BirthdayValidator.validate
    ),
    TextProp[AddressError.type, String](
      "住所",
      _.address,
      AddressValidator.validate
    ).inputElementClass("address-input"),
    TextProp[PhoneError.type, String](
      "電話",
      _.phone,
      PhoneValidator.validate
    ).inputElementClass("phone-input")
  )

  val (
    lastNameProp,
    firstNameProp,
    lastNameYomiProp,
    firstNameYomiProp,
    sexProp,
    birthdayProp,
    addressProp,
    phoneProp
  ) = props

  val formProps = (
    ("名前", div(displayBlock,
      lastNameProp.inputElement, " ", firstNameProp.inputElement
    )),
    ("よみ", div(displayBlock,
      lastNameYomiProp.inputElement, " ", firstNameYomiProp.inputElement
    )),
    sexProp,
    birthdayProp,
    addressProp,
    phoneProp
  )

  val dispProps = (
    ("名前", div(displayBlock,
      lastNameProp.dispElement, " ", firstNameProp.dispElement
    )),
    ("よみ", div(displayBlock,
      lastNameYomiProp.dispElement, " ", firstNameYomiProp.dispElement
    )),
    sexProp,
    birthdayProp,
    addressProp,
    phoneProp
  )

  def formPanel: HTMLElement = super.formPanel(formProps)(cls := "patient-form")
  def dispPanel: HTMLElement = super.dispPanel(dispProps)(cls := "patient-disp")

  def updateInput(): this.type =
    super.updateInput(props, modelOpt)
    this

  def updateDisp(): this.type =
    super.updateInput(props, modelOpt)
    this

  def validatedForEnter: Either[String, Patient] =
    PatientValidator.validate(
      PatientIdValidator.validateForEnter *:
        resultsOf(props)
    ).asEither

  def validatedForUpdate: Either[String, Patient] =
    PatientValidator.validate(
      PatientIdValidator.validateOptionForUpdate(modelOpt.map(_.patientId)) *:
        resultsOf(props)
    ).asEither

