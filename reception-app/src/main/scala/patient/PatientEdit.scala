package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.Patient
import dev.fujiwara.domq.dateinput.{DateInput, DateOptionInput}
import org.scalajs.dom.HTMLInputElement
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.web.appbase.PatientValidator
import dev.myclinic.scala.web.appbase.SexValidator
import dev.myclinic.scala.webclient.{Api, global}
import scala.util.{Success, Failure}
import scala.language.implicitConversions

class PatientEdit(ui: PatientEdit.UI, patient: Patient):
  val ele = ui.ele
  val patientForm = new PatientEdit.PatientForm(ui.formUI, patient)
  var onDone: () => Unit = () => ()
  var onCancel: () => Unit = () => ()

  ui.cancelButton(onclick := (() => onCancel()))
  ui.enterButton(onclick := (onEnter _))

  def onEnter(): Unit = 
    ui.errBox.hide()
    PatientFormValidator.validateForUpdate(patient.patientId, ui.formUI) match {
      case Right(patient) => 
        Api.updatePatient(patient).onComplete {
          case Success(_) => onDone()
          case Failure(ex) => ui.errBox.show(ex.getMessage)
        }
      case Left(msg) => ui.errBox.show(msg)
    }
  

object PatientEdit:
  def apply(patient: Patient): PatientEdit =
    new PatientEdit(new UI, patient)

  class UI:
    val formUI = new PatientFormUI
    val errBox = ErrorBox()
    val enterButton = button
    val cancelButton = button
    val ele = div(
      formUI.ele,
      errBox.ele,
      div(mt := "4px")(
        enterButton("入力"),
        cancelButton("キャンセル")
      )
    )

  class PatientForm(ui: PatientFormUI, patient: Patient):
    ui.patientId(innerText := patient.patientId.toString)
    ui.lastNameInput.value = patient.lastName
    ui.firstNameInput.value = patient.firstName
    ui.lastNameYomiInput.value = patient.lastNameYomi
    ui.firstNameYomiInput.value = patient.firstNameYomi
    ui.ele.qSelector(s"input[type=radio][name=sex][value=${patient.sex.code}]").foreach(
      e => e.asInstanceOf[HTMLInputElement].checked = true
    )
    ui.birthdayInput.init(Some(patient.birthday))
    ui.addressInput.value = patient.address
    ui.phoneInput.value = patient.phone

  class PatientFormUI extends PatientFormValidator.PatientFormUI:
    val patientForm = form
    val patientId = span
    val lastNameInput = Form.input
    val firstNameInput = Form.input
    val lastNameYomiInput = inputText
    val firstNameYomiInput = inputText
    def sexValue: Option[String] =
      patientForm.qSelector("input[name=sex]:checked").map(e => {
        e.asInstanceOf[HTMLInputElement].value
      })
    val birthdayInput = DateOptionInput()
    val addressInput = inputText
    val phoneInput = inputText
    val ele = patientForm(
      Form.rows(
        span("患者番号") -> patientId,
        span("氏名") -> div(Form.inputGroup)(
          lastNameInput(width := "100px", placeholder := "姓"),
          firstNameInput(width := "100px", ml := "4px", placeholder := "名")
        ),
        span("よみ") -> div(Form.inputGroup)(
          lastNameYomiInput(width := "100px", placeholder := "せい"),
          firstNameYomiInput(width := "100px", ml := "4px", placeholder := "めい")
        ),
        span("性別") -> div(
          radio(name := "sex", value := "M"), span("男"),
          radio(name := "sex", value := "F"), span("女")
        ),
        span("生年月日") -> birthdayInput.ele,
        span("住所") -> addressInput,
        span("電話") -> phoneInput
      )
    )

object PatientFormValidator:
  trait PatientFormUI:
    def lastNameInput: HTMLInputElement
    def firstNameInput: HTMLInputElement
    def lastNameYomiInput: HTMLInputElement
    def firstNameYomiInput: HTMLInputElement
    def sexValue: Option[String]
    def birthdayInput: DateOptionInput
    def addressInput: HTMLInputElement
    def phoneInput: HTMLInputElement
  
  def validateForEnter(ui: PatientFormUI): Either[String, Patient] =
    ???
    // import PatientValidator.*
    // validatePatientForEnter(
    //   validateLastName(ui.lastNameInput.value),
    //   validateFirstName(ui.firstNameInput.value),
    //   validateLastNameYomi(ui.lastNameYomiInput.value),
    //   validateFirstNameYomi(ui.firstNameYomiInput.value),
    //   validateSexInput(ui.sexValue),
    //   validateBirthday(ui.birthdayInput.value),
    //   validateAddress(ui.addressInput.value),
    //   validatePhone(ui.phoneInput.value)
    // ).asEither

  def validateForUpdate(patientId: Int, ui: PatientFormUI): Either[String, Patient] =
    ???
    // import PatientValidator.*
    // validatePatientForUpdate(
    //   validatePatientIdForUpdate(patientId),
    //   validateLastName(ui.lastNameInput.value),
    //   validateFirstName(ui.firstNameInput.value),
    //   validateLastNameYomi(ui.lastNameYomiInput.value),
    //   validateFirstNameYomi(ui.firstNameYomiInput.value),
    //   validateSexInput(ui.sexValue),
    //   validateBirthday(ui.birthdayInput.value),
    //   validateAddress(ui.addressInput.value),
    //   validatePhone(ui.phoneInput.value)
    // ).asEither


