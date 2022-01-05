package dev.myclinic.scala.web.reception.scan

import org.scalajs.dom.{HTMLElement, URL, HTMLImageElement, Event}
import org.scalajs.dom.{Blob, BlobPropertyBag}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, ShowMessage}
import dev.myclinic.scala.webclient.Api
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import scala.scalajs.js
import dev.myclinic.scala.model.Patient

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
        eUploadFile,
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

class ScannedItem(
    val ui: ScannedItem.UI,
    var savedFile: String,
    patientId: Option[Int],
    scanType: String,
    timestamp: String,
    index: Int,
    var total: Int
)(using queue: ScanWorkQueue, scope: ScanBox.Scope):
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
      total
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
              override var `type`: js.UndefOr[String] = "image/jpeg"
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
        image.width = (210 * 1.5).toInt
        image.height = (297 * 1.5).toInt
        ui.ePreviewImageWrapper.setChildren(image)
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
        yield ()
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

  def adjustToTotalChanged(newTotal: Int): Future[Unit] =
    val prevTotal = total
    total = newTotal
    (index, prevTotal, newTotal) match {
      case (1, 1, 2) | (1, 2, 1) =>
        val prevUploadFile = uploadFile
        updateUploadFile
        if isUploaded then
          Api
            .renamePatientImage(patientId.get, prevUploadFile, uploadFile)
            .map(_ => ())
        else Future.successful(())
      case _ => Future.successful(())
    }

  def adapt(patientId: Option[Int], deviceId: Option[String]): Unit =
    val queueIsEmpty = queue.isEmpty
    val canScan = ScanBox.canScan(patientId, deviceId)
    ui.ePreviewLink.show(queueIsEmpty)
    ui.eRescanLink.show(queueIsEmpty && canScan)
    ui.eDeleteLink.show(queueIsEmpty)

//   def deleteSavedFile(): Future[Unit] =
//     Api.deleteScannedFile(savedFile).map(_ => ())

//   def adaptToTotalChanged(newTotal: Int): Future[Unit] =
//     if index == 1 && total == 1 then
//       val src = eUploadFile.innerText
//       total = newTotal
//       val dst = uploadFileName
//       for
//         _ <-
//           if isUploaded then
//             Api.renamePatientImage(context.patient.value.get.patientId, src, dst)
//           else Future.successful(())
//       yield eUploadFile.innerText = dst
//     else
//       Future.successful(())

//   def disableEdit(): Unit =
//     eRescanLink(displayNone)
//     eDeleteLink(displayNone)

//   private def showSuccessIcon(): Unit =
//     eIconWrapper.setChildren(
//       List(Icons.check(stroke := "green"))
//     )

//   private def showFailureIcon(): Unit =
//     eIconWrapper.setChildren(
//       List(Icons.x(stroke := "red"))
//     )

//   private def upload(): Future[Unit] =
//     context.patient.value.map(_.patientId).fold(
//       Future.failed(new RuntimeException("Patient not specified."))
//     ) { patientId =>
//       val f =
//         for
//           data <- Api.getScannedFile(savedFile)
//           ok <- Api.savePatientImage(patientId, uploadFileName, data)
//         yield ()
//       f.transform[Unit] {
//         case Success(_) =>
//           isUploadedFlag = true
//           showSuccessIcon()
//           Success(())
//         case Failure(ex) =>
//           showFailureIcon()
//           Failure(ex)
//       }
//     }

// // class ScannedItem(
// //     var savedFile: String,
// //     timestamp: String
// // ):
// //   var isUploaded: Boolean = false
// //   var patient: Option[Patient] = None
// //   var index: Int = 1
// //   var total: Int = 1
// //   var kind: String = "image"
// //   val eUploadFile: HTMLElement = span()
// //   val eIconWrapper: HTMLElement = div()
// //   val ePreview: HTMLElement = div()
// //   val ele = div(
// //     div(cls := "scanned-item")(
// //       eIconWrapper(display := "inline-block"),
// //       eUploadFile(innerText := uploadFileName),
// //       a("表示", onclick := (onShow _)),
// //       a("再スキャン"),
// //       a("削除", onclick := (onDeleteClick _))
// //     ),
// //     ePreview(displayNone)(
// //       div(button("閉じる", onclick := (onClosePreview _)))
// //     )
// //   )

// //   def updateUI(state: ScanBoxState, sIndex: Int, sTotal: Int): Unit =
// //     val sPatient = state.patient
// //     val skind = state.kind
// //     eUploadFile.innerText = uploadFileName

// //   private def uploadFileName: String =
// //     val pat = patient match {
// //       case Some(p) => p.patientId.toString
// //       case None     => "????"
// //     }
// //     val ser: String = if total <= 1 then "" else s"(${index})"
// //     s"${pat}-${kind}-${timestamp}${ser}.jpg"

// //   private def showSuccessIcon(): Unit =
// //     eIconWrapper.setChildren(
// //       List(Icons.check(stroke := "green"))
// //     )

// //   private def showFailureIcon(): Unit =
// //     eIconWrapper.setChildren(
// //       List(Icons.x(stroke := "red"))
// //     )

// //   private def onClosePreview(): Unit =
// //     ePreview.qSelectorAll("img").foreach(_.remove())
// //     ePreview(displayNone)

// //   private def onShow(): Unit =
// //     val f =
// //       for data <- Api.getScannedFile(savedFile)
// //       yield
// //         val oURL = URL.createObjectURL(
// //           new Blob(js.Array(data), BlobPropertyBag("image/jpeg"))
// //         )
// //         val image = org.scalajs.dom.document
// //           .createElement("img")
// //           .asInstanceOf[HTMLImageElement]
// //         image.onload = (e: Event) => {
// //           URL.revokeObjectURL(oURL)
// //         }
// //         image.src = oURL
// //         val scale = 1.5
// //         image.width = (210 * 1.5).toInt
// //         image.height = (297 * 1.5).toInt
// //         ePreview.prepend(image)
// //         ePreview(displayDefault)
// //     f.onComplete {
// //       case Success(_)  => ()
// //       case Failure(ex) => System.err.println(ex.getMessage)
// //     }

// //   private def doDelete(): Unit =
// //     val f =
// //       for
// //         _ <- Api.deleteScannedFile(savedFile)
// //       yield ()
// //     f.onComplete {
// //       case Success(_) => ()
// //       case Failure(ex) => System.err.println(ex.getMessage)
// //     }

// //   private def onDeleteClick(): Unit =
// //     ShowMessage.confirm("この画像を削除していいですか？")(doDelete _)

// //   private def upload(): Future[Unit] =
// //     patientIdRef().fold(
// //       Future.failed(new RuntimeException("Patient not specified."))
// //     ) { patientId =>
// //       val f =
// //         for
// //           data <- Api.getScannedFile(savedFile)
// //           ok <- Api.savePatientImage(patientId, uploadFileName, data)
// //         yield ()
// //       f.transform[Unit] {
// //         case Success(_) =>
// //           isUploaded = true
// //           showSuccessIcon()
// //           Success(())
// //         case Failure(ex) =>
// //           showFailureIcon()
// //           Failure(ex)
// //       }
// //     }

// //   def ensureUploaded(): Future[Unit] =
// //     if isUploaded then Future.successful(())
// //     else upload()
