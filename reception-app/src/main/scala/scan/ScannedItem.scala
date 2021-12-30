package dev.myclinic.scala.web.reception.scan

import org.scalajs.dom.raw.{HTMLElement, URL, HTMLImageElement, Event}
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

class ScannedItem(
    var savedFile: String,
    timestamp: String
):
  var isUploaded: Boolean = false
  var patient: Option[Patient] = None
  var index: Int = 1
  var total: Int = 1
  var kind: String = "image"
  val eUploadFile: HTMLElement = span()
  val eIconWrapper: HTMLElement = div()
  val ePreview: HTMLElement = div()
  val ele = div(
    div(cls := "scanned-item")(
      eIconWrapper(display := "inline-block"),
      eUploadFile(innerText := uploadFileName),
      a("表示", onclick := (onShow _)),
      a("再スキャン"),
      a("削除", onclick := (onDeleteClick _))
    ),
    ePreview(displayNone)(
      div(button("閉じる", onclick := (onClosePreview _)))
    )
  )

  def updateUI(state: ScanBoxState, sIndex: Int, sTotal: Int): Unit =
    val sPatient = state.patient
    val skind = state.kind
    eUploadFile.innerText = uploadFileName

  private def uploadFileName: String =
    val pat = patient match {
      case Some(p) => p.patientId.toString
      case None     => "????"
    }
    val ser: String = if total <= 1 then "" else s"(${index})"
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

  private def doDelete(): Unit =
    val f =
      for 
        _ <- Api.deleteScannedFile(savedFile)
      yield ()
    f.onComplete {
      case Success(_) => ()
      case Failure(ex) => System.err.println(ex.getMessage)
    }

  private def onDeleteClick(): Unit =
    ShowMessage.confirm("この画像を削除していいですか？")(doDelete _)

  private def upload(): Future[Unit] =
    patientIdRef().fold(
      Future.failed(new RuntimeException("Patient not specified."))
    ) { patientId =>
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
    }

  def ensureUploaded(): Future[Unit] =
    if isUploaded then Future.successful(())
    else upload()
