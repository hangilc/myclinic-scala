package dev.myclinic.scala.web.appoint.sheet.appointdialog.edit

import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Colors, ErrorBox, Form}
import scala.language.implicitConversions
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import dev.myclinic.scala.model.{Patient, Appoint}
import dev.myclinic.scala.validator.AppointValidator
import scala.util.Success
import scala.util.Failure
import cats.data.Validated.Valid
import dev.myclinic.scala.web.appoint.sheet.appointdialog.{
  ValuePart,
  ValuePartManager
}

class PatientNamePart(var appoint: Appoint):
  val keyPart = span("患者名：")
  val manager = ValuePartManager(Disp())
  val valuePart = manager.ele

  def onAppointChanged(newAppoint: Appoint): Unit =
    appoint = newAppoint
    manager.updateUI()

  def changeValuePartTo(valuePart: ValuePart): Unit =
    manager.changeValuePartTo(valuePart)

  class Disp() extends ValuePart with SearchResult:
    val searchIcon = Icons.search
    val editIcon = Icons.pencilAlt
    val main = div(
      appoint.patientName,
      searchIcon(displayNone, ml := "0.5rem")(
        Icons.defaultStyle,
        onclick := (onSearchClick _)
      ),
      editIcon(displayNone, ml := "0.1rem")(
        Icons.defaultStyle,
        onclick := (onEditClick _)
      )
    )
    main(onmouseenter := (() => {
      searchIcon(displayDefault)
      editIcon(displayDefault)
      ()
    }))
    main(onmouseleave := (() => {
      searchIcon(displayNone)
      editIcon(displayNone)
      ()
    }))

    def updateUI(): Unit =
      changeValuePartTo(Disp())

    def onSearchClick(): Unit =
      if workareaIsEmpty then
        initWorkarea()
        (for (gen, patients) <- Api.searchPatient(appoint.patientName)
        yield populateSearchResult(
          patients,
          appoint.appointId,
          () => changeValuePartTo(Disp())
        )).catchErr
      else initWorkarea()

    def onEditClick(): Unit = changeValuePartTo(Edit())

  class Edit() extends ValuePart with SearchResult:
    val input = Form.input(value := appoint.patientName)
    //val enterIcon = Icons.checkCircle(color = Colors.primary, size = "1.2rem")
    val enterIcon = Icons.checkCircle(
      css(style => {
        style.stroke = Colors.primary
      })
    )
    //val discardIcon = Icons.xCircle(color = Colors.danger, size = "1.2rem")
    val discardIcon = Icons.xCircle(
      css(style => {
        style.stroke = Colors.danger
      })
    )
    val searchIcon = Icons.search
    val main = form(onsubmit := (onSearchClick _), Form.inputGroup)(
      input,
      enterIcon(
        Icons.defaultStyle,
        ml := "0.1rem",
        onclick := (onEnterClick _)
      ),
      discardIcon(Icons.defaultStyle, onclick := (onDiscardClick _)),
      searchIcon(Icons.defaultStyle, onclick := (onSearchClick _))
    )
    def updateUI(): Unit =
      input.value = appoint.patientName
      initWorkarea()

    def onEnterClick(): Unit =
      val f =
        for
          appoint <- Api.getAppoint(appoint.appointId)
          newAppoint = appoint.copy(patientName = input.value)
          patientOption <- Api.findPatient(appoint.patientId)
        yield {
          AppointValidator
            .validateForUpdate(newAppoint, patientOption)
            .asEither match {
            case Right(appoint) => {
              Api.updateAppoint(appoint)
              changeValuePartTo(Disp())
            }
            case Left(msg) => showError(msg)
          }
        }
      f.catchErr

    def onDiscardClick(): Unit = changeValuePartTo(Disp())

    def onSearchClick(): Unit =
      if workareaIsEmpty then
        initWorkarea()
        (for (gen, patients) <- Api.searchPatient(input.value)
        yield populateSearchResult(
          patients,
          appoint.appointId,
          () => changeValuePartTo(Disp())
        )).catchErr
      else initWorkarea()
