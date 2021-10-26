package dev.myclinic.scala.web.appoint.sheet

import dev.myclinic.scala.model.{AppointTime, Appoint}
import dev.fujiwara.domq.{Modal, Form, ErrorBox}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.webclient.Api

class ConvertAppointTimeDialog(appointTime: AppointTime):
  val errBox = ErrorBox()
  val kindInput = input(attr("value") := s"${appointTime.kind}")
  val capacityInput = input(attr("value") := s"${appointTime.capacity}")
  val enterButton = Modal.enter
  val cancelButton = Modal.cancel
  val dialog: Modal = Modal(
    "予約枠の編集",
    div(
      errBox.ele,
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
  cancelButton(onclick := (() => dialog.close()))
  
  def open(): Unit = dialog.open()

  def onEnter(): Unit =
    import dev.myclinic.scala.validator.AppointTimeValidator.*
    val v = validateForUpdate(
      appointTime.appointTimeId,
      validateDateValue(appointTime.date),
      validateFromTimeValue(appointTime.fromTime),
      validateUntilTimeValue(appointTime.untilTime),
      validateKindInput(kindInput.value),
      validateCapacityInput(capacityInput.value)
    )
    toEither(v) match {
      case Right(at) => { Api.updateAppointTime(at); dialog.close() }
      case Left(msg) => errBox.show(msg)
    }