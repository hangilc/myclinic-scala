package dev.myclinic.scala.web.reception.scan.docscan

import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.all.{*, given}
import java.time.LocalDateTime

class ScannedDoc(scannedFile: String)(using ds: DataSources)

  
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
      index: Int,
      total: Int,
      serialId: Option[Int]
  ): String =
    val pat = patientIdOption match {
      case Some(patientId) => patientId.toString
      case None            => "????"
    }
    val ser: String = if total <= 1 then "" else s"(${index})"
    val base = s"${pat}-${scanType}-${timestamp}${ser}" +
      (serialId match {
        case None => ""
        case Some(id) => s"-${id}"
      })
    val ext = ".jpg"
    base + ext


