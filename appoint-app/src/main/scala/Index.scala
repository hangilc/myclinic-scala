package dev.myclinic.web

import org.scalajs.dom
import org.scalajs.dom.document
import java.time.LocalDateTime

object JsMain {
  def main(args: Array[String]): Unit = {
    val s = LocalDateTime.now().toString()
    document.body.innerHTML = s"ScalaJS ${s}"
  }
}
