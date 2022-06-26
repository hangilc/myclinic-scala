package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.fujiwara.dateinput.DateOptionInput

case class PatientForm(init: Option[Patient]):
  val lastNameInput = inputText
  val firstNameInput = inputText
  val lastNameYomiInput = inputText
  val firstNameYomiInput = inputText
  val birthdayInput = DateOptionInput()
  val addressInput = inputText
  val phoneInput = inputText
  val ele = div(
    cls := "reception-patient-form grid-disp",
    span("患者番号", cls := "patient-id-key"),
    span(
      initValue(_.patientId.toString),
      cls := "patient-id-value"
    ),
    span("氏名"),
    div(
      lastNameInput(placeholder := "姓", value := initValue(_.lastName)),
      firstNameInput(placeholder := "名", value := initValue(_.firstName))
    ),
    span("よみ"), div(
      lastNameYomiInput(placeholder := "せい", value := initValue(_.lastNameYomi)),
      firstNameYomiInput(placeholder := "めい", value := initValue(_.firstNameYomi))
    ),
    span("性別"), div(
      radio(name := "sex", value := "M"),
      span("男"),
      radio(name := "sex", value := "F"),
      span("女")
    ),
    span("生年月日"), birthdayInput.ele,
    span("住所"), addressInput(value := initValue(_.address)),
    span("電話"), phoneInput(value := initValue(_.phone))
  )

  def initValue(f: Patient => String): String =
    init.map(f).getOrElse("")
