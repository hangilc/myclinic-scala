package dev.myclinic.scala.web.reception.scan.docscan

import dev.myclinic.scala.model.ScannerDevice

object ScannerList:
  def get(cb: List[ScannerDevice] => Unit): Unit =
    ???