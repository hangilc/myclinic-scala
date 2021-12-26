package dev.myclinic.scala.web.reception.scan

import org.scalajs.dom.raw.{HTMLElement, URL, HTMLImageElement, Event}
import org.scalajs.dom.{Blob, BlobPropertyBag}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons}
import dev.myclinic.scala.webclient.Api
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import scala.scalajs.js

class ScannedItem(
    var savedFile: String,
    timestamp: String,
    index: => Option[Int],
    patientId: => Option[Int],
    kind: => String
):
  var isUploaded: Boolean = false
  val eUploadFile: HTMLElement = span()
  val eIconWrapper: HTMLElement = div()
  val ePreview: HTMLElement = div()
  val ele = div(
    div(cls := "scanned-item")(
      eIconWrapper(display := "inline-block"),
      eUploadFile(innerText := uploadFileName),
      a("表示", onclick := (onShow _)),
      a("再スキャン"),
      a("削除")
    ),
    ePreview(displayNone)(
      div(button("閉じる", onclick := (onClosePreview _)))
    )
  )

  def updateUI(): Unit =
    eUploadFile.innerText = uploadFileName

  private def uploadFileName: String =
    val pat = patientId match {
      case Some(id) => id.toString
      case None    => "????"
    }
    val ser = index match {
      case Some(i) => s"(${i})"
      case None    => ""
    }
    s"${pat}-${kind}-${timestamp}${ser}.jpg"

  private def showSuccessIcon(): Unit =
    eIconWrapper.setChildren(
      List(Icons.check(stroke := "green"))
    )

  private def showFailureIcon(): Unit =
    eIconWrapper.setChildren(
      List(Icons.x(stroke := "red"))
    )

  private def onClosePreview(): Unit =
    ePreview.qSelectorAll("img").foreach(_.remove())
    ePreview(displayNone)

  private def onShow(): Unit =
    val f =
      for data <- Api.getScannedFile(savedFile)
      yield
        val oURL = URL.createObjectURL(
          new Blob(js.Array(data), BlobPropertyBag("image/jpeg"))
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
        ePreview.prepend(image)
        ePreview(displayDefault)
    f.onComplete {
      case Success(_)  => ()
      case Failure(ex) => System.err.println(ex.getMessage)
    }

  def upload(patientId: Int): Future[Unit] =
    val f =
      for
        data <- Api.getScannedFile(savedFile)
        ok <- Api.savePatientImage(patientId, uploadFileName, data)
      yield ()
    f.transform[Unit] {
      case Success(_) =>
        isUploaded = true
        showSuccessIcon()
        Success(())
      case Failure(ex) =>
        showFailureIcon()
        Failure(ex)
    }

  def ensureUploaded(patientId: Int): Future[Unit] =
    if isUploaded then Future.successful(())
    else upload(patientId)
