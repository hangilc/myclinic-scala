package dev.myclinic.scala.web.reception.scan

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.CustomEvent
import scala.language.implicitConversions
import org.scalajs.dom.{HTMLElement}
import scala.concurrent.Future
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import scala.util.Success
import scala.util.Failure
import dev.myclinic.scala.web.reception.scan.docscan.DocScan
import dev.myclinic.scala.web.reception.scan.patientimages.PatientImagesOrig
import dev.myclinic.scala.web.reception.scan.patientimages.PatientImages

class Scan() extends SideMenuService:
  val boxes = div
  val ele = div(cls := "content scan")(
        div(cls := "header")(
          h1("スキャン"),
          button("新規スキャン", onclick := (doScan _)),
          button("患者画像", onclick := (doPatientImages _))
        ),
        boxes
      )

  override def getElement = ele

  def doScan(): Unit = 
    val docScan = new DocScan
    boxes.prepend(docScan.ele)

  def doPatientImages(): Unit =
    val patientImages = new PatientImages
    boxes.prepend(patientImages.ele)


  // ui.eNewScanButton(onclick := (newScan _))
  // ui.ePatientImagesButton(onclick := (patientImages _))
  // addBox()


  // def addBox(): Unit =
  //   val box = ScanBox()
  //   box.onClosedCallbacks.add(_ => onBoxClose())
  //   box.init.onComplete {
  //     case Success(_) =>
  //       ui.eScannedBoxes.prepend(box.ui.ele)
  //       box.initFocus
  //     case Failure(ex) => System.err.println(ex.getMessage)
  //   }

  // private def newScan(): Unit = addBox()

  // private def patientImages(): Unit = 
  //   PatientSelect.open(patient => 
  //     val pi = PatientImages(patient)
  //     for 
  //       _ <- pi.init()
  //     yield ui.eScannedBoxes.prepend(pi.ele)
  //   )

  // private def countBoxes: Int =
  //   ui.eScannedBoxes.qSelectorAll(s".${ScanBox.cssClassName}").size

  // private def onBoxClose(): Unit =
  //   if countBoxes == 0 then addBox()

  // private def scanBoxes: List[HTMLElement] =
  //   ui.ele.qSelectorAll(s".${ScanBox.cssClassName}")

  // private def broadcastScanStarted(deviceId: String): Unit =
  //   CustomEvent.dispatchTo[String]("scan-started", deviceId, scanBoxes)

  // private def broadcastScanEnded(deviceId: String): Unit =
  //   CustomEvent.dispatchTo[String]("scan-ended", deviceId, scanBoxes)

object Scan:
  class UI:
    val eNewScanButton = button
    val ePatientImagesButton = button
    val eScannedBoxes = div
    val ele = 
      div(cls := "scan")(
        div(cls := "header")(
          h1("スキャン"),
          eNewScanButton("新規スキャン"),
          ePatientImagesButton("患者画像")
        ),
        eScannedBoxes
      )

  def apply(): Scan = new Scan

    

