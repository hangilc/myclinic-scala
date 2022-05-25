package dev.myclinic.scala.config

import dev.myclinic.java
import dev.myclinic.scala.model.ClinicInfo
import _root_.java.nio.file.Path
import _root_.java.nio.file.Files
import dev.myclinic.scala.clinicop.AdHocHolidayRange
import io.circe.yaml.parser
import io.circe.syntax.*
import io.circe.generic.semiauto._
import io.circe.Encoder
import io.circe.Decoder
import com.typesafe.scalalogging.Logger
import _root_.java.io.File
import cats.instances.try_

object Config extends ConfigCirce:
  val logger = Logger(getClass.getName)
  val config = new java.Config()
  val dataDir = Path.of(System.getenv("MYCLINIC_DATA_DIR"))

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

  def adHocHolidayRanges: List[AdHocHolidayRange] = 
    val file = dataDir.resolve("adhoc-holidays.yaml").toFile
    val reader: _root_.java.io.Reader = _root_.java.io.FileReader(file)
    try
      parser.parse(reader).flatMap(_.as[List[AdHocHolidayRange]])
        .getOrElse({
          logger.error("Failed to read AdHocholidayRange")
          List.empty
        })
    finally
      reader.close()

  def getShohouSamples: List[String] =
    val file = dataDir.resolve("shohou-sample.txt")
    Files.readString(file).split("\n\n").toList

  def getShinryouRegular: Map[String, List[String]] =
    val file = dataDir.resolve("shinryou-regular.yaml").toFile
    readYaml[Map[String, List[String]]](file)

  def readYaml[T: Decoder](file: File): T =
    val reader: _root_.java.io.Reader = _root_.java.io.FileReader(file)
    try
      parser.parse(reader).flatMap(_.as[T]).getOrElse(
        throw new RuntimeException("Failed to read: " + file.toString)
      )
    finally
      reader.close()
    

trait ConfigCirce:
  given Encoder[AdHocHolidayRange] = deriveEncoder[AdHocHolidayRange]
  given Decoder[AdHocHolidayRange] = deriveDecoder[AdHocHolidayRange]



  
