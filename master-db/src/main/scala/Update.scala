package dev.myclinic.scala.masterdb

import java.io.File
import java.nio.file.{Path}
import org.apache.commons.csv.{CSVParser, CSVFormat, CSVRecord}
import java.nio.charset.Charset
import collection.convert.ImplicitConversions.*
import dev.myclinic.scala.db.Db
import dev.myclinic.scala.masterdb.CSVRecordEx.*
import dev.myclinic.scala.model.{ShinryouMaster}
import java.time.LocalDate
import cats.effect.unsafe.implicits.global
import cats.*
import cats.syntax.all.*
import cats.effect.*
import fs2.Stream

sealed trait ShinryouUpdate:
  def token: String

case class ShinryouModified(
    val record: ShinryouMasterCSV,
    val current: ShinryouMaster
) extends ShinryouUpdate:
  def token: String = "shinryou-modified"

case class ShinryouUnchanged(
    val current: ShinryouMaster
) extends ShinryouUpdate:
  def token: String = "shinryou-unchanged"

case class ShinryouEnded(
    val current: Option[ShinryouMaster]
) extends ShinryouUpdate:
  def token: String = "shinryou-ended"

case class ShinryouNew(
    val record: ShinryouMasterCSV
) extends ShinryouUpdate:
  def token: String = "shinryou-new"

object Update:
  def updateShinryou(): IO[Unit] =
    val master = getShinryouFile
    val parser = CSVParser.parse(
      master.toFile,
      Charset.forName("MS932"),
      CSVFormat.RFC4180
    )
    Stream
      .unfold(parser.iterator)(i => if i.hasNext then Some(i.next, i) else None)
      .covary[IO]
      .evalMap(csv => {
        val r = ShinryouMasterCSV.from(csv)
        for m <- Db.findShinryouMaster(r.shinryoucode, LocalDate.now)
        yield (r, m)
      })
      .map(classifyShinryou.tupled)
      .fold(Map[String, Int]().withDefaultValue(0)) { case (stat, item) =>
        stat.updated(item.token, stat(item.token) + 1)
      }
      .evalMap(stat => IO { println(stat) })
      .compile
      .drain

  def classifyShinryou(
      record: ShinryouMasterCSV,
      currentOpt: Option[ShinryouMaster]
  ): ShinryouUpdate =
    import MasterDbConsts.*
    (record, currentOpt) match {
      case (_, _)
          if record.kubun == HenkouKubunHaishi || record.kubun == HenkouKubunMasshou =>
        ShinryouEnded(currentOpt)
      case (_, None) => ShinryouNew(record)
      case (_, Some(current))
          if record
            .toMaster(current.validFrom)
            .copy(
              validUpto = current.validUpto
            ) == current =>
        ShinryouUnchanged(current)
      case (_, Some(current)) => ShinryouModified(record, current)
      case _ =>
        throw new RuntimeException(
          s"Cannot handle shinryou update: ${record}; ${currentOpt}"
        )
    }

  def getMasterFilesDir: File =
    val dir = new File("./work/master-files")
    if !dir.isDirectory then
      throw new RuntimeException("Directory does not exist: " + dir.toString)
    dir

  def getShinryouFile: Path =
    getMasterFilesDir.toPath.resolve("s/s.csv")
