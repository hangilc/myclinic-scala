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
import dev.myclinic.scala.web.appbase.SideMenuProcs
import dev.myclinic.scala.web.appbase.SideMenuService
import dev.myclinic.scala.web.appbase.MockSideMenuService

class JsMain(val ui: MainUI)(using EventFetcher):
  ui.sideMenu.addItems(sideMenuItems)

  private def sideMenuItems: List[(String, SideMenuProcs => SideMenuService)] =
    List(
      "診察" -> (_ => PracticeService()),
      "会計" -> (_ => MockSideMenuService("会計")),
      "受付" -> (_ => MockSideMenuService("受付")),
      "ファックス済処方箋" -> (_ => MockSideMenuService("ファックス済処方箋")),
      "訪問看護" -> (_ => MockSideMenuService("訪問看護")),
      "主治医意見書" -> (_ => MockSideMenuService("主治医意見書")),
      "ファックス送信" -> (_ => MockSideMenuService("ファックス送信")),
      "印刷設定" -> (_ => MockSideMenuService("印刷設定")),
      "紹介状" -> (_ => MockSideMenuService("紹介状")),
      "診断書" -> (_ => MockSideMenuService("診断書"))
    )

object PracticeSideMenu:
  def apply(wrapper: HTMLElement): HTMLElement =
    val sideMenu = SideMenu(
      wrapper,
      List(
        "診察" -> (() => new PracticeService)
      )
    )
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
  val workarea = div
  val sideMenu = SideMenu(workarea)
  val hotlineBlock = new HotlineBlock
  val ele = div(id := "content")(
    div(id := "banner", "診察"),
    workarea(id := "workarea")(
      div(id := "side-bar")(
        sideMenu.ele(id := "side-menu"),
        hotlineBlock.ele
      )
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
