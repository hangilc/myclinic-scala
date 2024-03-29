package dev.myclinic.scala.web.appoint.sheet.appointdialog.edit

import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Colors, ErrorBox}
import scala.language.implicitConversions
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import dev.myclinic.scala.model.{Patient, Appoint}
import scala.util.Success
import scala.util.Failure
import dev.myclinic.scala.web.appbase.AppointValidator
import dev.myclinic.scala.web.appbase.AppointValidator.given
import scala.concurrent.Future
import dev.myclinic.scala.web.appoint.sheet.appointdialog.{
  ValuePart,
  ValuePartManager
}

class MemoPart(var appoint: Appoint):
  val keyPart: HTMLElement = span("メモ：")
  val manager = ValuePartManager(Disp())
  val valuePart: HTMLElement = manager.ele

  def onAppointChanged(newAppoint: Appoint): Unit =
    appoint = newAppoint
    manager.updateUI()

  def changeValuePartTo(part: ValuePart): Unit =
    manager.changeValuePartTo(part)

  class Disp() extends ValuePart:
    val editIcon = Icons.pencilAlt
    val main = div(
      onmouseenter := (() => {
        editIcon(displayDefault)
        ()
      }),
      onmouseleave := (() => {
        editIcon(displayNone)
        ()
      })
    )(
      span(text),
      editIcon(displayNone, ml := "0.5rem", Icons.defaultStyle)(
        onclick := (onEditClick _)
      )
    )

    def updateUI(): Unit =
      changeValuePartTo(Disp())

    def onEditClick(): Unit =
      changeValuePartTo(Edit())

    def text: String =
      if appoint.memoString.isEmpty then "（設定なし）"
      else appoint.memoString

  class Edit() extends ValuePart:
    val input = inputText(value := appoint.memo)
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
    discardIcon(onclick := (() => {
      changeValuePartTo(Disp())
    }))
    val main = div(
      div(
        input(value := appoint.memoString, width := "100%")
      ),
      div(
        enterIcon(Icons.defaultStyle, ml := "0.5rem", onclick := (onEnter _)),
        discardIcon(
          Icons.defaultStyle,
          onclick := (() => {
            changeValuePartTo(Disp())
          })
        )
      )
    )
    def updateUI(): Unit =
      input.value = appoint.memoString
    def onEnter(): Unit =
      val f =
        for
          appoint <- Api.getAppoint(appoint.appointId)
          newAppoint = appoint.modifyMemoString(input.value)
          patientOption <- Api.findPatient(appoint.patientId)
          validated = AppointValidator
            .validateForUpdate(newAppoint, patientOption)
            .asEither
          ok <- validated match {
            case Right(appoint) => Api.updateAppoint(appoint)
            case Left(msg) => {
              showError(msg)
              Future.successful(false)
            }
          }
        yield {
          if ok then changeValuePartTo(Disp())
        }
      f.catchErr
    
