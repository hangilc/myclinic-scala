package dev.myclinic.scala.web.practice

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import dev.fujiwara.domq.all.{*, given}
import org.scalajs.dom.document
import dev.myclinic.scala.web.appbase.{HotlineEnv, HotlineUI, HotlineHandler}

class JsMain(val ui: JsMain.UI):
  val hotlineHandler = new HotlineHandler(ui.hotlineElements, "practice", "reception")

@JSExportTopLevel("JsMain")
object JsMain:
  val ui = new UI

  @JSExport
  def main(): Unit =
    val jsMain = new JsMain(new UI)
    document.body(jsMain.ui.ele)

  class SideMenuAnchors:
    val anchors = List(
      a("診察")
    )

  class HotlineElements extends HotlineUI:
    val messageInput = textarea
    val sendButton = button
    val rogerButton = button
    val beepButton = button

  class UI:
    val sideMenuAnchors = new SideMenuAnchors
    val hotlineElements = new HotlineElements
    val ele = div(id := "content")(
      div(id := "banner", "診察"),
      div(id := "workarea")(
        div(id := "side-bar")(
          div(id := "side-menu", children := sideMenuAnchors.anchors),
          hotlineElements.messageInput(id := "hotline-input"),
          div(id := "hotline-commands")(
            hotlineElements.sendButton("送信"),
            hotlineElements.rogerButton("了解"),
            hotlineElements.beepButton("Beep"),
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
