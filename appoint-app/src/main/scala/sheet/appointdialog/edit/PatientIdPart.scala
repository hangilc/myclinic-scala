package dev.myclinic.scala.web.appoint.sheet.appointdialog.edit

import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Colors, ErrorBox, Form}
import scala.language.implicitConversions
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.webclient.Api
import concurrent.ExecutionContext.Implicits.global
import dev.myclinic.scala.model.{Patient, Appoint}
import org.scalajs.dom.document
import dev.myclinic.scala.validator.AppointValidator
import dev.myclinic.scala.validator.AppointValidator.given
import scala.util.Success
import scala.util.Failure
import scala.concurrent.Future
import dev.myclinic.scala.web.appoint.sheet.appointdialog.{
  ValuePart,
  ValuePartManager
}

class PatientIdPart(var appoint: Appoint):
  val keyPart = span("患者番号：")
  val manager = ValuePartManager(Disp())
  val valuePart = manager.ele

  def onAppointChanged(newAppoint: Appoint): Unit =
    appoint = newAppoint
    manager.updateUI()

  def changeValuePartTo(part: ValuePart): Unit =
    manager.changeValuePartTo(part)

  class Disp() extends ValuePart:
    val editIcon = Icons.pencilAlt
    val main = div(
      span(label),
      editIcon(
        Icons.defaultStyle,
        ml := "0.5rem",
        displayNone,
        onclick := (() => changeValuePartTo(Edit()))
      )
    )
    main(onmouseenter := (() => {
      editIcon(displayDefault)
      ()
    }))
    main(onmouseleave := (() => {
      editIcon(displayNone)
      ()
    }))

    def updateUI(): Unit =
      changeValuePartTo(Disp())

    def label: String =
      if appoint.patientId == 0 then "（設定なし）"
      else appoint.patientId.toString

  class Edit() extends ValuePart with SearchResult:
    def patientId: Int = appoint.patientId
    val input = Form.input(value := initialValue)
    //val enterIcon = Icons.checkCircle(color = Colors.primary, size = "1.2rem")
    val enterIcon = Icons.checkCircle(
      css(style => {
        style.stroke = Colors.primary
      })
    )
    //val discardIcon = Icons.xCircle(color = Colors.danger, size = "1.2rem")
    val discardIcon = Icons.xCircle(
      css(style => {
        style.color = Colors.danger
      })
    )
    //val searchIcon = Icons.search(color = "gray", size = "1.2rem")
    val searchIcon = Icons.search
    enterIcon(onclick := (() => onEnter()))
    discardIcon(onclick := (() => changeValuePartTo(Disp())))
    searchIcon(onclick := (() => onSearchClick()))

    val main = form(Form.inputGroup, onsubmit := (onSearchClick _))(
      Form.fixedSizeInput("4rem")(value := initialValue),
      enterIcon(
        Icons.defaultStyle,
        ml := "0.5rem"
      ),
      discardIcon(Icons.defaultStyle),
      searchIcon(Icons.defaultStyle)
    )

    def updateUI(): Unit =
      input.value = appoint.patientId.toString

    def initialValue: String = if patientId == 0 then "" else patientId.toString

    def onEnter(): Unit =
      val patientIdResult = AppointValidator.validatePatientId(input.value.trim)
      patientIdResult.asEither match {
        case Right(patientIdValue) => {
          if patientId == patientIdValue then changeValuePartTo(Disp())
          else
            for
              appoint <- Api.getAppoint(appoint.appointId)
              patientOption <- Api.findPatient(patientIdValue)
            yield {
              val newAppoint = appoint.copy(patientId = patientIdValue)
              AppointValidator
                .validateForUpdate(newAppoint, patientOption)
                .asEither match {
                case Right(newAppoint) => {
                  Api.updateAppoint(newAppoint)
                  changeValuePartTo(Disp())
                }
                case Left(msg) => showError(msg)
              }

            }
        }
        case Left(msg) => showError(msg)
      }

    def onSearchClick(): Unit =
      initWorkarea()
      AppointValidator.validatePatientId(input.value).asEither match {
        case Right(patientId) => {
          (for patientOption <- Api.findPatient(patientId)
          yield populateSearchResult(
            patientOption.toList,
            appoint.appointId,
            () => changeValuePartTo(Disp())
          )).catchErr
        }
        case Left(msg) => showError(msg)
      }
