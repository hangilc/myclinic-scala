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

// class ScanBoxOrig(val ui: DocScan.UI)(using queue: ScanWorkQueue)
//     extends DocScan.Scope:
//   given DocScan.Scope = this
//   val onClosedCallbacks = new Callbacks[Unit]
//   val timestamp = DocScan.makeTimeStamp
//   val patientSearch = new PatientSearch(ui.patientSearchUI)
//   val patientDisp = new PatientDisp
//   val scanTypeSelect = new ScanTypeSelect(ui.scanTypeSelectUI)
//   val scannerSelect = new ScannerSelect(ui.scannerSelectUI)
//   val scanProgress = new ScanProgress(ui.scanProgressUI, () => selectedScanner)
//   val scannedItems = new ScannedItems(
//     ui.scannedItemsUI,
//     DocScan.makeTimeStamp,
//     () => selectedScanner
//   )

//   var resolutionStore: Int = 100
//   def resolution: Int = resolutionStore

//   queue.pinCallbacks.add(_ => adapt())

//   var scanType: String = scanTypeSelect.getValue
//   def init: Future[Unit] =
//     scanTypeSelect.setValue(DocScan.defaultScanType)
//     scanType = scanTypeSelect.getValue
//     for _ <- scannerSelect.init
//     yield ()
//     Future.successful(())

//   def initFocus: Unit = patientSearch.focus()

//   patientSearch.onSelectCallback = onPatientSelected
//   scanTypeSelect.onChangeCallback = onScanTypeSelected

//   def selectedScanType: String = scanTypeSelect.getValue
//   def selectedScanner: Option[String] = scannerSelect.selected

//   var patient: Option[Patient] = None

//   scanProgress.onScannedCallback = savedFile =>
//     scannedItems
//       .add(savedFile, patient.map(_.patientId), selectedScanType)
//       .andThen { case Failure(_) =>
//         Api.deleteScannedFile(savedFile)
//       }

//   private def onPatientSelected(selected: Patient): Unit =
//     def changePatient(): Unit =
//       patientSearch.hideResult
//       patient = Some(selected)
//       patientDisp.setPatient(selected)
//       val task = ScanTask(() => scannedItems.adjustToPatientChanged(Some(selected.patientId)))
//       queue.append(task)
//     def needConfirm: Boolean = patient.isDefined && scannedItems.size > 0
//     ShowMessage.confirmIf(needConfirm, s"患者を${selected.fullName("")}に変更しますか？")(
//       changePatient _
//     )

//   private def onScanTypeSelected(selected: String): Unit =
//     def changeScanType(): Unit =
//       val task = ScanTask(() => 
//         for _ <- scannedItems.adjustToScanTypeChanged(selected)
//         yield scanType = selected
//       )
//       queue.append(task)
//     if scannedItems.size == 0 then
//       scanType = selected
//     else 
//       ShowMessage.confirm("文書の種類を変更しますか？")(changeScanType _, 
//         () => scanTypeSelect.setValue(scanType)
//       )

//   def adaptUploadButton: Unit =
//     val enable = scannedItems.hasUnUploadedImage && queue.isEmpty
//     ui.eUploadButton.enable(enable)

//   def uploadTask: ScanTask = ScanTask(() => scannedItems.upload)

//   ui.eUploadButton(
//     onclick := (() => queue.append(uploadTask))
//   )

//   val onScanningDevicesChangedCallbacks = new Callbacks[Unit]
//   var scanningDevices: Set[String] = Set.empty
//   ui.ele(oncustomevent[String]("scan-started") := (ev =>
//     scanningDevices = scanningDevices + ev.detail
//     onScanningDevicesChangedCallbacks.invoke(())
//   ))
//   ui.ele(oncustomevent[String]("scan-ended") := (ev =>
//     scanningDevices = scanningDevices - ev.detail
//     onScanningDevicesChangedCallbacks.invoke(())
//   ))

//   onScanningDevicesChangedCallbacks.add(_ => adaptScan)

//   ui.eCloseButton(onclick := (() =>
//     def doClose(): Unit =
//       ui.ele.remove()
//       onClosedCallbacks.invoke(())
//       scannedItems.deleteSavedFiles.onComplete {
//         case Success(_)  => ()
//         case Failure(ex) => System.err.println(ex.getMessage)
//       }
//     if scannedItems.hasUnUploadedImage then
//       ShowMessage.confirm("アップロードされていない画像がありますが、このまま閉じますか？")(doClose _)
//     else doClose()
//   ))

//   private def adaptScan: Unit =
//     val enable =
//       DocScan.canScan(patient.map(_.patientId), scannerSelect.selected)
//     scanProgress.enableScan(enable)

//   def adapt(): Unit =
//     adaptScan
//     scannedItems.adapt(patient.map(_.patientId), selectedScanner)
//     adaptUploadButton

//   adapt()

