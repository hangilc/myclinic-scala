package dev.myclinic.scala.web.appoint

import dev.fujiwara.domq.Dialog
import dev.fujiwara.domq.ElementQ.{given, *}
import dev.fujiwara.domq.Html.{given, *}
import dev.fujiwara.domq.Modifiers.{given, *}
import dev.myclinic.scala.model.Appoint
import dev.myclinic.scala.webclient.Api
import concurrent.ExecutionContext.Implicits.global
import scala.language.implicitConversions

private class CancelAppointDialogUI(appoint: Appoint) extends Dialog {
  title = "予約の取消"
  def onEnter(): Unit = ()

  content(
    div(cls := "fw-bold text-center mb-2")(Misc.formatAppointDateTime(appoint)),
    div(cls := "fw-bold text-center")(appoint.patientName)
  )

  commandBox(
    button(
      attr("type") := "button",
      cls := "btn btn-secondary",
      Dialog.closeButton
    )("閉じる"),
    button(
      attr("type") := "button",
      cls := "btn btn-primary",
      onclick := (onEnter _)
    )("予約取消実行")
  )
}

object CancelAppointDialog {
  def open(appoint: Appoint): Unit = {
    val ui = new CancelAppointDialogUI(appoint) {
      override def onEnter(): Unit = doCancel(appoint, this)
    }
    ui.open()
  }

  def doCancel(appoint: Appoint, ui: Dialog): Unit = {
    Api
      .cancelAppoint(appoint.date, appoint.time, appoint.patientName)
      .onComplete[Unit](_ => ui.close())
  }
}
