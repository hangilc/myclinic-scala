package dev.myclinic.scala.web.reception

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{ShowMessage, Icons, Colors, ContextMenu}
import dev.fujiwara.domq.PullDown.pullDownLink
import scala.language.implicitConversions
import dev.myclinic.scala.web.appbase.{SideMenu, EventPublishers}
import dev.myclinic.scala.model.{Patient}
import dev.myclinic.scala.webclient.Api
import org.scalajs.dom.{HTMLElement, MouseEvent}
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import scala.util.Success
import scala.util.Failure
import scala.concurrent.Future
import dev.myclinic.scala.web.reception.cashier.Cashier
import dev.myclinic.scala.web.reception.patient.PatientManagement
import dev.myclinic.scala.web.reception.records.Records
import dev.myclinic.scala.web.reception.scan.Scan
import dev.myclinic.scala.web.appbase.EventFetcher
import dev.myclinic.scala.model.Hotline
import dev.myclinic.scala.web.appbase.HotlineBlock

class MainUI(using fetcher: EventFetcher):
  def invoke(label: String): Unit =
    sideMenu.invokeByLabel(label)
  val hotline = new HotlineBlock("reception", "practice")

  private var lastHotlineAppEventId = 0
  private val eMain: HTMLElement = div()
  private val sideMenu = SideMenu(
    eMain,
    List(
      "メイン" -> (() => Cashier()),
      "患者管理" -> (() => new PatientManagement()),
      "診療記録" -> (() => Records()),
      "スキャン" -> (() => Scan())
    )
  )

  val ele =
    div(id := "content")(
      div(id := "banner")("受付"),
      div(id := "workarea")(
        div(id := "side-bar")(
          sideMenu.ele(id := "side-menu"),
          hotline.ele
        ),
        eMain(id := "main")
      )
    )


