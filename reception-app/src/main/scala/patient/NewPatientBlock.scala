package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form}
import scala.language.implicitConversions
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}
import scala.concurrent.Future
import dev.myclinic.scala.web.appbase.EventSubscriber
import org.scalajs.dom.raw.MouseEvent
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import dev.myclinic.scala.web.appbase.DateInput
import dev.myclinic.scala.validator.{PatientValidator, SexValidator}

class NewPatientBlock(onClose: (NewPatientBlock => Unit)):
  val eLastNameInput = Form.fixedSizeInput("10rem")
  val eFirstNameInput = Form.fixedSizeInput("10rem")
  val eLastNameYomiInput = Form.fixedSizeInput("10rem")
  val eFirstNameYomiInput = Form.fixedSizeInput("10rem")
  val ele = Block(
    "新規患者入力",
    div(
      Form.rows(
        span("氏名") -> div(Form.inputGroup, cls := "name")(
          eLastNameInput,
          eFirstNameInput
        ),
        span("よみ") -> div(Form.inputGroup, cls := "yomi")(
          eLastNameYomiInput,
          eFirstNameYomiInput
        ),
        span("生年月日") -> DateInput().ele,
        span("性別") -> div(
          input(attr("type") := "radio"),
          span("男"),
          input(attr("type") := "radio", attr("checked") := "checked"),
          span("女")
        ),
        span("住所") -> inputText(width := "100%"),
        span("電話") -> inputText(width := "100%")
      )(cls := "new-patient-form")
    ),
    div(
      button("入力", onclick := (onEnter _)),
      button("キャンセル", onclick := (() => onClose(this)))
    )
  ).ele

  private def onEnter(): Unit =
    PatientValidator.validatePatientForEnter(
      PatientValidator.validateLastName(eLastNameInput.value),
      PatientValidator.validateFirstName(eFirstNameInput.value),
      PatientValidator.validateLastNameYomi(eLastNameYomiInput.value),
      PatientValidator.validateFirstNameYomi(eFirstNameYomiInput.value),
      PatientValidator.validateSex(SexValidator.validateSex())
    )
