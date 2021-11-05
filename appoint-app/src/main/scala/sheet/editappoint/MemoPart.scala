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
import scala.util.Success
import scala.util.Failure
import dev.myclinic.scala.validator.AppointValidator
import dev.myclinic.scala.validator.AppointValidator.given
import scala.concurrent.Future

class MemoPart(var appoint: Appoint):
  val keyPart: HTMLElement = span("メモ：")
  val valuePart: HTMLElement = div()
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
    val editIcon = Icons.pencilAlt(color = "gray", size = "1.2rem")
    def populate(): Unit =
      val ele = div(
        span(text),
        editIcon(displayNone, ml := "0.5rem", Icons.defaultStyle)(
          onclick := (onEditClick _)
        )
      )
      ele(onmouseenter := (() => {
        editIcon(displayDefault)
        ()
      }))
      ele(onmouseleave := (() => {
        editIcon(displayNone)
        ()
      }))
      wrapper.innerHTML = ""
      wrapper(ele)
    def updateUI(): Unit =
      changeValuePartHandlerTo(Disp())

    def onEditClick(): Unit =
      changeValuePartHandlerTo(Edit())

    def text: String = 
      if appoint.memo.isEmpty then "（設定なし）"
      else appoint.memo

  class Edit() extends ValuePartHandler:
    val wrapper = valuePart
    val input = inputText()
    val enterIcon = Icons.checkCircle(color = Colors.primary)
    val discardIcon = Icons.xCircle(color = Colors.danger)
    val errBox = ErrorBox()
    enterIcon(onclick := (onEnter _))
    discardIcon(onclick := (() => {
      changeValuePartHandlerTo(Disp())
    }))
    def populate(): Unit =
      wrapper.innerHTML = ""
      wrapper(
        input(value := appoint.memo),
        enterIcon(Icons.defaultStyle, ml := "0.5rem"),
        discardIcon(Icons.defaultStyle),
        errBox.ele
      )
    def updateUI(): Unit =
      input.value = appoint.memo
    def onEnter(): Unit = 
      val f =
        for
          appoint <- Api.getAppoint(appoint.appointId)
          newAppoint = appoint.copy(memo = input.value)
          patientOption <-Api.findPatient(appoint.patientId)
          validated = AppointValidator.validateForUpdate(newAppoint, patientOption).toEither()
          ok <- validated match {
            case Right(appoint) => Api.updateAppoint(appoint)
            case Left(msg) => {
              errBox.show(msg)
              Future.successful(false)
            }
          }
        yield {
          if ok then changeValuePartHandlerTo(Disp())
        }
      f.onComplete {
        case Success(_) => ()
        case Failure(ex) => errBox.show(ex.toString)
      }

