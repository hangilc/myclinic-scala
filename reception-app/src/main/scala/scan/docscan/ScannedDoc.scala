package dev.myclinic.scala.web.reception.scan.docscan

import org.scalajs.dom.{HTMLElement, HTMLImageElement}
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
import dev.fujiwara.domq.DataImage
import dev.myclinic.scala.model.ScannerDevice
import scala.language.implicitConversions

class ScannedDoc(scannedFile: String, origIndex: Int)(using ds: DataSources):
  import ScannedDoc.*
  val errBox = ErrorBox()
  given slot: Slot = Slot(
    state = State.Scanned,
    scannedFile = scannedFile,
    index = origIndex,
    timestamp = makeTimeStamp,
    patientId = ds.patient.data.map(_.patientId),
    docType = ds.docType.data.getOrElse("image"),
    api = if ds.mock.data then new MockDocApi else new RealDocApi
  )
  slot.currentError.onUpdate {
    case Some(msg) => errBox.show(msg)
    case None      => errBox.hide()
  }
  val mp = new MultiPanel(new DispPanel, new UploadPanel, new RescanPanel)
  val ele = div(
    mp.ele,
    errBox.ele
  )
  ds.patient.onUpdate(patient => changePatient(patient.map(_.patientId)))
  ds.docType.onUpdate(_ => changeDocType())
  mp.switchTo("disp")

  def getState: State = slot.state
  def isUploaded: Boolean = getState == State.Uploaded
  def getIndex: Int = slot.index
  def changePatient(patientIdOpt: Option[Int]): Unit =
    if patientIdOpt != slot.patientId then
      slot.state match {
        case State.Scanned =>
          slot.updateUploadFileName()
          mp.switchTo("disp")
        case _ =>
          slot.currentError.update(Some("患者名の変更ができない状態です。"))
      }
  def changeDocType(): Unit =
    if slot.state == State.Scanned then
      slot.docType = resolveDocType(ds.docType.data)
      slot.updateUploadFileName()
      mp.switchTo("disp")
    else slot.currentError.update(Some("文書の種類の変更ができない状態です。"))

  def upload(): Unit = mp.switchTo("upload")

  def dispose(): Future[Unit] =
    val docApi = DocApi(ds)
    for
      _ <- docApi.deleteScannedFile(slot.scannedFile)
      _ <-
        if slot.state == State.Uploaded then
          docApi.deleteUploadedFile(slot.patientId.get, slot.uploadFileName)
        else Future.successful(())
    yield ele.remove()

  def swapWith(other: ScannedDoc): Future[Unit] =
    import State.*
    (getState, other.getState) match {
      case (Scanned, Scanned) =>
        val myIndex = getIndex
        val otherIndex = other.getIndex
        slot.index = otherIndex
        slot.updateUploadFileName()
        other.slot.index = myIndex
        other.slot.updateUploadFileName()
        mp.switchTo("disp")
        other.mp.switchTo("disp")
        Future.successful(())
      case _ => Future.failed(new Exception("Swap operation not allowed."))
    }

  private def resolveDocType(docTypeOpt: Option[String]): String =
    docTypeOpt.getOrElse("image")

  private def resolveUploadFileName(index: Int): String =
    createUploadFileName(
      ds.patient.data.map(_.patientId),
      resolveDocType(ds.docType.data),
      makeTimeStamp,
      index
    )

