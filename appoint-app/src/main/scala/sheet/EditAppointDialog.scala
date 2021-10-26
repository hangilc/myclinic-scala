package dev.myclinic.scala.web.appoint.sheet

import dev.fujiwara.domq.ElementQ.{given, *}
import dev.fujiwara.domq.Html.{given, *}
import dev.fujiwara.domq.Modifiers.{given, *}
import dev.fujiwara.domq.{Modal, LocalModal}
import dev.myclinic.scala.model.{Appoint, AppointTime}
import scala.language.implicitConversions
import dev.myclinic.scala.web.appoint.Misc
import dev.myclinic.scala.util.KanjiDate
import org.scalajs.dom.raw.HTMLElement
import dev.myclinic.scala.webclient.Api

class EditAppointDialog(appoint: Appoint, appointTime: AppointTime):
  val ui = UI()
  val dlog = Modal(
    "予約の編集",
    ui.body,
    ui.commands
  )
  ui.execCancelButton(onclick := (() => doExecCancel()))
  ui.closeButton(onclick := (() => dlog.close()))

  def open(): Unit =
    dlog.open()

  def doExecCancel(): Unit =
    confirmCancel(() => {
      Api.cancelAppoint(appoint.appointId)
      dlog.close()
    })

  def confirmCancel(cb: () => Unit): Unit =
    val msg = List(
      Misc.formatAppointTimeSpan(appointTime),
      s"患者名：${appoint.patientName}",
      "この予約をキャンセルしますか？"
    ).mkString("\n")
    val yesButton = Modal.yes
    val cancelButton = Modal.cancel
    val m = LocalModal(
      dlog.workarea,
      div(
        div(innerText := msg),
        div(css(style => {
          style.marginTop = "1rem"
          style.textAlign = "right"
        }))(yesButton, cancelButton)
      )
    )
    yesButton(onclick := (() => {
      m.close()
      cb()
    }))
    cancelButton(onclick := (() => m.close()))
    m.open()

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

  class UI():
    val execCancelButton = button("予約取消実行")
    val closeButton = button("閉じる")
    val body = div(
      div(timesRep),
      div(appoint.patientName)
    )
    val commands = div(execCancelButton, closeButton)

    def dateRep: String = Misc.formatAppointDate(appointTime.date)
    def timesRep: String = Misc.formatAppointTimeSpan(appointTime)


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
