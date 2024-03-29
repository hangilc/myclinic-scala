package dev.myclinic.scala.web.reception.scan.docscan

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.FlipFlop
import dev.fujiwara.domq.DelayedCall
import dev.myclinic.scala.webclient.{Api, global}
import scala.util.Success
import scala.util.Failure
import dev.myclinic.scala.model.ScannerDevice
import scala.concurrent.Future
import scala.scalajs.js.typedarray.ArrayBuffer
import scala.language.implicitConversions

class ScanRow(using ds: DataSources):
  import ScanRow.*
  val api = ScanApi(ds)
  val waitingComp = new Waiting(onStart _)
  val busyComp = new Busy(onDone _, onError _, api)
  val flipFlop = FlipFlop(waitingComp, busyComp)
  val ele = flipFlop.ele
  ds.scanner.onUpdate(updateScanButton(_, ScannerList.getBusyScanners))
  ScannerList.onBusyScannersChange(updateScanButton(ds.scanner.data, _))
  updateScanButton(
    ds.scanner.data,
    ScannerList.getBusyScanners
  )

  def updateScanButton(
      scannerOpt: Option[ScannerDevice],
      busyScanners: Set[ScannerDevice]
  ): Unit =
    val scanEnabled = scannerOpt.fold(false)(scanner => {
      !busyScanners.contains(scanner)
    })
    waitingComp.scanButton(enabled := scanEnabled)

  def onStart(): Unit =
    flipFlop.flop()

  def onDone(scannedFile: String): Unit =
    flipFlop.flip()
    ds.newlyScannedFile.update(scannedFile)

  def onError(ex: Throwable): Unit =
    System.err.println(ex.getMessage)
    flipFlop.flip()

object ScanRow:
  class Waiting(onStart: () => Unit):
    val scanButton = button
    val ele = div(
      scanButton("スキャン開始", onclick := onStart)
    )

  object Waiting:
    given ElementProvider[Waiting] = _.ele

  class Busy(onDone: String => Unit, onError: Throwable => Unit, api: ScanApi)(
      using ds: DataSources
  ):
    import Busy.*
    val prog = span
    val ele = div("スキャン中...", prog)

    def progress(c: Double, t: Double): Unit =
      val pct = (c / t * 100).toInt
      prog(innerText := s"${pct}%")

    def init(): Unit =
      prog(clear)
      progress(0, 10)

    def scan(): Unit =
      ds.scanner.data.foreach(scanner =>
        if ScannerList.openScanner(scanner) then
          init()
          api.scan(
            scanner.deviceId,
            progress _,
            ds.resolution.data,
            scannedFile => {
              ScannerList.closeScanner(scanner)
              onDone(scannedFile)
            },
            ex => {
              ScannerList.closeScanner(scanner)
              onError(ex)
            }
          )
        else onError(new Exception("スキャナーは使用中です。"))
      )

  object Busy:
    given ElementProvider[Busy] = _.ele
    given EventAcceptor[Busy, Unit, "activate"] = (t: Busy, e: Unit) => t.scan()

  trait ScanApi:
    def scan(
        deviceId: String,
        progress: (Double, Double) => Unit,
        resolution: Int = 100,
        cb: String => Unit,
        errCb: Throwable => Unit
    ): Unit
    def getSavedImage(file: String): Future[ArrayBuffer]

  object ScanApi:
    def apply(ds: DataSources): ScanApi =
      if ds.mock.data == true then new MockApi
      else new RealApi

  class RealApi extends ScanApi:
    def scan(
        deviceId: String,
        progress: (Double, Double) => Unit,
        resolution: Int = 100,
        cb: String => Unit,
        errCb: Throwable => Unit
    ): Unit =
      Api.scan(deviceId, progress, resolution).onComplete {
        case Success(saved) => cb(saved)
        case Failure(ex)    => errCb(ex)
      }

    def getSavedImage(file: String): Future[ArrayBuffer] =
      Api.getScannedFile(file)

  class MockApi extends ScanApi:
    import MockApi.*
    def scan(
        deviceId: String,
        progress: (Double, Double) => Unit,
        resolution: Int = 100,
        cb: String => Unit,
        errCb: Throwable => Unit
    ): Unit =
      val duration = 3
      for i <- 1 to 9 do DelayedCall.callLater(i * duration / 10, () => progress(i, 10))
      val s = s"file://scanned-file-${mockSerial}.jpeg"
      import org.scalajs.dom.{HTMLCanvasElement, document}
      val canvas = document.createElement("canvas").asInstanceOf[HTMLCanvasElement]
      val ctx = canvas.getContext("2d")
      ctx.fillStyle = "white"
      ctx.fillRect(0, 0, canvas.width, canvas.height)
      ctx.fillStyle = "black"
      ctx.font = "40px sans-serif"
      ctx.fillText(mockSerial.toString, 20, 60)
      val dataURL = canvas.toDataURL("image/jpeg")
      saveImage(s, dataURL)
      mockSerial += 1
      DelayedCall.callLater(10 * duration / 10, () => cb(s))

    def getSavedImage(file: String): Future[ArrayBuffer] =
      val dataURL = getImage(file)
      Future.successful(dev.fujiwara.domq.DomqUtil.dataURLtoArrayBuffer(dataURL))

  object MockApi:
    var mockSerial: Int = 1
    private var fileMap: Map[String, String] = Map.empty
    def saveImage(file: String, dataURL: String): Unit = 
      fileMap = fileMap + (file -> dataURL)
    def getImage(file: String): String = fileMap(file)
