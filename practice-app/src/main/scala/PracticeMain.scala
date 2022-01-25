package dev.myclinic.scala.web.practice

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import dev.fujiwara.domq.all.{*, given}
import org.scalajs.dom.document

@JSExportTopLevel("JsMain")
object JsMain:
  val ui = new UI

  @JSExport
  def main(): Unit =
    document.body(ui.ele)

  class UI:
    val ele = div("PRACTICE")