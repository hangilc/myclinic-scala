package dev.myclinic.scala.web.appoint

import dev.fujiwara.domq.Dialog
import org.scalajs.dom.raw.Element

class MakeAppointDialog(handler: String => Unit) extends Dialog[String](handler){

  override def setupContent(content: Element): Unit = {

  }

  override def setupCommandBox(commandBox: Element): Unit = {
    
  }

}

object MakeAppointDialog {
  def open(handler: String => Unit): Unit = {
    val dlog = new MakeAppointDialog(handler)
    dlog.open()
  }

}