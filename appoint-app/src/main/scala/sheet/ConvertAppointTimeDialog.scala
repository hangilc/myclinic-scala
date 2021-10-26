package dev.myclinic.scala.web.appoint.sheet

import dev.myclinic.scala.model.{AppointTime, Appoint}
import dev.fujiwara.domq.{Modal, Form}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions

object ConvertAppointTimeDialog:
  def open(appointTime: AppointTime): Unit =
    val errElement = div(cls := "error-box")
    val kindInput = input(attr("value") := s"${appointTime.kind}")
    val capacityInput = input(attr("value") := s"${appointTime.capacity}")
    val enterButton = Modal.enter
    val cancelButton = Modal.cancel
    val modal = Modal(
      "予約枠の編集",
      div(
        errElement,
        Form.rows(
          span("kind") -> kindInput(attr("type") := "text"),
          span("capacity") -> capacityInput(attr("type") := "text")
        )
      ),
      div(
        enterButton, cancelButton
      )
    )
    enterButton(onclick := (onEnter _))
    cancelButton(onclick := (() => modal.close()))
    modal.open()

  def onEnter(): Unit =
    ???