package dev.myclinic.scala.web.reception.scan.scanbox

import org.scalajs.dom.{HTMLElement, URL, HTMLImageElement, Event}
import org.scalajs.dom.{Blob, BlobPropertyBag}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, ShowMessage}
import dev.myclinic.scala.webclient.Api
import scala.concurrent.Future
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import scala.util.{Success, Failure}
import scala.scalajs.js
import dev.myclinic.scala.model.Patient
import org.scalajs.dom.MouseEvent
import dev.myclinic.scala.web.reception.scan.FutureCallbacks

object ScannedItem:
  class UI:
    val eIconWrapper: HTMLElement = div
    val eUploadFile: HTMLElement = span
    val ePreview: HTMLElement = div
    val ePreviewLink: HTMLElement = a
    val eRescanLink: HTMLElement = a
    val eDeleteLink: HTMLElement = a
    val ePreviewImageWrapper = div
    val eClosePreviewButton = button
    val eScanProgress = span
    val ele = div(
      div(cls := "scanned-item")(
        eIconWrapper(display := "inline-block"),
        eUploadFile(cls := "upload-file-disp"),
        ePreviewLink("表示"),
        eScanProgress(displayNone),
        eRescanLink("再スキャン"),
        eDeleteLink("削除")
      ),
      ePreview(displayNone)(
        ePreviewImageWrapper,
        div(eClosePreviewButton("閉じる"))
      )
    )

  def apply(
      savedFile: String,
      patientId: Option[Int],
      scanType: String,
      timestamp: String,
      index: Int,
      total: Int
  )(using ScanWorkQueue, ScanBox.Scope): ScannedItem =
    val ui = new UI
    new ScannedItem(
      ui,
      savedFile,
      patientId,
      scanType,
      timestamp,
      index,
      total
    )

  var serialStore = 1
  def serial: Int =
    val ser = serialStore
    serialStore += 1
    ser

