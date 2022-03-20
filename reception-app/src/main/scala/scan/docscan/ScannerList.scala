package dev.myclinic.scala.web.reception.scan.docscan

import dev.myclinic.scala.model.ScannerDevice
import dev.myclinic.scala.webclient.{Api, global}
import scala.util.Success
import scala.util.Failure
import dev.fujiwara.domq.LocalEventPublisher

object ScannerList:
  def list(cb: List[ScannerDevice] => Unit): Unit =
    val f =
      for list <- Api.listScannerDevices()
      yield 
        cb(list)
    f.onComplete {
      case Success(_)  => ()
      case Failure(ex) => System.err.println(ex.getMessage)
    }

  private var busyScanners: Set[ScannerDevice] = Set.empty
  private val onBusyScannersChange = LocalEventPublisher[Set[ScannerDevice]]

  def onBusyScannersChange(handler: Set[ScannerDevice] => Unit): Unit =
    onBusyScannersChange.subscribe(handler)

  def getBusyScanners: Set[ScannerDevice] = busyScanners

  def openScanner(scanner: ScannerDevice): Boolean =
    println(("open-scanner", scanner.name))
    if busyScanners.contains(scanner) then false
    else
      busyScanners = busyScanners + scanner
      onBusyScannersChange.publish(busyScanners)
      true

  def closeScanner(scanner: ScannerDevice): Unit =
    println(("close-scanner", scanner.name))
    if busyScanners.contains(scanner) then
      busyScanners = busyScanners - scanner
      onBusyScannersChange.publish(busyScanners)
