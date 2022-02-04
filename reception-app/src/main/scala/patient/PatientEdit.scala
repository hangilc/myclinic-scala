package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.Patient
import dev.fujiwara.dateinput.DateInput
import org.scalajs.dom.HTMLInputElement

class PatientEdit(ui: PatientEdit.UI, patient: Patient):
  val ele = ui.ele
  val patientForm = new PatientEdit.PatientForm(ui.formUI, patient)

object PatientEdit:
  def apply(patient: Patient): PatientEdit =
    new PatientEdit(new UI, patient)

  class UI:
    val formUI = new PatientFormUI
    val enterButton = button
    val cancelButton = button
    val ele = div(
      formUI.ele,
      div(
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
    ui.birthdayInput.setDate(patient.birthday)
    ui.addressInput.value = patient.address
    ui.phoneInput.value = patient.phone

  class PatientFormUI:
    val patientId = span
    val lastNameInput = Form.input
    val firstNameInput = Form.input
    val lastNameYomiInput = inputText
    val firstNameYomiInput = inputText
    val birthdayInput = new DateInput()
    val addressInput = inputText
    val phoneInput = inputText
    val ele = form(
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
          radio("sex", "M"), span("男"),
          radio("sex", "F"), span("女")
        ),
        span("生年月日") -> birthdayInput.ele,
        span("住所") -> addressInput,
        span("電話") -> phoneInput
      )
    )