class ScannedItem(
    val ui: ScannedItem.UI,
    var savedFile: String,
    var patientId: Option[Int],
    var scanType: String,
    timestamp: String,
    var index: Int,
    var total: Int
)(using queue: ScanWorkQueue, scope: ScanBox.Scope):
  val onDeletedCallbacks = new FutureCallbacks[Int]
  val serialId = ScannedItem.serial
  val ele = ui.ele
  var uploadFile: String = createUploadFile
  ui.eUploadFile.innerText = uploadFile

  private def updateUploadFile: Unit =
    uploadFile = createUploadFile
    ui.eUploadFile.innerText = uploadFile

  private def createUploadFile: String =
    ScannedItems.createUploadFileName(
      patientId,
      scanType,
      timestamp,
      index,
      total,
      None //Some(serialId)
    )

  private var uploadedFlag: Boolean = false
  def isUploaded: Boolean = uploadedFlag

  private def showSuccessIcon(): Unit =
    ui.eIconWrapper.setChildren(
      List(Icons.check(stroke := "green"))
    )

  private def showFailureIcon(): Unit =
    ui.eIconWrapper.setChildren(
      List(Icons.x(stroke := "red"))
    )

  def upload: Future[Unit] =
    patientId match {
      case None => Future.failed(new RuntimeException("患者が選択されていません。"))
      case Some(patientId) =>
        for
          data <- Api.getScannedFile(savedFile)
          ok <- Api.savePatientImage(patientId, uploadFile, data)
        yield ()
    } andThen {
      case Success(_) =>
        uploadedFlag = true
        showSuccessIcon()
      case Failure(ex) =>
        showFailureIcon()
    }

  def ensureUpload: Future[Unit] =
    if isUploaded then Future.successful(())
    else upload

  def deleteSavedFile: Future[Unit] =
    Api.deleteScannedFile(savedFile).map(_ => ())

  def previewTask: ScanTask =
    ScanTask(() =>
      for data <- Api.getScannedFile(savedFile)
      yield
        val oURL = URL.createObjectURL(
          new Blob(
            js.Array(data),
            new BlobPropertyBag {
              override val `type`: js.UndefOr[String] = "image/jpeg"
            }
          )
        )
        val image = org.scalajs.dom.document
          .createElement("img")
          .asInstanceOf[HTMLImageElement]
        image.onload = (e: Event) => {
          URL.revokeObjectURL(oURL)
        }
        image.src = oURL
        val scale = 1.5
        image.width = (210 * scale).toInt
        image.height = (297 * scale).toInt
        ui.ePreviewImageWrapper.setChild(image)
        ui.ePreview(displayDefault)
    )

  ui.ePreviewLink(
    onclick := (_ => queue.append(previewTask))
  )

  ui.eClosePreviewButton(
    onclick := (_ =>
      ui.ePreviewImageWrapper.clear()
      ui.ePreview(displayNone)
    )
  )

  private def cancelUpload: Future[Unit] =
    for _ <- Api.deletePatientImage(patientId.get, uploadFile)
    yield
      uploadedFlag = false
      ui.eIconWrapper.clear()

  def rescanTask(deviceId: String): ScanTask =
    ScanTask(
      () =>
        ui.eRescanLink.hide
        ui.eScanProgress.clear().show
        for
          saved <- Api.scan(
            deviceId,
            ScanBox.reportProgress(ui.eScanProgress),
            scope.resolution
          )
          _ <- Api.deleteScannedFile(savedFile)
          _ = savedFile = saved
          _ <-
            if isUploaded then cancelUpload
            else Future.successful(())
        yield ui.eScanProgress.clear().hide
      ,
      isScanning = Some(deviceId)
    )

  ui.eRescanLink(
    onclick := (_ =>
      scope.selectedScanner match {
        case Some(deviceId) => queue.append(rescanTask(deviceId))
        case None           => ()
      }
    )
  )

  private def ensureRemoteDeleted(): Future[Unit] =
    if isUploaded then
      for _ <- Api.deletePatientImage(patientId.get, uploadFile).map(_ => ())
      yield
        ui.eIconWrapper.clear()
        uploadedFlag = false
    else Future.successful(())

  private def deleteItem(): Future[Unit] =
    for
      _ <- ensureRemoteDeleted()
      _ <- Api.deleteScannedFile(savedFile)
    yield ui.ele.remove()

  private def deleteItemTask: ScanTask =
    ScanTask(() =>
      for
        _ <- deleteItem()
        _ <- onDeletedCallbacks.invoke(index)
      yield ()
    )

  private def onDeleteClick(event: MouseEvent): Unit =
    ShowMessage.confirm("このスキャンを削除しますか？")(() => queue.append(deleteItemTask))

  ui.eDeleteLink(
    onclick := (onDeleteClick _)
  )

  // def adjustToTotalChanged(newTotal: Int): Future[Unit] =
  //   adjustToIndexChanged(index, newTotal)

  // def adjustToSamePatientUploadChanged(newUploadFile: String): Future[Unit] =
  //   if isUploaded then
  //     Api
  //       .renamePatientImage(patientId.get, uploadFile, newUploadFile)
  //       .map(_ => ())
  //   else Future.successful(())

  // def adjustToIndexChanged(newIndex: Int, newTotal: Int): Future[Unit] =
  //   val newUploadFile: String = ScannedItems.createUploadFileName(
  //     patientId,
  //     scanType,
  //     timestamp,
  //     newIndex,
  //     newTotal,
  //     None //Some(serialId)
  //   )
  //   if newUploadFile != uploadFile then
  //     for _ <- adjustToSamePatientUploadChanged(newUploadFile)
  //     yield
  //       uploadFile = newUploadFile
  //       ui.eUploadFile.innerText = newUploadFile
  //       index = newIndex
  //       total = newTotal
  //   else Future.successful(())

  def adjust(
      newPatientId: Option[Int] = patientId,
      newScanType: String = scanType,
      newIndex: Int = index,
      newTotal: Int = total
  ): Future[Unit] =
    val newUploadFile: String = ScannedItems.createUploadFileName(
      newPatientId,
      newScanType,
      timestamp,
      newIndex,
      newTotal,
      None //Some(serialId)
    )
    if newUploadFile != uploadFile then
      for
        _ <-
          if (newPatientId != patientId) then ensureRemoteDeleted()
          else Future.successful(())
        _ <- if isUploaded then
          Api.renamePatientImage(patientId.get, uploadFile, newUploadFile)
          else Future.successful(())
      yield
        uploadFile = newUploadFile
        ui.eUploadFile.innerText = newUploadFile
        patientId = newPatientId
        scanType = newScanType
        index = newIndex
        total = newTotal
    else Future.successful(())

  def adapt(patientId: Option[Int], deviceId: Option[String]): Unit =
    val queueIsEmpty = queue.isEmpty
    val canScan = ScanBox.canScan(patientId, deviceId)
    ui.ePreviewLink.show(queueIsEmpty)
    ui.eRescanLink.show(queueIsEmpty && canScan)
    ui.eDeleteLink.show(queueIsEmpty)
