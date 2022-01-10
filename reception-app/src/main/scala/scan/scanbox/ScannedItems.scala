package dev.myclinic.scala.web.reception.scan.scanbox

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Selection, CustomEvent}
import scala.language.implicitConversions
import dev.myclinic.scala.model.ScannerDevice
import org.scalajs.dom.HTMLInputElement
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Success, Failure}
import cats.*
import cats.implicits.*

class ScannedItems(
    ui: ScannedItems.UI,
    timestamp: String,
    scannerRef: () => Option[String]
)(using ScanWorkQueue, ScanBox.Scope):
  val ele = ui.ele
  var items: List[ScannedItem] = List.empty
  def size: Int = items.size

  def add(
      savedFile: String,
      patientId: Option[Int],
      scanType: String
  ): Future[Unit] =
    val item = ScannedItem(
      savedFile,
      patientId,
      scanType,
      timestamp,
      items.size + 1,
      items.size + 1
    )
    item.onDeletedCallbacks.add(index => onItemDeleted(index - 1))
    for 
      _ <- if items.size == 1 then items(0).adjust(newTotal = 2) else Future.successful(())
    yield
      items = items :+ item
      ele(item.ele)

  def numItems: Int = items.size

  def hasUnUploadedImage: Boolean =
    items.find(!_.isUploaded).isDefined
  
  def upload: Future[Unit] = uploadAll(items)

  private def uploadAll(items: List[ScannedItem]): Future[Unit] =
    items match {
      case Nil      => Future.successful(())                          
      case hd :: tl => hd.ensureUpload.flatMap(_ => uploadAll(tl))
    }

  def deleteSavedFiles: Future[Unit] =
    items.map(_.deleteSavedFile).sequence_

  private def onItemDeleted(i: Int): Future[Unit] =
    val total = items.size - 1
    List.from(items.size - 1 until i by -1)
      .map(j => items(j).adjust(newIndex = j, newTotal = total))
      .sequence_
      .map(_ => 
        items = items.patch(i, List.empty, 1)
        if items.size == 1 then items(0).adjust(newIndex = 1, newTotal = 1)
      )

  def adjustToPatientChanged(newPatientId: Option[Int]): Future[Unit] =
    items.map(_.adjust(newPatientId = newPatientId)).sequence_

  def adjustToScanTypeChanged(newScanType: String): Future[Unit] =
    items.map(_.adjust(newScanType = newScanType)).sequence_

  def adapt(patientId: Option[Int], deviceId: Option[String]): Unit =
    items.foreach(_.adapt(patientId, deviceId))

object ScannedItems:
  class UI:
    val ele = div

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