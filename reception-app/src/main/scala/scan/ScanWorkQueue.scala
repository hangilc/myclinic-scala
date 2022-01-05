package dev.myclinic.scala.web.reception.scan

import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class ScanTask(
    run: () => Future[Unit],
    isScanning: Option[String] = None
) extends WorkQueueTask

class ScanWorkQueue extends WorkQueue[ScanTask]:
  override def append(t: ScanTask): Unit =
    val busy = t.isScanning.map(ScanWorkQueue.isScannerBusy(_)).getOrElse(false)
    if !busy then super.append(t)

object ScanWorkQueue:
  private var queueList: List[ScanWorkQueue] = List.empty

  def apply(): ScanWorkQueue =
    val q = new ScanWorkQueue
    queueList = queueList :+ q
    q

  def remove(q: ScanWorkQueue): Unit =
    queueList = queueList.filterNot(_ == q)

  private def isUsingScanner(q: ScanWorkQueue, deviceId: String): Boolean =
    q.scan(_.isScanning == Some(deviceId)).size > 0

  def isScannerBusy(deviceId: String): Boolean =
    queueList.find(isUsingScanner(_, deviceId)).isDefined
