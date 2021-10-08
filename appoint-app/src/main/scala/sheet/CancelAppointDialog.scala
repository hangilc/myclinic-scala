package dev.myclinic.scala.web.appoint.sheet

import dev.fujiwara.domq.Dialog
import dev.fujiwara.domq.ElementQ.{given, *}
import dev.fujiwara.domq.Html.{given, *}
import dev.fujiwara.domq.Modifiers.{given, *}
import dev.myclinic.scala.model.{Appoint, AppointTime}
import scala.language.implicitConversions
import dev.myclinic.scala.web.appoint.Misc
import dev.myclinic.scala.util.KanjiDate

private class CancelAppointDialogUI(appointTime: AppointTime, appoint: Appoint)
    extends Dialog:
  title = "予約の取消"
  def onEnter(): Unit = ()

  content(
    div(cls := "fw-bold text-center mb-2")(
      s"${KanjiDate.dateToKanji(appointTime.date, includeYoubi = true)} " +
        s"${KanjiDate.timeToKanji(appointTime.fromTime)} － " +
        s"${KanjiDate.timeToKanji(appointTime.untilTime)}"
    ),
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

object CancelAppointDialog:
  def open(
      appointTime: AppointTime,
      appoint: Appoint,
      doCancel: Dialog => Unit
  ): Unit =
    val ui = new CancelAppointDialogUI(appointTime, appoint) {
      override def onEnter(): Unit = doCancel(this)
    }
    ui.open()
