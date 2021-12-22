package dev.myclinic.scala.config

import dev.myclinic.java
import dev.myclinic.scala.model.ClinicInfo
import _root_.java.nio.file.Path
import _root_.java.nio.file.Files

object Config:
  val config = new java.Config()

  def getClinicInfo: ClinicInfo =
    val jc = config.getClinicInfo()
    ClinicInfo(
      jc.name,
      jc.postalCode,
      jc.address,
      jc.tel,
      jc.fax,
      jc.todoufukencode,
      jc.tensuuhyoucode,
      jc.kikancode,
      jc.homepage,
      jc.doctorName
    )

  private lazy val scanDir: String = System.getenv("MYCLINIC_PAPER_SCAN_DIR")

  def paperScanDir(patientId: Int): String =
    val d = Path.of(scanDir, patientId.toString)
    if !Files.exists(d) then
      d.toFile.mkdirs
    d.toString
