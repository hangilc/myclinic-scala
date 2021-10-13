package dev.myclinic.scala.web.appoint.sheet

import dev.myclinic.scala.model.{AppointTime, Appoint}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.ContextMenu
import dev.fujiwara.domq.Modal
import dev.fujiwara.domq.ModalCommand
import dev.fujiwara.domq.Form
import org.scalajs.dom.raw.MouseEvent
import scala.language.implicitConversions
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.{document, window}
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import dev.myclinic.scala.validator.AppointTimeValidator

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
    val body = div(
      errElement,
      Form.rows(
        span("kind") -> kindInput(attr("type") := "text"),
        span("capacity") -> capacityInput(attr("type") := "text")
      )
    )
    def validate(): Option[AppointTime] =
      AppointTimeValidator
        .validate(
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
      body,
      List(
        ModalCommand.Enter -> (() => None),
        ModalCommand.Cancel -> (() => None)
      ),
      value => println(value)
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
      follows = listFollows(appointTimes).take(nFollows)
      _ <- {
        if follows.isEmpty then Future.unit
        else
          val ids = (appointTime :: follows).map(_.appointTimeId)
          Api.combineAppointTimes(ids)
      }
    yield ()
