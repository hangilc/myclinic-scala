package dev.myclinic.scala.web.reception

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import org.scalajs.dom.document
import dev.fujiwara.domq.ElementQ.*
import dev.fujiwara.domq.Html.*
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{ShowMessage}
import scala.language.implicitConversions
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import scala.util.Success
import scala.util.Failure
import dev.myclinic.scala.model.{Hotline, AppModelEvent}
import dev.myclinic.scala.web.appbase.{EventFetcher, EventPublishers}
import scala.concurrent.Future
import dev.myclinic.scala.model.AppEvent
import dev.myclinic.scala.web.appbase.PageLayout1
import dev.myclinic.scala.web.appbase.SideMenuService
import dev.myclinic.scala.web.appbase.SideMenuProcs
import dev.myclinic.scala.web.reception.cashier.Cashier
import dev.myclinic.scala.web.reception.patient.PatientManagement
import dev.myclinic.scala.web.reception.records.Records
import dev.myclinic.scala.web.reception.scan.Scan

@JSExportTopLevel("JsMain")
object JsMain:
  import ReceptionEvent.given
  def sideMenuItems(
      admin: Boolean,
      mock: Boolean
  ): List[(String, SideMenuProcs => SideMenuService)] =
    List(
      "メイン" -> (_ => Cashier()),
      "患者管理" -> (_ => new PatientManagement()),
      "診療記録" -> (_ => Records()),
      "スキャン" -> (_ => Scan(mock))
    )

  @JSExport
  def main(isAdmin: Boolean, isMock: Boolean): Unit =
    val ui = new PageLayout1("reception", "practice")
    ui.banner("受付")
    ui.sideMenu.addItems(sideMenuItems(isAdmin, isMock))
    document.body(ui.ele)
    (for
      _ <- ui.hotline.init()
      _ <- fetcher.start()
    yield ui.sideMenu.invokeByLabel("メイン")).onComplete {
      case Success(_)  => ()
      case Failure(ex) => System.err.println(ex.getMessage)
    }
