package dev.myclinic.scala.web.reception.scan

trait ScanBoxUIComponent:
  def updateUI(state: ScanBoxState): Unit

