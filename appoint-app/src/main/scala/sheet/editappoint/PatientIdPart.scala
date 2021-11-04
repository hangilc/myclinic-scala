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
import org.scalajs.dom.document
import dev.myclinic.scala.validator.AppointValidator
import dev.myclinic.scala.validator.AppointValidator.given

class PatientIdPart(var patientId: Int, appointId: Int, patientName: => String):
  val keyPart = span("患者番号：")
  val valuePart = div()
  var valuePartHandler: ValuePartHandler = Disp()
  valuePartHandler.populate()

  def onPatientIdChanged(newPatientId: Int): Unit =
    patientId = newPatientId
    valuePartHandler.onPatientIdChanged()

  trait ValuePartHandler:
    def populate(): Unit
    def onPatientIdChanged(): Unit

  def changeValuePartTo(handler: ValuePartHandler): Unit =
    valuePartHandler = handler
    valuePartHandler.populate()

  class Disp() extends ValuePartHandler:
    val wrapper: HTMLElement = valuePart
    def populate(): Unit =
      val editIcon = Icons.pencilAlt(color = "gray", size = "1.2rem")
      val ele = div(
        span(label),
        editIcon(
          Icons.defaultStyle,
          ml := "0.5rem",
          displayNone,
          onclick := (() => changeValuePartTo(Edit()))
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
    def onPatientIdChanged(): Unit = populate()

  class Edit() extends ValuePartHandler:
    val wrapper = valuePart
    val input = inputText()
    val enterIcon = Icons.checkCircle(color = Colors.primary)
    val discardIcon = Icons.xCircle(color = Colors.danger)
    val refreshIcon = Icons.refresh(color = "gray")
    val workarea = div()
    val errBox = ErrorBox()
    enterIcon(onclick := (() => onEnter()))
    discardIcon(onclick := (() => {
      valuePartHandler = Disp()
      valuePartHandler.populate()
    }))
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

    def onPatientIdChanged(): Unit =
      input.value = initialValue

    def initialValue: String = if patientId == 0 then "" else patientId.toString

    def onEnter(): Unit =
      val patientIdResult = AppointValidator.validatePatientId(input.value.trim)
      patientIdResult.toEither() match {
        case Right(patientIdValue) => {
          if patientId == patientIdValue then
            changeValuePartTo(Disp())
          else
            for
              appoint <- Api.getAppoint(appointId)
              patientOption <- Api.findPatient(patientIdValue)
            yield {
              val newAppoint = appoint.copy(patientId = patientIdValue)
              AppointValidator.validateForUpdate(newAppoint, patientOption)
                .toEither() match {
                  case Right(newAppoint) => {
                    Api.updateAppoint(newAppoint)
                    changeValuePartTo(Disp())
                  }
                  case Left(msg) => errBox.show(msg)
                }

            }
        }
        case Left(msg) => errBox.show(msg)
      }

    def makePatientSlot(patient: Patient): HTMLElement =
      div(hoverBackground("#eee"), padding := "2px 4px", cursor := "pointer")(
        s"(${patient.patientId}) ${patient.fullName()}"
      )

    def doRefresh(): Unit =
      errBox.hide()
      workarea.innerHTML = ""
      for patients <- Api.searchPatient(patientName)
      yield {
        if patients.size == 1 then input.value = patients(0).patientId.toString
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

