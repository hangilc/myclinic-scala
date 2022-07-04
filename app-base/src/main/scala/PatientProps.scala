package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.prop.{*, given}
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

case class PatientProps() extends dev.fujiwara.domq.ModelProps[Patient] with PropUtil[Patient]:

  val props = (
    TextProp[LastNameError.type, String](
      "姓",
      _.lastName,
      LastNameValidator.validate _
    ).inputElementClass("last-name-input")      
  )


case class PatientPropsOrig(modelOpt: Option[Patient]):

  val props = (
    TextProp[Patient, LastNameError.type, String](
      "姓",
      _.lastName,
      LastNameValidator.validate _
    ).inputElementClass("last-name-input"),
    TextProp[Patient, FirstNameError.type, String](
      "名",
      _.firstName,
      FirstNameValidator.validate _
    ).inputElementClass("first-name-input"),
    TextProp[Patient, LastNameYomiError.type, String](
      "姓（よみ）",
      _.lastNameYomi,
      LastNameYomiValidator.validate _
    ).inputElementClass("last-name-yomi-input"),
    TextProp[Patient, FirstNameYomiError.type, String](
      "名（よみ）",
      _.firstNameYomi,
      FirstNameYomiValidator.validate
    ).inputElementClass("first-name-yomi-input"),
    RadioProp[Patient, SexError.type, Sex](
      "性別",
      List("男" -> Sex.Male, "女" -> Sex.Female),
      Sex.Female,
      _.sex,
      SexValidator.validate,
      toDispValue = (t: Sex, g: RadioGroup[Sex]) => g.findLabel(t) + "性"
    ),
    DateProp[Patient, BirthdayError.type](
      "生年月日",
      _.birthday,
      BirthdayValidator.validate
    ),
    TextProp[Patient, AddressError.type, String](
      "住所",
      _.address,
      AddressValidator.validate
    ).inputElementClass("address-input"),
    TextProp[Patient, PhoneError.type, String](
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

  def formPanel: HTMLElement = Prop.formPanel(formProps)(cls := "patient-form")
  def dispPanel: HTMLElement = Prop.dispPanel(dispProps)

  def updateInput(): this.type =
    val updater = new InputUpdater[Patient](modelOpt)
    import updater.given
    updater.update(props)
    this

  def updateDisp(): this.type =
    val updater = new DispUpdater[Patient](modelOpt)
    import updater.given
    updater.update(props)
    this

  def validatedForEnter: Either[String, Patient] =
    PatientValidator.validate(
      PatientIdValidator.validateForEnter *:
        Prop.resultsOf(props)
    ).asEither

  def validatedForUpdate: Either[String, Patient] =
    PatientValidator.validate(
      PatientIdValidator.validateOptionForUpdate(modelOpt.map(_.patientId)) *:
        Prop.resultsOf(props)
    ).asEither



