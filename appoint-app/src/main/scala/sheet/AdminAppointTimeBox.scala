package dev.myclinic.scala.web.appoint.sheet

import dev.myclinic.scala.model.{AppointTime, Appoint}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.ContextMenu
import dev.fujiwara.domq.Modal
import dev.fujiwara.domq.ModalCommand
import dev.fujiwara.domq.Form
import dev.fujiwara.domq.ShowMessage
import org.scalajs.dom.raw.MouseEvent
import scala.language.implicitConversions
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.{document, window}
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import dev.myclinic.scala.validator.AppointTimeValidator
import dev.myclinic.scala.web.appoint.Misc

class AdminAppointTimeBox(appointTime: AppointTime)
    extends AppointTimeBox(appointTime):
  ele.addEventListener(
    "contextmenu",
    (event: MouseEvent) => {
      event.preventDefault
      ContextMenu(
        "Convert" -> doConvert,
        "Combine" -> doCombine
      ).show(event)
    }
  )

  def doConvert(): Unit =
    val errElement = div(css(style => {
      style.color = "red"
      style.margin = "1rem"
    }))
    val kindInput = input(attr("value") := s"${appointTime.kind}")
    val capacityInput = input(attr("value") := s"${appointTime.capacity}")
    def setupBody(body: HTMLElement) = body(
      errElement,
      Form.rows(
        span("kind") -> kindInput(attr("type") := "text"),
        span("capacity") -> capacityInput(attr("type") := "text")
      )
    )
    def setupCommands(close: Modal.CloseFunction, wrapper: HTMLElement) =
      wrapper(
        Modal.enter(onclick := (() => doEnter(close))),
        Modal.cancel(onclick := (() => close()))
      )
    def doEnter(close: Modal.CloseFunction): Unit =
      validate() match {
        case Some(v) => { 
          Api.updateAppointTime(v)
          close() 
        }
        case None => ()
      }
    def validate(): Option[AppointTime] =
      AppointTimeValidator
        .validate(
          appointTime.appointTimeId,
          appointTime.date,
          appointTime.fromTime,
          appointTime.untilTime,
          kindInput.value,
          capacityInput.value
        )
        .bimap(
          e => {
            errElement.innerText =
              e.toNonEmptyList.toList.map(_.message).mkString("\n")
            e
          },
          a => a
        )
        .toOption
    Modal[Unit](
      "予約枠の編集",
      (close, body, commands) => {
        setupBody(body)
        setupCommands(close, commands)
      }
    ).open()

  def doCombine(): Unit =
    val nFollows = 1
    def listFollows(appointTimes: List[AppointTime]): List[AppointTime] =
      appointTimes
        .dropWhile(_.appointTimeId != appointTime.appointTimeId)
        .sliding(2)
        .takeWhile({
          case a :: b :: _ => a.isAdjacentTo(b)
          case _           => false
        })
        .map(_(1))
        .toList
    for
      appointTimes <- Api.listAppointTimesForDate(appointTime.date)
    yield {
      val follows = listFollows(appointTimes).take(nFollows)
      if follows.isEmpty then
        ShowMessage.showMessage("結合する予約枠がありません。")
        Future.unit
      else
        val lines = List(
          "以下のように予約枠を結合します。",
          Misc.formatAppointDate(appointTime.date),
          Misc.formatAppointTime(appointTime.fromTime) + " - ",
          Misc.formatAppointTime(follows.last.untilTime)
        ).mkString("\n")
        Modal("予約枠の結合", (close, body, commands) => {
          body.innerText = lines
          commands(
            Modal.ok()(onclick := { () => 
              val ids = (appointTime :: follows).map(_.appointTimeId)
              Api.combineAppointTimes(ids)
              close()
              }),
            Modal.cancel()(onclick := (() => close()))
          )
        }).open()
    }
