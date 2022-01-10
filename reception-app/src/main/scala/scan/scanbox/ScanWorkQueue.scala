package dev.myclinic.scala.web.reception.scan.scanbox

import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import dev.myclinic.scala.web.reception.scan.{WorkQueue, WorkQueueTask, Callbacks}

case class ScanTask(
    run: () => Future[Unit],
    isScanning: Option[String] = None
) extends WorkQueueTask

class ScanWorkQueue extends WorkQueue[ScanTask]:
  val pinCallbacks = new Callbacks[Unit]
  override def append(t: ScanTask): Unit =
    val busy = t.isScanning.map(ScanWorkQueue.isScannerBusy(_)).getOrElse(false)
    if !busy then super.append(t)

object ScanWorkQueue:
  private var queueList: List[ScanWorkQueue] = List.empty

  def apply(): ScanWorkQueue =
    val q = new ScanWorkQueue
    q.onStartCallbacks.add(t =>
      q.pinCallbacks.invoke(())
      if t.isScanning.isDefined then
        queueList.find(_ != q).foreach(_.pinCallbacks.invoke(()))
    )
    q.onEndCallbacks.add(tb =>
      q.pinCallbacks.invoke(())
      tb match {
        case (t, _) if t.isScanning.isDefined =>
          queueList.find(_ != q).foreach(_.pinCallbacks.invoke(()))
        case _ => ()
      }
    )
    queueList = queueList :+ q
    q

  def remove(q: ScanWorkQueue): Unit =
    queueList = queueList.filterNot(_ == q)

  private def isUsingScanner(q: ScanWorkQueue, deviceId: String): Boolean =
    q.find(_.isScanning == Some(deviceId)).isDefined

  def isScannerBusy(deviceId: String): Boolean =
    queueList.find(isUsingScanner(_, deviceId)).isDefined
