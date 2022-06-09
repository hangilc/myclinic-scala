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
import dev.myclinic.scala.web.reception.scan.patientimages.PatientImages

class Scan(mock: Boolean) extends SideMenuService:
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
    val docScan = new DocScan(mock)
    boxes.prepend(docScan.ele)

  def doPatientImages(): Unit =
    val patientImages = new PatientImages
    boxes.prepend(patientImages.ele)

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

    

