package dev.myclinic.scala.web.reception.scan.docscan

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.{HTMLElement, HTMLInputElement}
import dev.fujiwara.domq.{Selection, ErrorBox, ShowMessage, CustomEvent}
import dev.myclinic.scala.model.{Patient, Sex, ScannerDevice}
import java.time.LocalDate
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import scala.concurrent.Future
import java.time.LocalDateTime
import org.scalajs.dom.URL
import org.scalajs.dom.{Blob, BlobPropertyBag}
import scala.scalajs.js
import org.scalajs.dom.HTMLImageElement
import org.scalajs.dom.Event
import scala.util.Success
import scala.util.Failure
import cats.*
import cats.syntax.all.*
import dev.fujiwara.domq.Icons
import dev.myclinic.scala.web.reception.scan.{Callbacks, PatientSearch, PatientDisp}
import dev.myclinic.scala.web.reception.scan.ScanBox

class DocScan(mock: Boolean = false):
  given ds: DataSources = new DataSources
  ds.mock.update(mock)
  val box = new ScanBox
  box.title(innerText := "書類のスキャン")
  box.content(
    (new PatientRow).ele,
    (new DocTypeRow).ele,
    (new ScannerRow).ele,
    (new ScanRow).ele,
    (new ScannedRow).ele,
    (new UploadRow(doClose _)).ele
  )
  def ele = box.ele

  def doClose(): Unit =
    box.ele.remove()

object DocScan:
  val cssClassName: String = "scan-box"
  val defaultScanType: String = "image"

  class UI:
    val patientSearchUI = new PatientSearch.UI
    val patientDisp = new PatientDisp
    val scanTypeSelectUI = new ScanTypeSelect.UI
    val scannerSelectUI = new ScannerSelect.UI
    val scanProgressUI = new ScanProgress.UI
    val scannedItemsUI = new ScannedItems.UI
    val eUploadButton = button()
    val eCloseButton = button()
    val ele = div(cls := cssClassName)(
      patientSearchUI.ele,
      patientDisp.ele(cls := "selected-patient"),
      scanTypeSelectUI.ele(cls := "scan-type-area"),
      scannerSelectUI.ele(cls := "scanner-selection-area"),
      scanProgressUI.ele(cls := "scan-progress-area"),
      div(cls := "scanned")(
        scannedItemsUI.ele,
        eUploadButton("アップロード")(
          cls := "upload-button",
          disabled := true
        )
      ),
      div(cls := "command-box")(
        eCloseButton("閉じる")
      )
    )

  trait Scope:
    def selectedScanner: Option[String]
    def resolution: Int

  def makeTimeStamp: String =
    val at = LocalDateTime.now()
    String.format(
      "%d%02d%02d%02d%02d%02d",
      at.getYear,
      at.getMonthValue,
      at.getDayOfMonth,
      at.getHour,
      at.getMinute,
      at.getSecond
    )

  def canScan(
      patientIdOption: Option[Int],
      deviceIdOption: Option[String]
  ): Boolean =
    patientIdOption.isDefined && deviceIdOption
      .map(!ScanWorkQueue.isScannerBusy(_))
      .getOrElse(false)

  def reportProgress(e: HTMLElement): (Double, Double) => Unit =
    e.innerText = "スキャンの準備中"
    (loaded, total) =>
      val pct = loaded / total * 100
      e.innerText = s"${pct}%"



