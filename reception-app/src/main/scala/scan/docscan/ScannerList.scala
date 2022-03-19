package dev.myclinic.scala.web.reception.scan.docscan

import dev.myclinic.scala.model.ScannerDevice
import dev.myclinic.scala.webclient.{Api, global}
import scala.util.Success
import scala.util.Failure

object ScannerList:
  private var scanners: Option[List[ScannerDevice]] = None

  def get(cb: List[ScannerDevice] => Unit): Unit =
    scanners match {
      case Some(list) => cb(list)
      case None =>
        val f =
          for list <- Api.listScannerDevices()
          yield
            scanners = Some(list)
            cb(list)
        f.onComplete {
          case Success(_)  => ()
          case Failure(ex) => System.err.println(ex.getMessage)
        }
    }
