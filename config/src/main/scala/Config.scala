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
import scala.io.Source
import io.circe.HCursor
import io.circe.Decoder.Result

object Config extends ConfigCirce:
  val logger = Logger(getClass.getName)
  val config = new java.Config()
  val dataDir = Path.of(System.getenv("MYCLINIC_DATA_DIR"))
  val configDir = dataDir.resolve("config")

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

  def paperScanRoot: Path =
    Path.of(scanDir)

  def paperScanDir(patientId: Int): String =
    val d = paperScanRoot.resolve(patientId.toString)
    // val d = Path.of(scanDir, patientId.toString)
    if !Files.exists(d) then d.toFile.mkdirs
    d.toString

  def adHocHolidayRanges: List[AdHocHolidayRange] =
    val file = dataDir.resolve("adhoc-holidays.yaml").toFile
    val reader: _root_.java.io.Reader = _root_.java.io.FileReader(file)
    try
      parser
        .parse(reader)
        .flatMap(_.as[List[AdHocHolidayRange]])
        .getOrElse({
          logger.error("Failed to read AdHocholidayRange")
          List.empty
        })
    finally reader.close()

  def getShohouSamples: List[String] =
    val file = dataDir.resolve("shohou-sample.txt")
    Files.readString(file).split("\n\n").toList

  def getShinryouRegular: Map[String, List[String]] =
    val file = dataDir.resolve("shinryou-regular.yaml").toFile
    readYaml[Map[String, List[String]]](file)

  def getShinryouKensa: Map[String, List[String]] =
    val file = dataDir.resolve("shinryou-kensa.yaml").toFile
    readYaml[Map[String, List[String]]](file)

  def getMasterNameMap: MasterNameMap =
    import MasterNameMap.linePattern as pat
    val src: Source =
      Source.fromFile(configDir.resolve("master-name.txt").toFile, "UTF-8")
    try
      val map = src.getLines
        .map(line =>
          line match {
            case pat(kind, name, idString) => Some(kind, name, idString.toInt)
            case _                         => None
          }
        )
        .collect({ case Some(a) => a })
        .toList
        .groupMap(_.head)(_.tail)
      MasterNameMap(map)
    finally src.close()

  def getMasterTransition: MasterTransition =
    val src: Source =
      Source.fromFile(configDir.resolve("master-map.txt").toFile, "UTF-8")
    try
      src.getLines.foldLeft(MasterTransition())((m, s) =>
        MasterTransitionRule.parse(s) match {
          case Some(kind, r) =>
            kind match {
              case "S" => m.copy(shinryou = m.shinryou.extend(r))
              case "K" => m.copy(kizai = m.kizai.extend(r))
              case _   => m
            }
          case None => m
        }
      )
    finally src.close()

  def getStampInfo(name: String): StampInfo =
    val file: File = configDir.resolve(s"stamp-data/${name}.yml").toFile
    val stampInfo: StampInfo = readYaml[StampInfo](file)
    if !Path.of(stampInfo.imageFile).isAbsolute then
      val absPath = configDir
        .resolve("stamp-data")
        .resolve(Path.of(stampInfo.imageFile))
        .toAbsolutePath()
        .toString()
      stampInfo.copy(imageFile = absPath)
    else stampInfo

  def portalTmpDir: Path =
    Path.of(System.getenv("MYCLINIC_PORTAL_TMP_DIR"))

  def getDiseaseExample: List[DiseaseExample] =
    val file: File = configDir.resolve(s"disease-example.yml").toFile
    given Decoder[String | List[String]] with
        def apply(c: HCursor): Result[String | List[String]] =
          c.as[String].orElse(c.as[List[String]])
    readYaml[List[Map[String, String | List[String]]]](file)
        .map(m => DiseaseExample.fromMap(m))

  def readYaml[T: Decoder](file: File): T =
    val reader: _root_.java.io.Reader = _root_.java.io.FileReader(file)
    try
      parser
        .parse(reader)
        .flatMap(_.as[T])
        .getOrElse(
          throw new RuntimeException("Failed to read: " + file.toString)
        )
    finally reader.close()

trait ConfigCirce:
  given Encoder[AdHocHolidayRange] = deriveEncoder[AdHocHolidayRange]
  given Decoder[AdHocHolidayRange] = deriveDecoder[AdHocHolidayRange]
  given Decoder[StampInfo] = deriveDecoder[StampInfo]
