package dev.myclinic.scala.web.practiceapp

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import dev.fujiwara.domq.all.{*, given}
import org.scalajs.dom.document
import dev.myclinic.scala.web.appbase.{HotlineEnv}
import dev.myclinic.scala.web.appbase.EventFetcher
import dev.myclinic.scala.web.appbase.EventPublishers
import dev.myclinic.scala.model.*
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.web.appbase.SideMenu
import dev.myclinic.scala.web.practiceapp.practice.PracticeService
import dev.myclinic.scala.web.appbase.SideMenuProcs
import dev.myclinic.scala.web.appbase.SideMenuService
import dev.myclinic.scala.web.appbase.MockSideMenuService
import dev.myclinic.scala.web.appbase.HotlineBlock
import dev.myclinic.scala.webclient.global
import dev.myclinic.scala.web.appbase.PageLayout1
import dev.myclinic.scala.web.practiceapp.practice.disease.Frame
import scala.language.implicitConversions
import dev.myclinic.scala.web.practiceapp.PracticeBus
import dev.myclinic.scala.web.practiceapp.cashier.CashierService
import dev.myclinic.scala.web.practiceapp.phone.PhoneService
import dev.myclinic.scala.util.NumberUtil.format
import scala.util.Success
import scala.util.Failure

class JsMain(using EventFetcher):
  val ui = new PageLayout1("practice", "reception")
  ui.banner("診察")
  ui.sideMenu.addItems(sideMenuItems)
  document.body(ui.ele)
  for
    _ <- ui.hotline.init()
    _ <- JsMain.fetcher.start()
    _ <- Frame.init()
  yield StartUp.run(this)

  private def sideMenuItems: List[(String, SideMenuProcs => SideMenuService)] =
    List(
      "診察" -> (_ => PracticeService()),
      "会計" -> (_ => CashierService()),
      "受付" -> (_ => MockSideMenuService("受付")),
      "ファックス済処方箋" -> (_ => MockSideMenuService("ファックス済処方箋")),
      "訪問看護" -> (_ => MockSideMenuService("訪問看護")),
      "主治医意見書" -> (_ => MockSideMenuService("主治医意見書")),
      "ファックス送信" -> (_ => MockSideMenuService("ファックス送信")),
      "印刷設定" -> (_ => MockSideMenuService("印刷設定")),
      "紹介状" -> (_ => MockSideMenuService("紹介状")),
      "診断書" -> (_ => MockSideMenuService("診断書")),
      "電話" -> (_ => PhoneService())
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

class MainUI(using EventFetcher):
  val workarea = div
  val sideMenu = SideMenu(workarea)
  val hotlineBlock = new HotlineBlock("practice", "reception")
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
    val jsMain = new JsMain
    document.body(jsMain.ui.ele) 

  val publishers = new EventPublishers:
    override def onPaymentCreated(payment: Payment): Unit =
      PracticeBus.paymentEntered.publish(payment)
    override def onWqueueCreated(wqueue: Wqueue): Unit =
      PracticeBus.wqueueEntered.publish(wqueue)
    override def onWqueueUpdated(wqueue: Wqueue): Unit =
      PracticeBus.wqueueUpdated.publish(wqueue)
    override def onWqueueDeleted(wqueue: Wqueue): Unit =
      PracticeBus.wqueueDeleted.publish(wqueue)
    override def onChargeCreated(charge: Charge): Unit =
      PracticeBus.chargeEntered.publish(charge)
    override def onChargeUpdated(charge: Charge): Unit =
      PracticeBus.chargeUpdated.publish(charge)

  given fetcher: EventFetcher = new EventFetcher
  fetcher.appModelEventPublisher.subscribe(event => publishers.publish(event))
  fetcher.hotlineBeepEventPublisher.subscribe(event =>
    publishers.publish(event)
  )
