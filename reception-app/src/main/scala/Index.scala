package dev.myclinic.scala.web.reception

import dev.fujiwara.domq.all.{_, given}
import dev.myclinic.scala.web.appbase.PageLayout1
import dev.myclinic.scala.web.appbase.SideMenuProcs
import dev.myclinic.scala.web.appbase.SideMenuService
import dev.myclinic.scala.web.appbase.reception.Cashier
import dev.myclinic.scala.web.reception.scan.Scan
import dev.myclinic.scala.webclient.global
import org.scalajs.dom.document

import scala.language.implicitConversions
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.util.Failure
import scala.util.Success
import dev.myclinic.scala.web.appbase.reception.ReceptionSubscriberChannels

@JSExportTopLevel("JsMain")
object JsMain:
  import ReceptionEvent.given
  def sideMenuItems(
      admin: Boolean,
      mock: Boolean
  ): List[(String, SideMenuProcs => SideMenuService)] =
    List(
      "メイン" -> (_ => {
        given ReceptionSubscriberChannels = ReceptionBus.subscriberChannels
        new Cashier()
      }),
      "スキャン" -> (_ => Scan(mock))
    )

  @JSExport
  def main(isAdmin: Boolean, isMock: Boolean): Unit =
    val ui = new PageLayout1("reception", "practice")
    ui.banner("受付")
    ui.sideMenu.addItems(sideMenuItems(isAdmin, isMock))
    document.body(ui.ele)
    val rsc = new dev.fujiwara.domq.Resource()
    rsc.startObserve(ui.ele)
    (for
      _ <- ui.hotline.init()
      _ <- fetcher.start()
    yield ui.sideMenu.invokeByLabel("メイン")).onComplete {
      case Success(_)  => ()
      case Failure(ex) => System.err.println(ex.getMessage)
    }
