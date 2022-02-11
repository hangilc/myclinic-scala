package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form, ErrorBox}
import scala.language.implicitConversions
import scala.util.{Success, Failure}
import org.scalajs.dom.{HTMLElement, HTMLInputElement}
import scala.concurrent.Future
import org.scalajs.dom.MouseEvent
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import dev.myclinic.scala.validator.{PatientValidator, SexValidator}
import dev.myclinic.scala.model.{Sex, Patient}
import dev.fujiwara.kanjidate.KanjiDate.Gengou
import dev.fujiwara.dateinput.DateInput

class NewPatientBlock(onClose: (NewPatientBlock => Unit)):
  val eErrorBox = ErrorBox()
  val eLastNameInput = Form.fixedSizeInput("10rem")
  val eFirstNameInput = Form.fixedSizeInput("10rem")
  val eLastNameYomiInput = Form.fixedSizeInput("10rem")
  val eFirstNameYomiInput = Form.fixedSizeInput("10rem")
  val eSexInput = form()
  val eBirthdayInput = DateInput()
  val eAddressInput = inputText()
  val ePhoneInput = inputText()
  val ele = Block(
    "新規患者入力",
    div(
      eErrorBox.ele,
      Form.rows(
        span("氏名") -> div(Form.inputGroup, cls := "name")(
          eLastNameInput,
          eFirstNameInput
        ),
        span("よみ") -> div(Form.inputGroup, cls := "yomi")(
          eLastNameYomiInput,
          eFirstNameYomiInput
        ),
        span("生年月日") -> eBirthdayInput.ele,
        span("性別") -> eSexInput(
          input(attr("type") := "radio", name := "sex", value := Sex.Male.code),
          span("男"),
          input(
            attr("type") := "radio",
            name := "sex",
            value := Sex.Female.code,
            attr("checked") := "checked"
          ),
          span("女")
        ),
        span("住所") -> eAddressInput(width := "100%"),
        span("電話") -> ePhoneInput(width := "100%")
      )(cls := "new-patient-form")
    ),
    div(
      button("入力", onclick := (onEnter _)),
      button("キャンセル", onclick := (() => onClose(this)))
    )
  ).ele

  private def onEnter(): Unit =
    validate().asEither match {
      case Right(patient) => {
        Api.enterPatient(patient).onComplete {
          case Success(_)  => onClose(this)
          case Failure(ex) => eErrorBox.show(ex.getMessage)
        }
      }
      case Left(msg) => eErrorBox.show(msg)
    }

  private def validate(): PatientValidator.Result[Patient] =
    PatientValidator.validatePatientForEnter(
      PatientValidator.validateLastName(eLastNameInput.value),
      PatientValidator.validateFirstName(eFirstNameInput.value),
      PatientValidator.validateLastNameYomi(eLastNameYomiInput.value),
      PatientValidator.validateFirstNameYomi(eFirstNameYomiInput.value),
      PatientValidator.validateSex(
        SexValidator.validateSexInput(eSexInput.getCheckedRadioValue("sex"))
      ),
      PatientValidator.validateBirthday(eBirthdayInput.validate(), _.message),
      PatientValidator.validateAddress(eAddressInput.value),
      PatientValidator.validatePhone(ePhoneInput.value)
    )
