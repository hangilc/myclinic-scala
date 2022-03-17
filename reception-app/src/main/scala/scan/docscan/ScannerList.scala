package dev.myclinic.scala.web.reception.scan.docscan

import dev.myclinic.scala.model.ScannerDevice
import dev.myclinic.scala.webclient.{Api, global}

object ScannerList:
  private var scanners: Option[List[ScannerDevice]] = None

  def get(cb: List[ScannerDevice] => Unit): Unit =
    scanners match {
      case Some(list) => cb(list)
      case None =>
        for
          list <- Api.listScannerDevices()
        yield
          scanners = Some(list)
          cb(list)
    }