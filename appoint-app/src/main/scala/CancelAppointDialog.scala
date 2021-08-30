package dev.myclinic.scala.web.appoint

import dev.fujiwara.domq.Dialog
import dev.fujiwara.domq.DomqUtil
import dev.fujiwara.domq.Html._
import dev.fujiwara.domq.ElementQ._
import dev.fujiwara.domq.Modifiers._
import dev.myclinic.scala.model.Appoint
import scala.util.Success
import scala.util.Failure
import dev.myclinic.scala.webclient.Api
import dev.myclinic.scala.webclient.implicits._

private class CancelAppointDialogUI(appoint: Appoint) extends Dialog {
  title = "予約の取消"
  var onEnter: () => Unit = () => ()

  content(
    div(cls := "fw-bold text-center mb-2")(Misc.formatAppointDateTime(appoint)),
    div(cls := "fw-bold text-center")(appoint.patientName)
  )

  commandBox(
    button(attr("type") := "button", cls := "btn btn-secondary", Dialog.closeButton)("閉じる"),
    button(attr("type") := "button", cls := "btn btn-primary", onclick := onEnter)("予約取消実行")
  )
}

class CancelAppointDialog {
  var onConducted: () => Unit = () => ()
}

object CancelAppointDialog {
  def open(appoint: Appoint): CancelAppointDialog = {
    val dlog = new CancelAppointDialog
    val ui = new CancelAppointDialogUI(appoint)
    ui.onEnter = () => {
      Api.cancelAppoint(appoint.date, appoint.time, appoint.patientName).onComplete(_ match {
        case Success(_) => {
          ui.close()
          dlog.onConducted()
        }
        case Failure(ex) => DomqUtil.alert(ex.toString)
      })
    }
    ui.open()
    dlog
  }
}