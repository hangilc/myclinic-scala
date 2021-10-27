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

class PatientNamePart(var patientName: String, appointId: Int):
  val keyPart = span("患者名：")
  val valuePart = div()
  var valuePartHandler: ValuePartHandler = Disp()
  valuePartHandler.populate()

  def onPatientNameChanged(newPatientName: String): Unit =
    patientName = newPatientName
    setValuePartHandler(Disp())

  def setValuePartHandler(handler: ValuePartHandler): Unit =
    valuePartHandler = handler
    valuePartHandler.populate()

  trait ValuePartHandler:
    def populate(): Unit

  class Disp() extends ValuePartHandler:
    val wrapper = valuePart
    val searchIcon = Icons.search(color = "gray")
    val workarea = div()
    val errBox = ErrorBox()
    val ele = div(
      patientName,
      searchIcon(displayNone, Icons.defaultStyle, ml := "0.5rem")(
        onclick := (onSearchClick _)
      ),
      workarea,
      errBox.ele
    )
    ele(onmouseenter := (() => {
      searchIcon(displayDefault)
      ()
    }))
    ele(onmouseleave := (() => {
      searchIcon(displayNone)
      ()
    }))

    def populate(): Unit =
      wrapper.innerHTML = ""
      wrapper(ele)

    def makeNameSlot(patient: Patient): HTMLElement =
      div(hoverBackground("#eee"), padding := "2px 4px", cursor := "pointer")(
        s"(${patient.patientId}) ${patient.fullName()}"
      )

    def onSearchClick(): Unit =
      errBox.hide()
      workarea.innerHTML = ""
      for patients <- Api.searchPatient(patientName)
      yield {
        patients.foreach(patient => {
          val slot = makeNameSlot(patient)
          slot(onclick := (() => applyPatient(patient)))
          workarea(slot)
        })
      }

    def applyPatient(patient: Patient): Unit =
      for
        appoint <- Api.getAppoint(appointId)
        newAppoint = {
          appoint.copy(
            patientName = patient.fullName("　"),
            patientId = patient.patientId
          )
        }
        _ <- Api.updateAppoint(newAppoint)
      yield {}
