package dev.myclinic.scala.web.reception.scan

import dev.myclinic.scala.model.Patient
import java.time.LocalDateTime
import dev.myclinic.scala.model.ScannerDevice

class ScanContext:
  val timestamp: String = ScanContext.makeTimeStamp()
  var globallyScanEnabled: Boolean = true
  val globallyScanEnabledCallbacks = new Callbacks
  var patient: Option[Patient] = None
  val patientCallbacks = new Callbacks
  var scanType: String = "image"
  val scanTypeCallbacks = new Callbacks
  var scannerDeviceId: Option[String] = None
  var isScanning: Boolean = false
  val isScanningCallbacks = new Callbacks
  var numScanned: Int = 0
  val numScannedCallbacks = new Callbacks

object ScanContext:
  def makeTimeStamp(): String =
    val at = LocalDateTime.now()
    String.format(
      "%d%02d%02d%02d%02d%02d",
      at.getYear,
      at.getMonthValue,
      at.getDayOfMonth,
      at.getHour,
      at.getMinute,
      at.getSecond
    )
