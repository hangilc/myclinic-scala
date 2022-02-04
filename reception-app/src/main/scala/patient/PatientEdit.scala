package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.Patient
import dev.fujiwara.dateinput.DateInput

class PatientEdit(ui: PatientEdit.UI, patient: Patient):
  val ele = ui.ele
  ui.patientId(innerText := patient.patientId.toString)

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

  class PatientForm(ui: PatientFormUI, patient: Patient)

  class PatientFormUI:
    val patientId = span
    val lastNameInput = Form.input
    val firstNameInput = Form.input
    val lastNameYomiInput = inputText
    val firstNameYomiInput = inputText
    val birthdayInput = new DateInput()
    val address = inputText
    val phone = inputText
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
        span("住所") -> address,
        span("電話") -> phone
      )
    )
