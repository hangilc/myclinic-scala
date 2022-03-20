package dev.myclinic.scala.web.reception.scan.docscan

import dev.myclinic.scala.model.ScannerDevice
import dev.myclinic.scala.webclient.{Api, global}
import scala.util.Success
import scala.util.Failure

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
