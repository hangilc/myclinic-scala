package dev.myclinic.scala.web.reception.scan

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.CustomEvent
import scala.language.implicitConversions
import org.scalajs.dom.{HTMLElement}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure

class Scan extends SideMenuService:
  val newScanButton: HTMLElement = button("新規スキャン", onclick := (newScan _))
  val eScannedBoxes: HTMLElement = div()
  val ele = 
    div(cls := "scan")(
      div(cls := "header")(
        h1("スキャン"),
        newScanButton
      ),
      eScannedBoxes
    )
  addBox()
  ele.listenToCustomEvent[String]("scan-started", deviceId => broadcastScanStarted(deviceId))
  ele.listenToCustomEvent[String]("scan-ended", deviceId => broadcastScanEnded(deviceId))

  def getElement = ele

  def addBox(): Unit =
    val box = ScanBox()
    box.onClosedCallbacks.add(_ => onBoxClose())
    box.init.onComplete {
      case Success(_) =>
        eScannedBoxes.prepend(box.ui.ele)
        box.initFocus
      case Failure(ex) => System.err.println(ex.getMessage)
    }

  private def newScan(): Unit = addBox()

  private def countBoxes: Int =
    eScannedBoxes.qSelectorAll(s".${ScanBox.cssClassName}").size

  private def onBoxClose(): Unit =
    if countBoxes == 0 then addBox()

  private def scanBoxes: List[HTMLElement] =
    ele.qSelectorAll(s".${ScanBox.cssClassName}")

  private def broadcastScanStarted(deviceId: String): Unit =
    CustomEvent.dispatchTo[String]("scan-started", deviceId, scanBoxes)

  private def broadcastScanEnded(deviceId: String): Unit =
    CustomEvent.dispatchTo[String]("scan-ended", deviceId, scanBoxes)

    

