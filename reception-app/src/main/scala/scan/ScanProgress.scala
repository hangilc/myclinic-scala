package dev.myclinic.scala.web.reception.scan

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Selection, CustomEvent}
import scala.language.implicitConversions
import dev.myclinic.scala.model.ScannerDevice
import org.scalajs.dom.raw.HTMLInputElement
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Success, Failure}

abstract class ScanProgress(scanBox: ScanBox):
  val eScanButton = button()
  val eScanProgress = span()
  val ele = div(
    eScanButton(
      "スキャン開始",
      onclick := (startScan _),
      disabled := true
    ),
    eScanProgress(displayNone)
  )
  def onScan(savedFile: String): Unit

  def enableScan(flag: Boolean): Unit = eScanButton.enable(flag)

  private def startScan(): Unit =
    scanBox.selectedScanner.foreach(deviceId => {
      ele.dispatchEvent(CustomEvent("scan-started", (), true))
      val resolution = 100
      eScanProgress.innerText = "スキャンの準備中"
      eScanProgress(displayDefault)
      (for file <- Api.scan(deviceId, (reportProgress _), 100)
      yield
        eScanProgress.innerText = ""
        eScanProgress(displayNone)
        file
      ).onComplete(r => {
        ele.dispatchEvent(CustomEvent("scan-ended", (), true))
        r match {
          case Success(file) => onScan(file)
          case Failure(ex) => System.err.println(ex.getMessage)
        }
      })
    })

  private def reportProgress(loaded: Double, total: Double): Unit =
    val pct = loaded / total * 100
    eScanProgress.innerText = s"${pct}%"


