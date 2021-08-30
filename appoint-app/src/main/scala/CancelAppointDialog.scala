package dev.myclinic.scala.web.appoint

import dev.fujiwara.domq.Dialog
import dev.fujiwara.domq.DomqUtil
import dev.fujiwara.domq.Html._
import dev.fujiwara.domq.ElementQ._
import dev.fujiwara.domq.Modifiers._
import dev.myclinic.scala.model.Appoint
import org.scalajs.dom.raw._
import org.scalajs.dom.document
import scala.util.Success
import scala.util.Failure
import dev.myclinic.scala.webclient.Api
import dev.myclinic.scala.webclient.implicits._

private class CancelAppointDialogUI(appoint: Appoint) extends Dialog {
  title = "予約の取消"
  def onEnter(): Unit = ()

  content(
    div(cls := "fw-bold text-center mb-2")(Misc.formatAppointDateTime(appoint)),
    div(cls := "fw-bold text-center")(appoint.patientName)
  )

  commandBox(
    button(attr("type") := "button", cls := "btn btn-secondary", Dialog.closeButton)("閉じる"),
    button(attr("type") := "button", cls := "btn btn-primary", onclick := (onEnter _))("予約取消実行")
  )
}

object CancelAppointDialog {
  def open(appoint: Appoint): Unit = {
    val ui = new CancelAppointDialogUI(appoint){
      override def onEnter(): Unit = doCancel(appoint, this)
    }
    ui.open()
  }

  def doCancel(appoint: Appoint, ui: Dialog): Unit = {
      Api.cancelAppoint(appoint.date, appoint.time, appoint.patientName).onComplete(_ match {
        case Success(_) => {
          ui.close()
          Api.getAppoint(appoint.date, appoint.time).onComplete(_ match {
            case Success(modified) => {
              val e = new CustomEvent("mc-appoint-modified", new CustomEventInit{
                detail = modified
              })
              document.body.dispatchEvent(e)
            }
            case Failure(ex) => DomqUtil.alert(ex.toString)
          })
        }
        case Failure(ex) => DomqUtil.alert(ex.toString)
      })
  }
}