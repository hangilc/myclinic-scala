package dev.myclinic.scala.web.appoint.sheet

import dev.fujiwara.domq.Dialog
import dev.fujiwara.domq.ElementQ.{given, *}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.Html._
import dev.fujiwara.domq.Modal
import dev.myclinic.scala.model.AppointTime
import dev.myclinic.scala.util.KanjiDate
import dev.fujiwara.domq.Binding.InputBinding
import dev.fujiwara.domq.Binding.TextBinding
import dev.fujiwara.domq.Binding.bindTo
import scala.language.implicitConversions
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.raw.HTMLInputElement

class MakeAppointDialog(appointTime: AppointTime, handler: String => Unit)
    extends Dialog():

  title = "診察予約入力"

  val nameInputBinding = InputBinding()
  val nameErrorBinding = TextBinding()

  content(
    div(cls := "fw-bold text-center mb-2")(dateTimeRep),
    form(
      div(cls := "row")(
        div(cls := "col-auto")(
          label(cls := "form-label")("患者名")
        ),
        div(cls := "col-auto")(
          input(
            attr("type") := "text",
            cls := "form-control",
            bindTo(nameInputBinding)
          ),
          div(cls := "invalid-feedback", bindTo(nameErrorBinding))
        )
      )
    )
  )

  commandBox(
    button(
      attr("type") := "button",
      cls := "btn btn-secondary",
      Dialog.closeButton
    )("キャンセル"),
    button(
      attr("type") := "button",
      cls := "btn btn-primary",
      onclick := (onOkClick _)
    )("入力")
  )

  def dateTimeRep: String =
    val d = appointTime.date
    val t = appointTime.fromTime
    val youbi = KanjiDate.youbi(d)
    val m = d.getMonthValue()
    val day = d.getDayOfMonth()
    val hour = t.getHour()
    val minute = t.getMinute()
    s"${m}月${day}日（$youbi）${hour}時${minute}分"

  def onOkClick(): Unit =
    val name = nameInputBinding.value
    val ok: Boolean = validateName(
      name,
      errs => {
        nameErrorBinding.text = errs
        nameInputBinding.setValid(false)
      }
    )
    if ok then
      close()
      handler(name)

  def validateName(name: String, error: String => Unit): Boolean =
    if name.isEmpty then
      error("患者名が入力されていません。")
      false
    else true

object MakeAppointDialog:
  // def open(appointTime: AppointTime, handler: String => Unit): Unit =
  //   val dlog = new MakeAppointDialog(appointTime, handler)
  //   dlog.open()

  def open(appointTime: AppointTime, handler: String => Unit): Unit =
    val dlog = Modal(
      "診察予約入力", close => makeContent(appointTime, handler, close)
    )
    dlog.open()

  def makeContent(
      appointTime: AppointTime,
      handler: String => Unit,
      close: () => Unit
  ): HTMLElement =
    val nameInput: HTMLInputElement = input(attr("type") := "text")
    val errorBox: HTMLElement = div()
    def onEnterClick(): Unit = 
      validateName(nameInput.value) match {
        case Right(name) => { handler(name); close() }
        case Left(e) => showError(e)
      }
    def showError(msg: String): Unit = 
      errorBox.clear()
      errorBox(msg, display := "block")

    div(
      div(Modal.modalBody)(
        div(dateTimeRep(appointTime)),
        errorBox(
          display := "none",
          color := "red",
          border := "1px solid red"
        ),
        div(
          span("患者名："),
          nameInput(ml := "0.5rem")
        ),
      ),
      div(Modal.modalCommands)(
        button("入力", onclick := (() => onEnterClick())),
        button("キャンセル", onclick := (() => close()))
      )
    )

  def dateTimeRep(appointTime: AppointTime): String =
    val d = appointTime.date
    val t = appointTime.fromTime
    val youbi = KanjiDate.youbi(d)
    val m = d.getMonthValue()
    val day = d.getDayOfMonth()
    val hour = t.getHour()
    val minute = t.getMinute()
    val hour2 = appointTime.untilTime.getHour()
    val minute2 = appointTime.untilTime.getMinute()
    s"${m}月${day}日（$youbi）${hour}時${minute}分 - ${hour2}時${minute2}分"

  def validateName(input: String): Either[String, String] =
    if input.isEmpty then Left("患者名が入力されていません。") else Right(input)
  