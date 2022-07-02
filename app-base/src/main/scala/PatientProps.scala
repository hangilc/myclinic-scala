package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.prop.*
import dev.myclinic.scala.model.Patient
import dev.myclinic.scala.model.Sex
import dev.myclinic.scala.web.appbase.PatientValidator.{*, given}
import dev.myclinic.scala.web.appbase.PatientValidator
import dev.fujiwara.kanjidate.KanjiDate
import PropUtil.*
import java.time.LocalDate
import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.Implicits.*
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions

case class PatientProps(model: Option[Patient]):

  val props = (
    TextProp[Patient, LastNameError.type, String](
      "姓",
      _.lastName,
      LastNameValidator.validate _
    ),
    TextProp[Patient, FirstNameError.type, String](
      "名",
      _.firstName,
      FirstNameValidator.validate _
    ),
    TextProp[Patient, LastNameYomiError.type, String](
      "姓（よみ）",
      _.lastNameYomi,
      LastNameYomiValidator.validate _
    ),
    TextProp[Patient, FirstNameYomiError.type, String](
      "名（よみ）",
      _.firstNameYomi,
      FirstNameYomiValidator.validate
    ),
    RadioProp[Patient, SexError.type, Sex](
      "性別",
      List("男" -> Sex.Male, "女" -> Sex.Female),
      Sex.Female,
      _.sex,
      SexValidator.validate
    ),
    Prop[Patient, BirthdayError.type, LocalDate](
      "生年月日",
      DateInput(
        _.birthday,
        BirthdayValidator.validate
      ),
      SpanDisp(p => KanjiDate.dateToKanji(p.birthday))
    ),
    Prop[Patient, AddressError.type, String](
      "住所",
      TextInput(
        _.address,
        AddressValidator.validate
      ),
      SpanDisp(_.address)
    ),
    Prop[Patient, PhoneError.type, String](
      "電話",
      TextInput(
        _.phone,
        PhoneValidator.validate
      ),
      SpanDisp(_.phone)
    )
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

  val pModel = PropsModel(model)

  def updateDisp(): Unit = pModel.updateDisp(props)
  def dispPanel: HTMLElement = pModel.dispPanel(props)
  def updateInput(): Unit = pModel.updateInput(props)
  def formPanel: HTMLElement = pModel.formPanel(props)

object PatientProps:
  def disp(patient: Patient): HTMLElement =
    val props = PatientProps(Some(patient))
    props.updateDisp()
    val dispProps = (
      ("患者番号", span(patient.patientId.toString)),
      (
        "姓名",
        div(
          props.lastNameProp.dispSpec.createElement,
          " ",
          props.firstNameProp.dispSpec.createElement
        )
      ),
      (
        "よみ",
        div(
          props.lastNameYomiProp.dispSpec.createElement,
          " ",
          props.firstNameYomiProp.dispSpec.createElement
        )
      ),
      props.birthdayProp,
      props.sexProp,
      props.addressProp,
      props.phoneProp
    )
    props.pModel.dispPanel(dispProps)

  case class Form(model: Option[Patient]):
    val props = PatientProps(model)
    props.updateInput()
    val ele: HTMLElement =
      val formProps = (
        (
          "姓名",
          div(
            props.lastNameProp.inputSpec.createElement,
            " ",
            props.firstNameProp.inputSpec.createElement
          )
        ),
        (
          "よみ",
          div(
            props.lastNameYomiProp.inputSpec.createElement,
            " ",
            props.firstNameYomiProp.inputSpec.createElement
          )
        ),
        props.birthdayProp,
        props.sexProp,
        props.addressProp,
        props.phoneProp
      )
      props.pModel.formPanel(formProps)
    def validateForEnter: Either[String, Patient] =
      val rs = props.pModel.resultsOf(props.props)
      PatientValidator
        .validate(PatientIdValidator.validateForEnter *: rs)
        .asEither
    def validateForUpdate: Either[String, Patient] =
      val rs = props.pModel.resultsOf(props.props)
      PatientValidator
        .validate(
          PatientIdValidator.validateOptionForUpdate(
            model.map(_.patientId)
          ) *: rs
        )
        .asEither

  def form(patientOption: Option[Patient]): Form =
    Form(patientOption)
