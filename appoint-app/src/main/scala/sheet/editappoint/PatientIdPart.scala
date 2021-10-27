package dev.myclinic.scala.web.appoint.sheet.editappoint

import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Colors, ErrorBox}
import scala.language.implicitConversions
import org.scalajs.dom.raw.HTMLElement
import dev.myclinic.scala.webclient.Api
import concurrent.ExecutionContext.Implicits.global
import dev.myclinic.scala.model.{Patient}

class PatientIdPart(var patientId: Int, appointId: Int, patientName: => String):
  val keyPart = span("患者番号：")
  val valuePart = div()
  Disp(valuePart).populate()

  class Disp(wrapper: HTMLElement):
    def populate(): Unit =
      val editIcon = Icons.pencilAlt(color = "gray")
      val ele = div(
        span(label),
        editIcon(
          Icons.defaultStyle,
          ml := "0.5rem",
          displayNone,
          onclick := (() => Edit(wrapper).populate())
        )
      )
      wrapper.innerHTML = ""
      wrapper(ele)
      ele(onmouseenter := (() => {
        editIcon(displayDefault)
        ()
      }))
      ele(onmouseleave := (() => {
        editIcon(displayNone)
        ()
      }))
    def label: String =
      if patientId == 0 then "（設定なし）"
      else patientId.toString

  class Edit(wrapper: HTMLElement):
    val input = inputText()
    val enterIcon = Icons.checkCircle(color = Colors.primary)
    val discardIcon = Icons.xCircle(color = Colors.danger)
    val refreshIcon = Icons.refresh(color = "gray")
    val workarea = div()
    val errBox = ErrorBox()
    enterIcon(onclick := (() => onEnter()))
    discardIcon(onclick := (() => Disp(wrapper).populate()))
    refreshIcon(onclick := (() => doRefresh()))

    def populate(): Unit =
      wrapper.innerHTML = ""
      wrapper(
        input(value := initialValue, width := "4rem"),
        enterIcon(
          Icons.defaultStyle,
          ml := "0.5rem"
        ),
        discardIcon(Icons.defaultStyle),
        refreshIcon(Icons.defaultStyle),
        workarea,
        errBox.ele
      )

    def initialValue: String = if patientId == 0 then "" else patientId.toString

    def onEnter(): Unit =
      val inputValue = input.value.trim
      validateInput(inputValue) match {
        case Right(ival) => doUpdate(ival)
        case Left(msg)   => errBox.show(msg)
      }

    def validateInput(inputValue: String): Either[String, Int] =
      if inputValue == "" then Right(0)
      else
        try
          val ival = inputValue.toInt
          if ival >= 0 then Right(ival)
          else Left("入力が負数です。")
        catch {
          case _: Exception => Left("入力が数値でありません。")
        }

    def makePatientSlot(patient: Patient): HTMLElement =
      div(hoverBackground("#eee"), padding := "2px 4px", cursor := "pointer")(
        s"(${patient.patientId}) ${patient.fullName()}"
      )

    def doRefresh(): Unit =
      errBox.hide()
      workarea.innerHTML = ""
      for
        patients <- Api.searchPatient(patientName)
      yield {
        if patients.size == 1 then
          input.value = patients(0).patientId.toString
        else if patients.size > 1 then
          patients.foreach(patient => {
            val slot = makePatientSlot(patient)
            slot(onclick := (() => {
              input.value = patient.patientId.toString
              workarea.innerHTML = ""
            }))
            workarea(slot)
          })
      }

    def doUpdate(newPatientId: Int): Unit =
      import dev.myclinic.scala.validator.AppointValidator
      if newPatientId == patientId then
        Disp(wrapper).populate()
      else
        for
          patientOption <- Api.findPatient(newPatientId)
          appoint <- Api.getAppoint(appointId)
          appointUpdate = appoint.copy(patientId = newPatientId)
        yield {
          patientOption match {
            case None => errBox.show("患者番号に該当する患者情報をみつけられません。")
            case Some(patient) => {
              val v =
                AppointValidator.validatePatientIdConsistency(
                  appointUpdate,
                  patient
                )
              AppointValidator.toEither(v) match {
                case Right(app) => { 
                  Api.updateAppoint(app)
                  Disp(wrapper).populate()
                }
                case Left(msg)  => errBox.show(msg)
              }
            }
          }
        }
