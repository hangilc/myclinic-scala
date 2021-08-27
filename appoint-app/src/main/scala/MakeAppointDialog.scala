package dev.myclinic.scala.web.appoint

import dev.fujiwara.domq.Dialog
import dev.fujiwara.domq.ElementQ._
import dev.fujiwara.domq.Modifiers._
import dev.fujiwara.domq.Html._
import dev.myclinic.scala.model.Appoint
import dev.myclinic.scala.util.KanjiDate
import dev.fujiwara.domq.Binding.InputBinding
import dev.fujiwara.domq.Binding.TextBinding
import dev.fujiwara.domq.Binding.bindTo

class MakeAppointDialog(appoint: Appoint, handler: String => Unit)
    extends Dialog[String]() {

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
    button(attr("type") := "button", cls := "btn btn-primary",
      onclick := (onOkClick _))("入力")
  )

  def dateTimeRep: String = {
    val d = appoint.date
    val t = appoint.time
    val youbi = KanjiDate.youbi(d)
    s"${d.getMonthValue()}月${d.getDayOfMonth()}日（$youbi）${t.getHour()}時${t.getMinute()}分"
  }

  def onOkClick(): Unit = {
    val name = nameInputBinding.value
    val ok: Boolean = validateName(name, errs => {
      nameErrorBinding.text = errs
      nameInputBinding.setValid(false)
    })
    if( ok ){
      close()
      handler(name)
    }
  }

  def validateName(name: String, error: String => Unit): Boolean = {
    if( name.isEmpty ){
      error("患者名が入力されていません。")
      false
    } else {
      true
    }
  }

}

object MakeAppointDialog {
  def open(appoint: Appoint, handler: String => Unit): Unit = {
    val dlog = new MakeAppointDialog(appoint, handler)
    dlog.open()
  }

}