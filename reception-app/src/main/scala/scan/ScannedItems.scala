package dev.myclinic.scala.web.reception.scan

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

class ScannedItems(ui: ScannedItems.UI):
  val ele = ui.ele
  var items: List[ScannedItem] = List.empty

  def add(savedFile: String, patientId: Int, uploadFile: String): Future[Unit] =
    val item = ScannedItem(savedFile, patientId, uploadFile)
    items = items :+ item
    ele(item.ele)
    Future.successful(())

  def numItems: Int = items.size

  def hasUnUploadedImage: Boolean =
    items.find(!_.isUploaded).isDefined

  def upload: Future[Unit] = uploadAll(items)

  private def uploadAll(items: List[ScannedItem]): Future[Unit] =
    items match {
      case Nil => Future.successful(())
      case hd :: tl => hd.ensureUpload.flatMap(_ => uploadAll(tl))
    }

object ScannedItems:
  class UI:
    val ele = div

  def createUploadFileName(
      patientId: Int,
      scanType: String,
      timestamp: String,
      index: Int,
      total: Int
  ): String =
    val pat = patientId.toString
    val ser: String = if total <= 1 then "" else s"(${index})"
    s"${pat}-${scanType}-${timestamp}${ser}.jpg"


