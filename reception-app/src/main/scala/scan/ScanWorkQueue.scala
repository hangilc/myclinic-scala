package dev.myclinic.scala.web.reception.scan

import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global

case class TaskTag(
    isScanning: Option[String] = None,
    isUploading: Option[Int] = None
)

class ScanWorkQueue:
  type Tag = TaskTag
  private val wq = new WorkQueue[Tag]

  def scan(
      deviceId: String,
      progress: (Double, Double) => Unit,
      resolution: Int,
      onEnd: String => Unit
  ): Boolean =
    val isBusy = wq.scan(t => t.tag.isScanning == Some(deviceId)).size == 0
    if isBusy then false
    else
      val tag = TaskTag(isScanning = Some(deviceId))
      val task = Task(
        tag,
        () =>
          for savedFile <- Api.scan(deviceId, progress, resolution)
          yield onEnd(savedFile)
      )
      true
