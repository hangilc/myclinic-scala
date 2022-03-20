package dev.myclinic.scala.web.reception.scan.docscan

import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.all.{*, given}
import java.time.LocalDateTime
import dev.fujiwara.domq.LocalDataSource
import scala.concurrent.Future
import dev.myclinic.scala.webclient.{Api, global}

class ScannedDoc(scannedFile: String, val index: LocalDataSource[Int])(using ds: DataSources):
  import ScannedDoc.*
  private var uploadFileName = updatedUploadFileName
  private val api = new MockUploadApi
  def getUploadFileName = uploadFileName
  val ui = new UI
  ui.eUploadFile(uploadFileName)
  val ele = ui.ele

  def upload(): Unit =
    ds.patient.data.map(_.patientId).foreach(patientId =>
      api.upload(scannedFile, patientId, uploadFileName)
    )

  def updatedUploadFileName: String =
    createUploadFileName(
      ds.patient.data.map(_.patientId),
      ds.docType.data.getOrElse("image"),
      makeTimeStamp,
      index.data
    )

object ScannedDoc:
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
      Future.successful(())

    


