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
import dev.myclinic.scala.web.reception.scan.variable.*

class ScanBox():
  val timestamp: String = ScanBox.makeTimeStamp()
  val varPatient = new CachedVariableWithCallbacks[Option[Patient]](None)
  def patient: Option[Patient] = varPatient.get

  val patientSearch = new PatientSearch():
    def onPatientSelect(newPatient: Patient): Unit = varPatient.set(Some(newPatient))

  val patientDisp = new PatientDisp()
  varPatient.addCallback { 
    case Some(patient) => patientDisp.setPatient(patient)
    case None => ()
  }
  val scanTypeSelect = new ScanTypeSelect(ScanBox.defaultScanType):
    def onChange(scanType: String): Unit = ()
  val varScanType: Variable[String] = scanTypeSelect.variable
  val scannerSelect = new ScannerSelect()
  val scanProgress = new ScanProgress(this):
    def onScan(savedFile: String): Unit = onScanFileAdd(savedFile)

  val scannedItems = new ScannedItems(this)
  val eUploadButton = button()
  val eCloseButton = button()
  val ele = div(cls := ScanBox.cssClassName)(
    patientSearch.ele(cls := "search-area"),
    patientDisp.ele(cls := "selected-patient"),
    scanTypeSelect.ele(cls := "scan-type-area"),
    scannerSelect.ele(cls := "scanner-selection-area"),
    scanProgress.ele(cls := "scan-progress-area"),
    div(cls := "scanned")(
      scannedItems.ele,
      eUploadButton("アップロード")(
        cls := "upload-button",
        onclick := (onUploadClick _),
        disabled := true
      )
    ),
    div(cls := "command-box")(
      eCloseButton("閉じる", onclick := (onCloseClick _))
    )
  )
  ele.listenToCustomEvent[Boolean](
    "globally-enable-scan",
    enable => varGloballyScanEnabled.set(enable)
  )

  def init(): Future[Unit] =
    for _ <- scannerSelect.init()
    yield ()

  def initFocus(): Unit = patientSearch.initFocus()

  def selectedScanType: String =
    scanTypeSelect.selected

  def selectedScanner: Option[String] =
    scannerSelect.selected

  val varGloballyScanEnabled = new CachedVariable[Boolean](true)
  def globallyScanEnabled: Boolean = varGloballyScanEnabled.get


  private var isScanningFlag = false
  def isScanning: Boolean = isScanningFlag
  def isScanning_=(value: Boolean) =
    isScanningFlag = value
    if value then ele.dispatchEvent(CustomEvent("scan-started", (), true))
    else ele.dispatchEvent(CustomEvent("scan-ended", (), true))
    adaptPatientSearch()
    eCloseButton.enable(!isScanning)

  private var isUploadingFlag = false
  def isUploading: Boolean = isUploadingFlag
  def isUploading_=(value: Boolean): Unit =
    isUploadingFlag = value
    adaptPatientSearch()
    eCloseButton.enable(!isUploading)

  def adaptPatientSearch(): Unit =
    patientSearch.enable(!(isScanning || isUploading))

  //varGloballyScanEnabled.addCallback(_ => adaptScanButton())
  varPatient.addCallback(_ => adaptScanButton())
  def adaptScanButton(): Unit =
    val enable =
      globallyScanEnabled && selectedScanner.isDefined && patient.isDefined
    scanProgress.enableScan(enable)

  def updateUploadButton(): Unit =
    val enable = scannedItems.hasUnUploadedImage && !scannedItems.isUploading
    eUploadButton.enable(enable)

  private def canStartScan: Boolean =
    selectedScanner.isDefined && patient.isDefined

  private def onScanFileAdd(savedFile: String): Unit =
    scannedItems.add(savedFile)

  private def onUploadClick(): Unit =
    eUploadButton.enable(false)
    scannedItems
      .upload()
      .onComplete(r => {
        r match {
          case Success(_)  => ()
          case Failure(ex) => System.err.println(ex.getMessage)
        }
        updateUploadButton()
      })

  private def onCloseClick(): Unit =
    def doClose(): Unit =
      val parent = ele.parentNode
      ele.remove()
      parent.dispatchEvent(CustomEvent("scan-box-close", (), true))
      scannedItems.deleteSavedFiles().onComplete {
        case Success(_)  => ()
        case Failure(ex) => System.err.println(ex.getMessage)
      }
    if scannedItems.hasUnUploadedImage then
      ShowMessage.confirm("アップロードされていない画像がありますが、このまま閉じますか？")(doClose _)
    else doClose()

  private def onUpload(done: Boolean): Unit =
    if done then eCloseButton.innerText = "閉じる"
    else eCloseButton.innerText = "キャンセル"

object ScanBox:
  val cssClassName: String = "scan-box"
  val defaultScanType: String = "image"
  def makeTimeStamp(): String =
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

class ScannedItems(scanBox: ScanBox):
  var items: List[ScannedItem] = List.empty
  val ele = div()
  def add(savedFile: String): Future[Unit] =
    val item = new ScannedItem(
      scanBox,
      savedFile,
      scanBox.patient,
      scanBox.selectedScanType,
      items.size + 1,
      items.size + 1
    )
    for
      _ <-
        if items.size == 1 then items(0).adaptToTotalChanged(2)
        else Future.successful(())
    yield
      items = items :+ item
      ele(item.ele)
      scanBox.updateUploadButton()
  def isUploading: Boolean =
    items.find(_.isUploading).isDefined
  def hasUnUploadedImage: Boolean =
    items.find(!_.isUploaded).isDefined
  def upload(): Future[Unit] =
    scanBox.patientSearch.ele(displayNone)
    items.foreach(_.disableEdit())
    uploadItems(items)
  def deleteSavedFiles(): Future[Unit] =
    Future.sequence(items.map(_.deleteSavedFile())).map(_ => ())
  private def uploadItems(items: List[ScannedItem]): Future[Unit] =
    items match {
      case Nil => Future.successful(())
      case h :: t =>
        h.ensureUploaded().flatMap(_ => uploadItems(t))
    }
