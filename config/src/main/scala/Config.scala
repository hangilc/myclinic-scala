package dev.myclinic.scala.config

import _root_.java.io.File
import _root_.java.nio.file.Files
import _root_.java.nio.file.Path
import cats.instances.try_
import com.typesafe.scalalogging.Logger
import dev.myclinic.scala.clinicop.AdHocHolidayRange
import dev.myclinic.scala.model.DiseaseExample
import io.circe.Decoder
import io.circe.Decoder.Result
import io.circe.Encoder
import io.circe.HCursor
import io.circe.generic.semiauto._
import io.circe.syntax.*
import io.circe.yaml.parser

import scala.io.Source
import scala.util.Try

val Config = new Configurator()

class Configurator extends JavaConfigurator with FileConfigurator:
  val logger = Logger(getClass.getName)
  val dataDir = Path.of(System.getenv("MYCLINIC_DATA_DIR"))
  val configDir = dataDir.resolve("config")

  private lazy val scanDir: String = System.getenv("MYCLINIC_PAPER_SCAN_DIR")

  def paperScanRoot: Path =
    Path.of(scanDir)

  def paperScanDir(patientId: Int): String =
    val d = paperScanRoot.resolve(patientId.toString)
    if !Files.exists(d) then d.toFile.mkdirs
    d.toString

  given Encoder[AdHocHolidayRange] = deriveEncoder[AdHocHolidayRange]
  given Decoder[AdHocHolidayRange] = deriveDecoder[AdHocHolidayRange]
  given Decoder[StampInfo] = deriveDecoder[StampInfo]

  def adHocHolidayRanges: List[AdHocHolidayRange] =
    readDataYamlFile("adhoc-holidays.yaml")

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
              case "Y" => m.copy(yakuzai = m.yakuzai.extend(r))
              case "S" => m.copy(shinryou = m.shinryou.extend(r))
              case "K" => m.copy(kizai = m.kizai.extend(r))
              case "D" => m.copy(byoumei = m.byoumei.extend(r))
              case "A" => m.copy(shuushokugo = m.shuushokugo.extend(r))
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

  private lazy val portalTmpDir: Path =
    Path.of(System.getenv("MYCLINIC_PORTAL_TMP_DIR"))

  def resolvePortalTmpFile(file: String): Path =
    val p = portalTmpDir.resolve(file).normalize()
    if p.startsWith(portalTmpDir) then p
    else throw new RuntimeException("Invalid portal tmp file path: " + file)

  def getDiseaseExample: List[DiseaseExample] =
    val file: File = configDir.resolve(s"disease-example.yml").toFile
    given Decoder[String | List[String]] with
      def apply(c: HCursor): Result[String | List[String]] =
        c.as[String].orElse(c.as[List[String]])
    readYaml[List[Map[String, String | List[String]]]](file)
      .map(m => DiseaseExample.fromMap(m))

  def defaultKoukikoureiHokenshaBangou: Int =
    39131156 // 杉並区

  def getDictValue(key: String): String =
    val file: File = dataDir.resolve("dict.yaml").toFile
    if !Files.exists(file.toPath()) then
      Files.writeString(file.toPath(), "{}")
      ""
    else
      readYaml[Map[String, String]](file).getOrElse(key, "")

  def setDictValue(key: String, value: String) =
    val file: File = dataDir.resolve("dict.yaml").toFile
    if !Files.exists(file.toPath()) then
      Files.writeString(file.toPath(), "")
    val dict = readYaml[Map[String, String]](file)
    writeYaml(file, dict.updated(key, value))

  private def readDataFile(file: String): String =
    fileContent(dataDir.resolve(file))

  private def readDataYamlFile[T](file: String)(using Decoder[T]): T =
    readYaml[T](dataDir.resolve(file).toFile)

  def phonebook: String =
    Try(readDataFile("phonebook.txt")).getOrElse("")

