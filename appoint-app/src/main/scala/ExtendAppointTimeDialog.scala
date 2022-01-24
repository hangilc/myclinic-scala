package dev.myclinic.scala.web.appoint.sheet

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.AppointTime
import java.time.LocalTime
import scala.util.{Try, Success, Failure}
import dev.myclinic.scala.util.DateUtil

class ExtendAppointTimeDialog(
    ui: ExtendAppointTimeDialog.UI,
    orig: AppointTime
):
  ui.eKind.innerText = orig.kind
  val dialog = Modal("受付枠の時間変更", ui.body, ui.commands)
  ui.eFromTime.value = timeToText(orig.fromTime)
  ui.eUntilTime.value = timeToText(orig.untilTime)
  ui.eEnterButton(onclick := (doEnter _))
  ui.eCancelButton(onclick := (() => dialog.close()))

  def open(): Unit =
    dialog.open()

  def doEnter(): Unit =
    DateUtil.stringToTime(ui.eFromTime.value + ":00")
    ???

  def timeToText(time: LocalTime): String =
    String.format("%02d:%02d", time.getHour, time.getMinute)

object ExtendAppointTimeDialog:
  def apply(appointTime: AppointTime): ExtendAppointTimeDialog =
    new ExtendAppointTimeDialog(new UI, appointTime)

  class UI:
    val eKind = div
    val eFromTime = inputText
    val eUntilTime = inputText
    val errBox = ErrorBox()
    val eEnterButton = button
    val eCancelButton = button
    val body = div(
      eKind,
      Form.rows(
        span("開示時間") -> eFromTime(placeholder := "HH:MM"),
        span("終了時間") -> eUntilTime(placeholder := "HH:MM")
      ),
      errBox.ele
    )
    val commands = div(
      eEnterButton("入力"),
      eCancelButton("キャンセル")
    )
