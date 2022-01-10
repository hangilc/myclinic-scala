package dev.myclinic.scala.web.reception.scan.scanbox

import dev.fujiwara.domq.ElementQ.*
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Selection}
import scala.language.implicitConversions
import dev.myclinic.scala.model.ScannerDevice
import org.scalajs.dom.HTMLInputElement
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Success, Failure}

class ScannerSelect(ui: ScannerSelect.UI):
  ui.eRefreshButton(onclick := (() => { refreshScannerSelect(); () }))
  def init: Future[Unit] = refreshScannerSelect()

  def selected: Option[String] =
    ui.eScannerSelect.getValueOption

  private def doRefresh(): Unit =
    refreshScannerSelect().onComplete {
      case Success(_)  => ()
      case Failure(ex) => System.err.println(ex.getMessage)
    }

  private def refreshScannerSelect(): Future[Unit] =
    for devices <- Api.listScannerDevices()
    yield setSelectOptions(devices)

  private def setSelectOptions(devices: List[ScannerDevice]): Unit =
    ui.eScannerSelect.setChildren(
      devices.map(device => {
        option(device.name, value := device.deviceId)
      })
    )

object ScannerSelect:
  class UI:
    val eScannerSelect = select
    val eRefreshButton = button
    val ele = div(
      h2("スキャナ選択"),
      eScannerSelect,
      eRefreshButton(
        "更新"
      )
    )

