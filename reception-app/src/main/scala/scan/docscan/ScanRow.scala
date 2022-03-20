package dev.myclinic.scala.web.reception.scan.docscan

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.FlipFlop
import dev.fujiwara.domq.DelayedCall
import dev.myclinic.scala.webclient.{Api, global}
import scala.util.Success
import scala.util.Failure

class ScanRow(using ds: DataSources):
  import ScanRow.*
  val flipFlop = FlipFlop(new Waiting(onStart _), new Busy(onDone _))
  val ele = flipFlop.ele

  def onStart(): Unit =
    flipFlop.flop()

  def onDone(scannedFile: String): Unit =
    flipFlop.flip()
    ds.newlyScannedFile.update(scannedFile)

object ScanRow:
  class Waiting(onStart: () => Unit):
    val ele = div(
      button("スキャン開始", onclick := onStart)
    )

  object Waiting:
    given ElementProvider[Waiting] = _.ele

  class Busy(onDone: String => Unit)(using ds: DataSources):
    import Busy.*
    val prog = span
    val ele = div("スキャン中...", prog)

    def progress(c: Double, t: Double): Unit =
      val pct = (c/t*100).toInt
      prog(innerText := s"${pct}%")

    def onError(ex: Throwable): Unit =
      System.err.println(ex.getMessage)

    def init(): Unit =
      prog(clear)
      progress(0, 10)

    def scan(): Unit = 
      init()
      ds.scanner.data.foreach(scanner => 
        doMockScan(scanner.deviceId, progress _, ds.resolution.data, onDone, onError)
      )

  object Busy:
    given ElementProvider[Busy] = _.ele
    given EventAcceptor[Busy, "activate", Unit] = (t: Busy, e: Unit) => t.scan()

    def doScan(
        deviceId: String,
        progress: (Double, Double) => Unit,
        resolution: Int = 100,
        cb: String => Unit,
        errCb: Throwable => Unit
    ): Unit =
      Api.scan(deviceId, progress, resolution).onComplete {
        case Success(saved) => cb(saved)
        case Failure(ex) => errCb(ex)
      }

    private var mockSerial: Int = 1

    def doMockScan(
        deviceId: String,
        progress: (Double, Double) => Unit,
        resolution: Int = 100,
        cb: String => Unit,
        errCb: Throwable => Unit
    ): Unit =
      for i <- 1 to 9 do
        DelayedCall.callLater(i*0.2, () => progress(i, 10))
      val s = s"file://scanned-file-${mockSerial}.jpeg"
      mockSerial += 1
      DelayedCall.callLater(10*0.2, () => cb(s))
      

