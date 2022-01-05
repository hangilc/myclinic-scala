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

class ScanBox(val ui: ScanBox.UI)(using queue: ScanWorkQueue):
  val onClosedCallbacks = new Callbacks[Unit]
  val timestamp = ScanBox.makeTimeStamp
  val patientSearch = new PatientSearch(ui.patientSearchUI)
  val patientDisp = new PatientDisp(ui.patientDispUI)
  val scanTypeSelect = new ScanTypeSelect(ui.scanTypeSelectUI)
  val scannerSelect = new ScannerSelect(ui.scannerSelectUI)
  val scanProgress = new ScanProgress(ui.scanProgressUI, () => selectedScanner)
  val scannedItems = new ScannedItems(ui.scannedItemsUI, ScanBox.makeTimeStamp)

  def init: Future[Unit] =
    scanTypeSelect.setValue(ScanBox.defaultScanType)
    for _ <- scannerSelect.init
    yield ()
    Future.successful(())

  def initFocus: Unit = ()

  patientSearch.onSelectCallbacks.add(_ => patientSearch.hideResult)
  scanProgress.onScannedCallbacks.add(savedFile =>
    createUploadFileName match {
      case Some((uploadFile, patientId)) =>
        (for _ <- scannedItems.add(savedFile, patientId, uploadFile)
        yield ()).onComplete {
          case Success(_)  => adaptUploadButton
          case Failure(ex) => System.err.println(ex.getMessage)
        }
      case None => System.err.println("Patient is not specified.")
    }
  )

  def selectedScanType: String = scanTypeSelect.getValue
  def selectedScanner: Option[String] = scannerSelect.selected

  var patient: Option[Patient] = None
  patientSearch.onSelectCallbacks.add(newPatient => patient = Some(newPatient))

  patientSearch.onSelectCallbacks.add(patientDisp.setPatient(_))

  patientSearch.onSelectCallbacks.add(_ => adaptScan)

  def adaptUploadButton: Unit =
    val enable = scannedItems.hasUnUploadedImage
    ui.eUploadButton.enable(enable)

  ui.eUploadButton(
    onclick := (() =>
      scannedItems.upload.onComplete {
        case Success(_)  => ()
        case Failure(ex) => System.err.println(ex.getMessage)
      }
    )
  )

  def createUploadFileName: Option[(String, Int)] =
    patient.map(patient =>
      val patientId = patient.patientId
      val index = scannedItems.numItems + 1
      val total = scannedItems.numItems + 1
      (
        ScannedItems.createUploadFileName(
          patientId,
          selectedScanType,
          timestamp,
          index,
          total
        ),
        patientId
      )
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

  private def adaptScan: Unit =
    val enable = patient.isDefined && scannerSelect.selected
      .map(!scanningDevices.contains(_))
      .getOrElse(false)
    scanProgress.enableScan(enable)

object ScanBox:
  val cssClassName: String = "scan-box"
  val defaultScanType: String = "image"

  def apply(): ScanBox =
    given queue: ScanWorkQueue = ScanWorkQueue()
    new ScanBox(new UI)

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

// class ScanBoxPrev():
//   given context: ScanContext = new ScanContext

//   val patientSearch = new PatientSearch()
//   // def onPatientSelect(newPatient: Patient): Unit =
//   //   context.patient.update(Some(newPatient))

//   val scanTypeSelect = new ScanTypeSelect(using context)
//   scanTypeSelect.select(ScanBox.defaultScanType)
//   context.scanType.set(scanTypeSelect.selected)
//   scanTypeSelect.addCallback(scanType => context.scanType.update(scanType))

//   val patientDisp = new PatientDisp()
//   context.patient.callbacks.add { () =>
//     context.patient.value match
//       case Some(patient) => patientDisp.setPatient(patient)
//       case None          => ()
//   }
//   val scannerSelect = new ScannerSelect()
//   val scanProgress = new ScanProgress
//   //  def onScan(savedFile: String): Unit = onScanFileAdd(savedFile)

//   val scannedItems = new ScannedItems
//   val eUploadButton = button()
//   val eCloseButton = button()
//   val ele = div(cls := ScanBox.cssClassName)(
//     patientSearch.ele(cls := "search-area"),
//     patientDisp.ele(cls := "selected-patient"),
//     scanTypeSelect.ele(cls := "scan-type-area"),
//     scannerSelect.ele(cls := "scanner-selection-area"),
//     scanProgress.ele(cls := "scan-progress-area"),
//     div(cls := "scanned")(
//       scannedItems.ele,
//       eUploadButton("アップロード")(
//         cls := "upload-button",
//         onclick := (onUploadClick _),
//         disabled := true
//       )
//     ),
//     div(cls := "command-box")(
//       eCloseButton("閉じる", onclick := (onCloseClick _))
//     )
//   )
//   context.isScanning.callbacks.add(() =>
//     val isScanning: Boolean = context.isScanning.value
//     if isScanning then
//       ele.dispatchEvent(CustomEvent[Unit]("scan-started", (), true))
//     else ele.dispatchEvent(CustomEvent[Unit]("scan-ended", (), true))
//     adaptPatientSearch()
//     adaptCloseButton()
//   )
//   context.isUploading.callbacks.add(() => {
//     adaptPatientSearch()
//     adaptCloseButton()
//   })
//   ele.listenToCustomEvent[Boolean](
//     "globally-enable-scan",
//     enable => context.globallyScanEnabled.update(enable)
//   )

//   def init(): Future[Unit] =
//     for _ <- scannerSelect.init()
//     yield context.scannerDeviceId.update(scannerSelect.selected)

//   def initFocus(): Unit = patientSearch.initFocus()

//   def adaptPatientSearch(): Unit =
//     patientSearch.enable(
//       !(context.isScanning.value || context.isUploading.value)
//     )

//   context.patient.callbacks.add(() => adaptScanButton())
//   def adaptScanButton(): Unit =
//     val enable =
//       context.globallyScanEnabled.value &&
//         context.scannerDeviceId.value.isDefined &&
//         context.patient.value.isDefined
//     scanProgress.enableScan(enable)

//   private def adaptCloseButton(): Unit =
//     val disable = context.isScanning.value || context.isUploading.value
//     eCloseButton.enable(!disable)

//   // private def adaptUploadButton(): Unit =
//   //   val enable = scannedItems.hasUnUploadedImage && !scannedItems.isUploading
//   //   eUploadButton.enable(enable)

//   private def onScanFileAdd(savedFile: String): Unit =
//     scannedItems.add(savedFile)

//   private def onUploadClick(): Unit =
//     context.isUploading.update(true)
//     scannedItems
//       .upload()
//       .onComplete(r => {
//         r match {
//           case Success(_)  => ()
//           case Failure(ex) => System.err.println(ex.getMessage)
//         }
//         context.isUploading.update(false)
//       })

//   private def onCloseClick(): Unit =
//     def doClose(): Unit =
//       val parent = ele.parentNode
//       ele.remove()
//       parent.dispatchEvent(CustomEvent("scan-box-close", (), true))
//       scannedItems.deleteSavedFiles().onComplete {
//         case Success(_)  => ()
//         case Failure(ex) => System.err.println(ex.getMessage)
//       }
//     if scannedItems.hasUnUploadedImage then
//       ShowMessage.confirm("アップロードされていない画像がありますが、このまま閉じますか？")(doClose _)
//     else doClose()

//   private def onUpload(done: Boolean): Unit =
//     if done then eCloseButton.innerText = "閉じる"
//     else eCloseButton.innerText = "キャンセル"

// // object ScanBox:
// //   val cssClassName: String = "scan-box"
// //   val defaultScanType: String = "image"

// class ScannedItemsOrig(using context: ScanContext):
//   var items: List[ScannedItem] = List.empty
//   val ele = div()
//   def add(savedFile: String): Future[Unit] =
//     val item = new ScannedItem(
//       savedFile,
//       items.size + 1,
//       items.size + 1
//     )
//     for
//       _ <-
//         if items.size == 1 then items(0).adaptToTotalChanged(2)
//         else Future.successful(())
//     yield
//       items = items :+ item
//       ele(item.ele)
//       context.numScanned.update(items.size)
//   def hasUnUploadedImage: Boolean =
//     items.find(!_.isUploaded).isDefined
//   def upload(): Future[Unit] =
//     uploadItems(items)
//   def deleteSavedFiles(): Future[Unit] =
//     Future.sequence(items.map(_.deleteSavedFile())).map(_ => ())
//   private def uploadItems(items: List[ScannedItem]): Future[Unit] =
//     items match {
//       case Nil => Future.successful(())
//       case h :: t =>
//         h.ensureUploaded().flatMap(_ => uploadItems(t))
//     }
