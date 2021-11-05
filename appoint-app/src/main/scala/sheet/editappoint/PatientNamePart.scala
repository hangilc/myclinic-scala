package dev.myclinic.scala.web.appoint.sheet.editappoint

import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Colors, ErrorBox}
import scala.language.implicitConversions
import org.scalajs.dom.raw.HTMLElement
import dev.myclinic.scala.webclient.Api
import concurrent.ExecutionContext.Implicits.global
import dev.myclinic.scala.model.{Patient, Appoint}
import dev.myclinic.scala.validator.AppointValidator
import scala.util.Success
import scala.util.Failure
import cats.data.Validated.Valid

class PatientNamePart(var appoint: Appoint):
  val keyPart = span("患者名：")
  val valuePart = div()
  var valuePartHandler: ValuePartHandler = Disp()
  valuePartHandler.populate()

  def onAppointChanged(newAppoint: Appoint): Unit =
    appoint = newAppoint
    valuePartHandler.updateUI()

  def changeValuePartHandlerTo(handler: ValuePartHandler): Unit =
    valuePartHandler = handler
    valuePartHandler.populate()

  trait ValuePartHandler:
    def populate(): Unit
    def updateUI(): Unit

  class Disp() extends ValuePartHandler:
    val wrapper = valuePart
    val searchIcon = Icons.search(color = "gray", size = "1.2rem")
    val editIcon = Icons.pencilAlt(color = "gray", size = "1.2rem")
    val workarea = div()
    val errBox = ErrorBox()
    val ele = div(
      appoint.patientName,
      searchIcon(displayNone, ml := "0.5rem")(
        Icons.defaultStyle,
        onclick := (onSearchClick _)
      ),
      editIcon(displayNone, ml := "0.1rem")(
        Icons.defaultStyle,
        onclick := (onEditClick _)
      ),
      workarea,
      errBox.ele
    )
    ele(onmouseenter := (() => {
      searchIcon(displayDefault)
      editIcon(displayDefault)
      ()
    }))
    ele(onmouseleave := (() => {
      searchIcon(displayNone)
      editIcon(displayNone)
      ()
    }))

    def populate(): Unit =
      wrapper.innerHTML = ""
      wrapper(ele)

    def updateUI(): Unit =
      changeValuePartHandlerTo(Disp())

    def onSearchClick(): Unit =
      if workarea.isEmpty then
        errBox.hide()
        workarea.innerHTML = ""
        for patients <- Api.searchPatient(appoint.patientName)
        yield populateSearchResult(workarea, errBox, patients)
      else workarea.clear()

    def onEditClick(): Unit =
      errBox.hide()
      workarea.innerHTML = ""
      Edit().populate()
      ()

  class Edit() extends ValuePartHandler:
    val input = inputText()
    input.value = appoint.patientName
    val enterIcon = Icons.checkCircle(color = Colors.primary, size = "1.2rem")
    val discardIcon = Icons.xCircle(color = Colors.danger, size = "1.2rem")
    val searchIcon = Icons.search(color = "gray", size = "1.2rem")
    val workarea = div()
    val errBox = ErrorBox()
    def populate(): Unit =
      val wrapper = valuePart
      wrapper.innerHTML = ""
      wrapper(
        input,
        enterIcon(
          Icons.defaultStyle,
          ml := "0.1rem",
          onclick := (onEnterClick _)
        ),
        discardIcon(Icons.defaultStyle, onclick := (onDiscardClick _)),
        searchIcon(Icons.defaultStyle, onclick := (onSearchClick _)),
        workarea,
        errBox.ele
      )

    def updateUI(): Unit =
      input.value = appoint.patientName
      errBox.hide()
      workarea.clear()

    def onEnterClick(): Unit =
      val f =
        for
          appoint <- Api.getAppoint(appoint.appointId)
          newAppoint = appoint.copy(patientName = input.value)
          patientOption <- Api.findPatient(appoint.patientId)
        yield {
          AppointValidator
            .validateForUpdate(newAppoint, patientOption)
            .toEither() match {
            case Right(appoint) => {
              Api.updateAppoint(appoint)
              changeValuePartHandlerTo(Disp())
            }
            case Left(msg) => errBox.show(msg)
          }
        }
      f.onComplete {
        case Success(_)  => ()
        case Failure(ex) => errBox.show(ex.toString)
      }

    def onDiscardClick(): Unit =
      Disp().populate()

    def onSearchClick(): Unit =
      if workarea.isEmpty then
        val f = 
          for
            patients <- Api.searchPatient(input.value)
          yield populateSearchResult(workarea, errBox, patients)
        f.onComplete {
          case Success(_) => ()
          case Failure(ex) => errBox.show(ex.toString)
        }
      else
        workarea.clear()

  def makeNameSlot(patient: Patient): HTMLElement =
    div(hoverBackground("#eee"), padding := "2px 4px", cursor := "pointer")(
      s"(${patient.patientId}) ${patient.fullName()}"
    )

  def populateSearchResult(
      wrapper: HTMLElement,
      errBox: ErrorBox,
      patients: List[Patient]
  ): Unit =
    wrapper.clear()
    patients.foreach(patient => {
      val slot = makeNameSlot(patient)
      wrapper(slot)
      slot(onclick := (() => applyPatient(patient, errBox)))
    })

  def applyPatient(patient: Patient, errBox: ErrorBox): Unit =
    for
      appoint <- Api.getAppoint(appoint.appointId)
      newAppoint = {
        appoint.copy(
          patientName = patient.fullName("　"),
          patientId = patient.patientId
        )
      }
      patientOption <- Api.findPatient(patient.patientId)
    yield {
      AppointValidator
        .validateForUpdate(appoint, patientOption)
        .toEither() match {
        case Right(appoint) => {
          Api.updateAppoint(appoint)
          changeValuePartHandlerTo(Disp())
        }
        case Left(msg) => errBox.show(msg)
      }
    }
