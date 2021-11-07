package dev.myclinic.scala.web.appoint.sheet.editappoint

import dev.fujiwara.domq.ElementQ.{given, *}
import dev.fujiwara.domq.Html.{given, *}
import dev.fujiwara.domq.Modifiers.{given, *}
import dev.fujiwara.domq.{Modal, LocalModal, Icons, ContextMenu, ShowMessage}
import dev.myclinic.scala.model.{Appoint, AppointTime}
import scala.language.implicitConversions
import dev.myclinic.scala.web.appoint.Misc
import dev.myclinic.scala.util.KanjiDate
import org.scalajs.dom.raw.HTMLElement
import dev.myclinic.scala.webclient.Api
import org.scalajs.dom.raw.MouseEvent
import scala.util.Success
import scala.util.Failure
import scala.concurrent.ExecutionContext.Implicits.global

class EditAppointDialog(val appoint: Appoint, appointTime: AppointTime):
  val ui = UI(appoint, appointTime)
  val dlog = Modal(
    "予約の編集",
    ui.body(cls := "edit-appoint-dialog-body"),
    ui.commands
  )
  dlog.auxMenu(
    Icons.menu(color = "gray")(Icons.defaultStyle, onclick := (onMenuClick _))
  )
  ui.execCancelButton(onclick := (() => doExecCancel()))
  ui.closeButton(onclick := (() => dlog.close()))

  def open(): Unit =
    dlog.open()

  def onClose(cb: () => Unit): Unit =
    dlog.onClose(_ => cb())

  def onMenuClick(event: MouseEvent): Unit =
    ContextMenu(List("最初にもどす" -> (doRevert _)), zIndex = dlog.zIndex + 2)
      .open(event)
  def doRevert(): Unit =
    Api.updateAppoint(appoint).onComplete {
      case Success(_)  => ()
      case Failure(ex) => ShowMessage.showError(s"予約の回復に失敗しました。\n${ex}")
    }

  def onAppointUpdated(updated: Appoint): Unit =
    assert(appoint.appointId == updated.appointId)
    ui.onAppointChanged(updated)

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
