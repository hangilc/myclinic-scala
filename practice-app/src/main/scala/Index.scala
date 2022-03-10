package dev.myclinic.scala.web.practiceapp

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import dev.fujiwara.domq.all.{*, given}
import org.scalajs.dom.document
import dev.myclinic.scala.web.appbase.{HotlineEnv, HotlineUI, HotlineHandler}
import dev.myclinic.scala.web.appbase.EventFetcher
import dev.myclinic.scala.web.appbase.EventPublishers
import dev.myclinic.scala.model.*
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.web.appbase.SideMenu
import dev.myclinic.scala.web.practiceapp.practice.PracticeService

class JsMain(val ui: MainUI)(using EventFetcher):
  ui.sideMenuWrapper(PracticeSideMenu(ui.main))

object PracticeSideMenu:
  def apply(main: HTMLElement): HTMLElement =
    val sideMenu = SideMenu(main, List(
      "診察" -> (() => new PracticeService)
    ))
    sideMenu.ele

    // a("診察"),
    // a("会計"),
    // a("受付"),
    // a("ファックス済処方箋"),
    // a("訪問看護"),
    // a("主治医意見書"),
    // a("ファックス送信"),
    // a("印刷設定"),
    // a("紹介状"),
    // a("診断書"),

class HotlineBlock:
  val messageInput = textarea
  val sendButton = button
  val rogerButton = button
  val beepButton = button
  val ele = div(
    messageInput(id := "hotline-input"),
    div(id := "hotline-commands")(
      sendButton("送信"),
      rogerButton("了解"),
      beepButton("Beep"),
      PullDown.createLinkAnchor("常用"),
      PullDown.createLinkAnchor("患者")
    ),
    textarea(
      id := "hotline-messages",
      attr("readonly") := "readonly",
      attr("tabindex") := "-1"
    )
  )

class MainUI:
  val sideMenuWrapper = div
  val hotlineBlock = new HotlineBlock
  val main = div
  val ele = div(id := "content")(
    div(id := "banner", "診察"),
    div(id := "workarea")(
      div(id := "side-bar")(
        sideMenuWrapper(id := "side-menu"),
        hotlineBlock.ele
      ),
      main(id := "main")
    )
  )

@JSExportTopLevel("JsMain")
object JsMain:
  @JSExport
  def main(): Unit =
    val jsMain = new JsMain(new MainUI)
    document.body(jsMain.ui.ele)

  val publishers = new EventPublishers

  given fetcher: EventFetcher = new EventFetcher:
    override def publish(event: AppModelEvent): Unit = publishers.publish(event)
    override def publish(event: HotlineBeep): Unit = publishers.publish(event)
