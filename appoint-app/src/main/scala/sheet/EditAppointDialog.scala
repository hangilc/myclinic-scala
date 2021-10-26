package dev.myclinic.scala.web.appoint.sheet

import dev.fujiwara.domq.ElementQ.{given, *}
import dev.fujiwara.domq.Html.{given, *}
import dev.fujiwara.domq.Modifiers.{given, *}
import dev.fujiwara.domq.Modal
import dev.myclinic.scala.model.{Appoint, AppointTime}
import scala.language.implicitConversions
import dev.myclinic.scala.web.appoint.Misc
import dev.myclinic.scala.util.KanjiDate
import org.scalajs.dom.raw.HTMLElement

object EditAppointDialog:
  def open(
      appointTime: AppointTime,
      appoint: Appoint,
      handler: () => Unit
  ): Unit =
    ()
  //   Modal("予約の編集", close => makeContent(appointTime, appoint, handler, close))
  //     .open()

  // def makeContent(
  //     appointTime: AppointTime,
  //     appoint: Appoint,
  //     handler: () => Unit,
  //     close: () => Unit
  // ): HTMLElement =
  //   div(
  //     div(Modal.modalBody)(
  //       div(dateTimeRep(appointTime)),
  //       div(appoint.patientName)
  //     ),
  //     div(Modal.modalCommands)(
  //       button("予約取消実行", onclick := (() => { handler(); close() })),
  //       button("閉じる", onclick := (() => close()))
  //     )
  //   )

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
