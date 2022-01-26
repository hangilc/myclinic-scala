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
    val ele = div(id := "content")(
      div(id := "banner", "診察"),
      div(id := "workarea")(
        div(id := "side-bar")(
          div(id := "side-menu"),
          div(id := "hotline-input"),
          div(id := "hotline-commands")(
            button("送信"),
            button("了解"),
            button("Beep"),
            PullDown.createLinkAnchor("常用"),
            PullDown.createLinkAnchor("患者")
          ),
          textarea(
            id := "hotline-messages",
            attr("readonly") := "readonly",
            attr("tabindex") := "-1"
          )
        ),
        div(id := "main")
      )
    )
