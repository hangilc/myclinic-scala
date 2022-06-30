package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.fujiwara.dateinput.DateOptionInput
import dev.fujiwara.domq.DispPanel
import dev.myclinic.scala.web.appbase.PatientValidator

case class PatientForm(init: Option[Patient]):
  val lastNameInput = inputText
  val firstNameInput = inputText
  val lastNameYomiInput = inputText
  val firstNameYomiInput = inputText
  val birthdayInput = DateOptionInput(init.map(_.birthday))
  val sexInput: RadioGroup[Sex] = RadioGroup[Sex](List(
    "男" -> Sex.Male,
    "女" -> Sex.Female
  ), "sex", initValue = init.map(_.sex).orElse(Some(Sex.Female)))
  val addressInput = inputText
  val phoneInput = inputText
  val dispPanel = DispPanel(form = true)
  init.foreach(patient => dispPanel.add("患者番号", initValue(_.patientId.toString)))
  dispPanel.add("氏名", div(
      lastNameInput(placeholder := "姓", value := initValue(_.lastName), cls := "last-name-input"),
      firstNameInput(placeholder := "名", value := initValue(_.firstName), cls := "first-name-input")
    ))
  dispPanel.add("よみ", div(
      lastNameYomiInput(placeholder := "せい", value := initValue(_.lastNameYomi), cls := "last-name-yomi-input"),
      firstNameYomiInput(placeholder := "めい", value := initValue(_.firstNameYomi), cls := "first-name-yomi-input")
    ))
  dispPanel.add("性別", sexInput.ele(cls := "sex-input"))
  dispPanel.add("生年月日", birthdayInput.ele(cls := "birthday-input"))
  dispPanel.add("住所", addressInput(cls := "address-input", value := initValue(_.address)))
  dispPanel.add("電話", phoneInput(cls := "phone-input", value := initValue(_.phone)))

  val ele = dispPanel.ele(cls := "reception-patient-form")

  def initValue(f: Patient => String): String =
    init.map(f).getOrElse("")

  def validateForEnter: Either[String, Patient] =
    import PatientValidator.*
    validatePatientForEnter(
      validateLastName(lastNameInput.value),
      validateFirstName(firstNameInput.value),
      validateLastNameYomi(lastNameYomiInput.value),
      validateFirstNameYomi(firstNameYomiInput.value),
      validateSex(sexInput.value),
      validateBirthday(birthdayInput.value),
      validateAddress(addressInput.value),
      validatePhone(phoneInput.value)
    ).asEither

  def validateForUpdate: Either[String, Patient] =
    import PatientValidator.*
    validatePatientForUpdate(
      validatePatientIdOptionForUpdate(init.map(_.patientId)),
      validateLastName(lastNameInput.value),
      validateFirstName(firstNameInput.value),
      validateLastNameYomi(lastNameYomiInput.value),
      validateFirstNameYomi(firstNameYomiInput.value),
      validateSex(sexInput.value),
      validateBirthday(birthdayInput.value),
      validateAddress(addressInput.value),
      validatePhone(phoneInput.value)
    ).asEither


