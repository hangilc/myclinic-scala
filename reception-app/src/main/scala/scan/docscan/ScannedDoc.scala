package dev.myclinic.scala.web.reception.scan.docscan

import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.all.{*, given}
import java.time.LocalDateTime
import dev.fujiwara.domq.LocalDataSource
import dev.fujiwara.domq.MultiPanel
import dev.fujiwara.domq.MultiPanel.given
import scala.concurrent.Future
import dev.myclinic.scala.webclient.{Api, global}
import scala.util.Success
import scala.util.Failure
import scala.concurrent.Promise

class ScannedDoc(scannedFile: String, origIndex: Int)(using ds: DataSources):
  import ScannedDoc.*
  val errBox = ErrorBox()
  val slot = Slot(
    state = State.Scanned,
    scannedFile = scannedFile,
    index = origIndex,
    timestamp = makeTimeStamp,
    patientId = ds.patient.data.map(_.patientId),
    scanType = ds.docType.data.getOrElse("image")
  )
  slot.currentError.onUpdate {
    case Some(msg) => errBox.show(msg)
    case None => errBox.hide()
  }
  val mp = new MultiPanel(new DispPanel(slot))
  val ele = div(
    mp.ele,
    errBox.ele
  )
  ds.patient.onUpdate(patient => changePatient(patient.map(_.patientId)))
  mp.switchTo("disp")

  def getState: State = slot.state
  def changePatient(patientIdOpt: Option[Int]): Unit =
    println(("change patient to: ", patientIdOpt))
    // if patientIdOpt != slot.patientId then
    //   slot.state match {
    //     case State.Scanned =>
    //       slot.uploadFileName = Some(resolveUpload)
    //     case _ => 
    //       slot.onError(Some("患者名の変更ができない状態です。"))
    //       Future.failed(new Exception("Failed to change patient."))
    //   }
    // else Future.successful(())

  // private var uploadFileName: Option[String] = None
  // private var state: State = State.Scanned
  private val api = if ds.mock.data then new MockUploadApi else new RealUploadApi
  // def getUploadFileName = uploadFileName
  // def getState: State = state
  // val ui = new UI
  // val ele = ui.ele

  // def init(index: Int): Unit =
  //   val fn = resolveUploadFileName(index)
  //   uploadFileName = Some(fn)
  //   ui.eUploadFile(innerText := fn)

  def upload(): Unit =
    if slot.state == State.Scanned && slot.patientId.isDefined then
      val patientId = slot.patientId.get
      api.upload(scannedFile, patientId, slot.uploadFileName).onComplete {
        case Success(_) => 
          slot.state = State.Uploaded
          mp.switchTo("disp")
        case Failure(ex) => 
          slot.currentError.update(Some("file upload failed: " + ex.getMessage))
          mp.switchTo("disp")
      }

  private def resolveUploadFileName(index: Int): String =
    createUploadFileName(
      ds.patient.data.map(_.patientId),
      ds.docType.data.getOrElse("image"),
      makeTimeStamp,
      index
    )

object ScannedDoc:
  enum State:
    case Scanned, Uploaded, Rescanning, DeletingUpload, Deleting

  case class Slot(
    var state: State,
    var scannedFile: String,
    var index: Int,
    val timestamp: String,
    var patientId: Option[Int],
    var scanType: String,
    val currentError: LocalDataSource[Option[String]] = LocalDataSource[Option[String]](None)
  ):
    var uploadFileName: String = resolveUploadFileName()

    def updateUploadFileName(): Unit =
      uploadFileName = resolveUploadFileName()

    def resolveUploadFileName(): String =
      createUploadFileName(
        patientId,
        scanType,
        timestamp,
        index
      )

  class DispPanel(slot: Slot):
    var switchTo: String => Unit = _ => ()
    val ui = new DispPanelUI

    def ele = ui.ele
    def update(): Unit =
      ui.eUploadFile(innerText := slot.uploadFileName)
      slot.currentError.data match {
        case Some(_) => showFailureIcon()
        case None => slot.state match {
          case State.Uploaded => showUploadedIcon()
          case _ => ()
        }
      }
    private def showUploadedIcon(): Unit =
      ui.eIconWrapper(
        clear,
        children := List(Icons.check(stroke := "green"))
      )

    private def showFailureIcon(): Unit =
      ui.eIconWrapper(
        clear,
        children := List(Icons.x(stroke := "red"))
      )

  object DispPanel:
    given ElementProvider[DispPanel] = _.ele
    given IdProvider[DispPanel, String] = _ => "disp"
    given GeneralTriggerDataProvider[DispPanel, String, "switch-to"] =
      (panel, handler) => panel.switchTo = handler
    given EventAcceptor[DispPanel, Unit, "activate"] =
      (panel, _) => panel.update()

  class DispPanelUI:
    val eIconWrapper: HTMLElement = div
    val eUploadFile: HTMLElement = span
    val ePreview: HTMLElement = div
    val ePreviewLink: HTMLElement = a
    val eRescanLink: HTMLElement = a
    val eDeleteLink: HTMLElement = a
    val ePreviewImageWrapper = div
    val eClosePreviewButton = button
    val eScanProgress = span
    val eErrorBox = ErrorBox()
    val ele = div(
      div(cls := "scanned-item")(
        eIconWrapper(display := "inline-block"),
        eUploadFile(cls := "upload-file-disp"),
        ePreviewLink("表示"),
        eScanProgress(displayNone),
        eRescanLink("再スキャン"),
        eDeleteLink("削除")
      ),
      eErrorBox.ele,
      ePreview(displayNone)(
        ePreviewImageWrapper,
        div(eClosePreviewButton("閉じる"))
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

  def createUploadFileName(
      patientIdOption: Option[Int],
      scanType: String,
      timestamp: String,
      index: Int
  ): String =
    val pat = patientIdOption match {
      case Some(patientId) => patientId.toString
      case None            => "????"
    }
    val ser = String.format("%02d", index)
    val ext = "jpg"
    s"${pat}-$scanType-${timestamp}-${ser}.${ext}"

  trait UploadApi:
    def upload(
        scannedFile: String,
        patientId: Int,
        uploadFileName: String
    ): Future[Unit]

  class RealUploadApi extends UploadApi:
    def upload(
        scannedFile: String,
        patientId: Int,
        uploadFileName: String
    ): Future[Unit] =
      for
        data <- Api.getScannedFile(scannedFile)
        ok <- Api.savePatientImage(patientId, uploadFileName, data)
      yield ()

  class MockUploadApi extends UploadApi:
    def upload(
        scannedFile: String,
        patientId: Int,
        uploadFileName: String
    ): Future[Unit] = 
      println(s"File uploaded ${scannedFile} -> ${patientId}/${uploadFileName}")
      val p = Promise[Unit]()
      import scala.scalajs.js
      js.setTimeout
      p.future

    


