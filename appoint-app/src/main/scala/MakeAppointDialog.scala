package dev.myclinic.scala.web.appoint

import dev.fujiwara.domq.Dialog
import dev.fujiwara.domq.ElementQ._
import dev.fujiwara.domq.Modifiers._
import dev.fujiwara.domq.Html._
import dev.myclinic.scala.model.Appoint
import org.scalajs.dom.raw.Element
import dev.myclinic.scala.util.KanjiDate

class MakeAppointDialog(appoint: Appoint, handler: String => Unit)
    extends Dialog[String](handler) {

  title = "診察予約入力"

  override def setupContent(content: Element): Unit = {
    content(
      div(cls := "fw-bold text-center")(dateTimeRep)
    )
  }

  override def setupCommandBox(commandBox: Element): Unit = {
    commandBox(
      button(
        attr("type") := "button",
        cls := "btn btn-secondary",
        Dialog.closeButton
      )("キャンセル"),
      button(attr("type") := "button", cls := "btn btn-primary")("入力")
    )
  }

  def dateTimeRep: String = {
    val d = appoint.date
    val t = appoint.time
    val youbi = KanjiDate.youbi(d)
    s"${d.getMonthValue()}月${d.getDayOfMonth()}日（$youbi）${t.getHour()}時${t.getMinute()}分"
  }

}

object MakeAppointDialog {
  def open(appoint: Appoint, handler: String => Unit): Unit = {
    val dlog = new MakeAppointDialog(appoint, handler)
    dlog.open()
  }

}