object ScannedDoc:
  enum State:
    case Scanned, Uploading, Uploaded, Rescanning, DeletingUpload, Deleting

  case class Slot(
      var state: State,
      var scannedFile: String,
      var index: Int,
      val timestamp: String,
      var patientId: Option[Int],
      var docType: String,
      val currentError: LocalDataSource[Option[String]] =
        LocalDataSource[Option[String]](None),
      val api: DocApi
  ):
    var uploadFileName: String = resolveUploadFileName()

    def updateUploadFileName(): Unit =
      uploadFileName = resolveUploadFileName()

    def resolveUploadFileName(): String =
      createUploadFileName(
        patientId,
        docType,
        timestamp,
        index
      )

  class DispPanel(using slot: Slot, ds: DataSources):
    var switchTo: String => Unit = _ => ()
    val ui = new DispPanelUI
    ui.ePreviewLink(onclick := (onPreview _))
    ui.eRescanLink(onclick := (onRescan _))
    ui.eDeleteLink(onclick := (() => {
      if slot.state == State.Scanned then ds.reqDelete.update(slot.index)
      else 
        System.err.println(("Cannot Delete", slot.state))
    }))

    def ele = ui.ele

    def update(): Unit =
      ui.eUploadFile(innerText := slot.uploadFileName)
      slot.currentError.data match {
        case Some(_) => showFailureIcon()
        case None =>
          slot.state match {
            case State.Uploaded => showUploadedIcon()
            case _              => ()
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
    private def onPreview(): Unit =
      (for image <- createPreviewImage(slot.scannedFile, "image/jpeg")
      yield
        ui.ePreviewImageWrapper(clear, image)
        ui.showPreview()
      ).onComplete {
        case Success(_)  => ()
        case Failure(ex) => System.err.println(ex.getMessage)
      }

    private def onRescan(): Unit =
      if slot.state == State.Scanned then switchTo("rescan")
      else slot.currentError.update(Some("再スキャンできない状態です。"))

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
        div(eClosePreviewButton("閉じる", onclick := (hidePreview _)))
      )
    )

    def showPreview(): Unit = ePreview(displayDefault)
    def hidePreview(): Unit =
      ePreviewImageWrapper(clear)
      ePreview(displayNone)

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

  class UploadPanel(using slot: Slot):
    val iconWrapper = span
    var switchTo: String => Unit = _ => ()
    val ele = div(
      iconWrapper,
      s"${slot.uploadFileName} をアップロード中..."
    )

    private def showUploadingIcon(): Unit =
      iconWrapper(
        clear,
        children := List(Icons.arrowCircleUp(stroke := "blue"))
      )

    def startUpload(): Unit =
      slot.state match {
        case State.Scanned =>
          slot.state = State.Uploading
          showUploadingIcon()
          slot.patientId.foreach(patientId => {
            slot.api
              .upload(slot.scannedFile, patientId, slot.uploadFileName)
              .onComplete {
                case Success(_) =>
                  slot.state = State.Uploaded
                  switchTo("disp")
                case Failure(ex) =>
                  slot.currentError
                    .update(Some("file upload failed: " + ex.getMessage))
                  slot.state = State.Scanned
                  switchTo("disp")
              }
          })
        case _ => switchTo("disp")
      }

  object UploadPanel:
    given ElementProvider[UploadPanel] = _.ele
    given IdProvider[UploadPanel, String] = _ => "upload"
    given EventAcceptor[UploadPanel, Unit, "activate"] = (t, _) =>
      t.startUpload()
    given GeneralTriggerDataProvider[UploadPanel, String, "switch-to"] =
      (t, handler) => t.switchTo = handler

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

  class RescanPanel(using slot: Slot, ds: DataSources):
    val iconWrapper = span
    var switchTo: String => Unit = _ => ()
    val progSpan = span
    val ele = div(
      iconWrapper,
      s"${slot.uploadFileName}の再スキャン中...",
      progSpan
    )

    def startRescan(): Unit =
      if slot.state == State.Scanned then
        slot.state = State.Rescanning
        val scanner: ScannerDevice = ds.scanner.data.get
        if ScannerList.openScanner(scanner) then
          val api = ScanRow.ScanApi(ds)
          def progress(pct: Double, total: Double): Unit =
            progSpan(innerText := s"${(pct / total * 100).toInt}%")
          def prolog(): Unit =
            ScannerList.closeScanner(scanner)
            slot.state = State.Scanned
            switchTo("disp")
          iconWrapper(clear, Icons.save(stroke := "orange"))
          api.scan(
            scanner.deviceId,
            progress _,
            ds.resolution.data,
            file => {
              prolog()
            },
            err => {
              slot.currentError.update(Some(err.getMessage))
              prolog()
            }
          )

  object RescanPanel:
    given ElementProvider[RescanPanel] = _.ele
    given IdProvider[RescanPanel, String] = _ => "rescan"
    given EventAcceptor[RescanPanel, Unit, "activate"] = (t, _) =>
      t.startRescan()
    given GeneralTriggerDataProvider[RescanPanel, String, "switch-to"] =
      (t, handler) => t.switchTo = handler

  def createPreviewImage(
      savedFile: String,
      mimeType: String
  )(using ds: DataSources): Future[HTMLImageElement] =
    val scanApi = ScanRow.ScanApi(ds)
    for data <- scanApi.getSavedImage(savedFile)
    yield
      val image = DataImage(data, mimeType)
      val scale = 1.5
      image.width = (210 * scale).toInt
      image.height = (297 * scale).toInt
      image

  trait DocApi:
    def upload(
        scannedFile: String,
        patientId: Int,
        uploadFileName: String
    ): Future[Unit]
    def deleteScannedFile(file: String): Future[Unit]
    def renameUploadedFile(
        patientId: Int,
        src: String,
        dst: String
    ): Future[Unit]
    def deleteUploadedFile(patientId: Int, file: String): Future[Unit]
    def swapUploadedFileNames(
        patientId: Int,
        file1: String,
        file2: String
    ): Future[Unit] =
      val save = file1 + "-save"
      for
        _ <- renameUploadedFile(patientId, file1, save)
        _ <- renameUploadedFile(patientId, file2, file1)
        _ <- renameUploadedFile(patientId, save, file2)
      yield ()

  object DocApi:
    def apply(ds: DataSources): DocApi =
      if ds.mock.data then new MockDocApi
      else new RealDocApi

  class RealDocApi extends DocApi:
    def deleteScannedFile(file: String): Future[Unit] =
      Api.deleteScannedFile(file).map(_ => ())
    def upload(
        scannedFile: String,
        patientId: Int,
        uploadFileName: String
    ): Future[Unit] =
      for
        data <- Api.getScannedFile(scannedFile)
        ok <- Api.savePatientImage(patientId, uploadFileName, data)
      yield ()

    def renameUploadedFile(
        patientId: Int,
        src: String,
        dst: String
    ): Future[Unit] =
      Api.renamePatientImage(patientId, src, dst).map(_ => ())

    def deleteUploadedFile(patientId: Int, file: String): Future[Unit] =
      Api.deletePatientImage(patientId: Int, file).map(_ => ())

  class MockDocApi extends DocApi:
    def deleteScannedFile(file: String): Future[Unit] =
      println(("deleting scanned file", file))
      Future.successful(())
    def upload(
        scannedFile: String,
        patientId: Int,
        uploadFileName: String
    ): Future[Unit] =
      println(s"File uploaded ${scannedFile} -> ${patientId}/${uploadFileName}")
      val delaySec = 5
      val p = Promise[Unit]()
      import scala.scalajs.js
      scala.scalajs.js.timers.setTimeout(delaySec * 1000) {
        p.success(())
      }
      p.future

    def renameUploadedFile(
        patientId: Int,
        src: String,
        dst: String
    ): Future[Unit] =
      println((s"rename: ${src} -> ${dst}"))
      Future.successful(())

    def deleteUploadedFile(patientId: Int, file: String): Future[Unit] =
      println((s"delete: ${file}"))
      Future.successful(())
