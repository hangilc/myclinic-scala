package dev.myclinic.scala.web.reception.scan

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
import scala.concurrent.ExecutionContext.Implicits.global
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

class ScanBox(val ui: ScanBox.UI)(using queue: ScanWorkQueue) extends ScanBox.Scope:
  given ScanBox.Scope = this
  val onClosedCallbacks = new Callbacks[Unit]
  val timestamp = ScanBox.makeTimeStamp
  val patientSearch = new PatientSearch(ui.patientSearchUI)
  val patientDisp = new PatientDisp(ui.patientDispUI)
  val scanTypeSelect = new ScanTypeSelect(ui.scanTypeSelectUI)
  val scannerSelect = new ScannerSelect(ui.scannerSelectUI)
  val scanProgress = new ScanProgress(ui.scanProgressUI, () => selectedScanner)
  val scannedItems = new ScannedItems(
    ui.scannedItemsUI,
    ScanBox.makeTimeStamp,
    () => selectedScanner
  )

  var resolutionStore: Int = 100
  def resolution: Int = resolutionStore

  queue.pinCallbacks.add(_ => adapt())

  def init: Future[Unit] =
    scanTypeSelect.setValue(ScanBox.defaultScanType)
    for _ <- scannerSelect.init
    yield ()
    Future.successful(())

  def initFocus: Unit = patientSearch.focus()

  patientSearch.onSelectCallbacks.add(_ => patientSearch.hideResult)

  def selectedScanType: String = scanTypeSelect.getValue
  def selectedScanner: Option[String] = scannerSelect.selected

  var patient: Option[Patient] = None
  patientSearch.onSelectCallbacks.add(newPatient => patient = Some(newPatient))

  scanProgress.onScannedCallback = savedFile =>
    scannedItems
      .add(savedFile, patient.map(_.patientId), selectedScanType)
      .andThen { case Failure(_) =>
        Api.deleteScannedFile(savedFile)
      }

  patientSearch.onSelectCallbacks.add(patientDisp.setPatient(_))

  patientSearch.onSelectCallbacks.add(_ => adaptScan)

  def adaptUploadButton: Unit =
    val enable = scannedItems.hasUnUploadedImage && queue.isEmpty
    ui.eUploadButton.enable(enable)

  def uploadTask: ScanTask = ScanTask(() => scannedItems.upload)

  ui.eUploadButton(
    onclick := (() => queue.append(uploadTask))
  )

  val onScanningDevicesChangedCallbacks = new Callbacks[Unit]
  var scanningDevices: Set[String] = Set.empty
  ui.ele(oncustomevent[String]("scan-started") := (ev =>
    scanningDevices = scanningDevices + ev.detail
    onScanningDevicesChangedCallbacks.invoke(())
  ))
  ui.ele(oncustomevent[String]("scan-ended") := (ev =>
    scanningDevices = scanningDevices - ev.detail
    onScanningDevicesChangedCallbacks.invoke(())
  ))

  onScanningDevicesChangedCallbacks.add(_ => adaptScan)

  ui.eCloseButton(onclick := (() =>
    def doClose(): Unit =
      ui.ele.remove()
      onClosedCallbacks.invoke(())
      scannedItems.deleteSavedFiles.onComplete {
        case Success(_)  => ()
        case Failure(ex) => System.err.println(ex.getMessage)
      }
    if scannedItems.hasUnUploadedImage then
      ShowMessage.confirm("アップロードされていない画像がありますが、このまま閉じますか？")(doClose _)
    else doClose()
  ))

  private def adaptScan: Unit =
    val enable =
      ScanBox.canScan(patient.map(_.patientId), scannerSelect.selected)
    scanProgress.enableScan(enable)

  def adapt(): Unit =
    adaptScan
    adaptUploadButton
    scannedItems.adapt(patient.map(_.patientId), selectedScanner)

  adapt()

object ScanBox:
  val cssClassName: String = "scan-box"
  val defaultScanType: String = "image"

  def apply(): ScanBox =
    given queue: ScanWorkQueue = ScanWorkQueue()
    val box = new ScanBox(new UI)
    box.onClosedCallbacks.add(_ => ScanWorkQueue.remove(queue))
    box

  class UI:
    val patientSearchUI = new PatientSearch.UI
    val patientDispUI = new PatientDisp.UI
    val scanTypeSelectUI = new ScanTypeSelect.UI
    val scannerSelectUI = new ScannerSelect.UI
    val scanProgressUI = new ScanProgress.UI
    val scannedItemsUI = new ScannedItems.UI
    val eUploadButton = button()
    val eCloseButton = button()
    val ele = div(cls := cssClassName)(
      patientSearchUI.ele,
      patientDispUI.ele(cls := "selected-patient"),
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
