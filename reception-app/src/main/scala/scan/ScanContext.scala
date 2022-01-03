package dev.myclinic.scala.web.reception.scan

import dev.myclinic.scala.model.Patient
import java.time.LocalDateTime
import dev.myclinic.scala.model.ScannerDevice

class ScanContext:
  val timestamp: String = ScanContext.makeTimeStamp()
  val globallyScanEnabled = Variable[Boolean](true)
  val patient = Variable[Option[Patient]](None)
  val scanType = Variable[String]("image")
  val scannerDeviceId = Variable[Option[String]](None)
  val isScanning = Variable[Boolean](false)
  val isUploading = Variable[Boolean](false)
  val numScanned = Variable[Int](0)

  def canScan: Boolean = globallyScanEnabled.value && !isScanning.value

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
