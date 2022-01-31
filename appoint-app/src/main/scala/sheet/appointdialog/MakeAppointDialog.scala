package dev.myclinic.scala.web.appoint.sheet.appointdialog

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.{AppointTime, Appoint, Patient}
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.web.appoint.Misc

class MakeAppointDialog(
    ui: MakeAppointDialog.UI,
    appointTime: AppointTime,
    followingVacantRegular: () => Option[AppointTime]
):
  ui.appointTimeDisp.innerText = Misc.formatAppointTimeSpan(appointTime)
  val dlog = Modal(
    "診察予約入力",
    ui.body(cls := "appoint-dialog-body"),
    ui.commands
  )
  ui.cancelButton(onclick := (() => dlog.close()))

  def open(): Unit =
    dlog.open()
    ui.initFocus()

  def close(): Unit = dlog.close()

object MakeAppointDialog:
  def apply(appointTime: AppointTime, followingVacantRegular: () => Option[AppointTime]): MakeAppointDialog =
    new MakeAppointDialog(new UI, appointTime, followingVacantRegular)

  class UI:
    val appointTimeDisp = div
    val nameValue = NameValue()
    val enterButton = button
    val cancelButton = button
    val body = div(
      appointTimeDisp,
      Form.rows(
        span("患者名：") -> nameValue.ui.ele
      )
    )
    val commands = div(
      enterButton("入力"),
      cancelButton("キャンセル")
    )
    def initFocus(): Unit = nameValue.initFocus()

  class NameValue(val ui: NameValue.UI):
    ui.searchForm(onsubmit := (onSubmit _))
    ui.searchIcon(onclick := (onSubmit _))
    ui.searchResult.onSelect = ???

    def onSubmit(): Unit =
      val txt = value
      if !txt.isEmpty then
        for
          patients <- Api.searchPatient(txt)
        yield 
          ui.searchResult.setItems(patients, patient => s"(${patient.patientId}) ${patient.fullName()}")
          ui.searchResult.show()

    def value: String = ui.input.value
    def initFocus(): Unit = ui.input.focus()

  object NameValue:
    def apply(): NameValue =
      new NameValue(new UI)
    class UI:
      val inputArea = div
      val workArea = div
      val searchForm = form
      val input = inputText(width := "100%")
      val searchIcon = Icons.search
      val searchResult = Selection[Patient]()
      searchResult.hide()
      val ele = div(
        inputArea(Form.inputGroup)(
          searchForm(input, flex := "1 1 auto"),
          searchIcon(Icons.defaultStyle, ml := "0.5rem")
        ),
        searchResult.ele
      )