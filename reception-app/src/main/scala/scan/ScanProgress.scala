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

object ScanProgress:
  class UI:
    val eScanButton = button()
    val eScanProgress = span()
    val ele = div(
      eScanButton(
        "スキャン開始",
        disabled := true
      ),
      eScanProgress(displayNone)
    )

class ScanProgress(ui: ScanProgress.UI, deviceRef: () => Option[String])(using
    queue: ScanWorkQueue,
    scope: ScanBox.Scope
):
  var onScannedCallback: String => Future[Unit] = _ => Future.successful(())
  val onScannedCallbacks = new FutureCallbacks[String]
  val ele = ui.ele
  ui.eScanButton(onclick := (onScanClick _))

  def enableScan(flag: Boolean): Unit = ui.eScanButton.enable(flag)

  private def onScanClick(): Unit =
    deviceRef().foreach(deviceId => queue.append(scan(deviceId)))

  private def scan(deviceId: String): ScanTask =
    ScanTask(
      () => {
        ui.eScanProgress.clear().show
        for
          file <- Api.scan(
            deviceId,
            ScanBox.reportProgress(ui.eScanProgress),
            scope.resolution
          )
          _ = {
            ui.eScanProgress.innerText = ""
            ui.eScanProgress(displayNone)
          }
          _ <- onScannedCallback(file)
        yield ()
      },
      isScanning = Some(deviceId)
    )

// private def reportProgress(loaded: Double, total: Double): Unit =
//   val pct = loaded / total * 100
//   ui.eScanProgress.innerText = s"${pct}%"

// context.globallyScanEnabled.addCallback(adaptScanButton _)
// context.isScanning.addCallback(adaptScanButton _)
// private def adaptScanButton(): Unit =
//   eScanButton.enable(context.canScan)

// private def startScan(): Unit =
//   context.scannerDeviceId.value.foreach(deviceId => {
//     context.isScanning.update(true)
//     val resolution = 100
//     eScanProgress.innerText = "スキャンの準備中"
//     eScanProgress(displayDefault)
//     (for file <- Api.scan(deviceId, (reportProgress _), 100)
//     yield
//       eScanProgress.innerText = ""
//       eScanProgress(displayNone)
//       file
//     ).onComplete(r => {
//       context.isScanning.update(false)
//       r match {
//         case Success(file) => onScan(file)
//         case Failure(ex) => System.err.println(ex.getMessage)
//       }
//     })
//   })

// private def reportProgress(loaded: Double, total: Double): Unit =
//   val pct = loaded / total * 100
//   eScanProgress.innerText = s"${pct}%"
