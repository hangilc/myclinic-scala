package dev.myclinic.scala.masterdb

import java.io.File
import java.nio.file.{Path}
import org.apache.commons.csv.{CSVParser, CSVFormat, CSVRecord}
import java.nio.charset.Charset
import collection.convert.ImplicitConversions.*
import dev.myclinic.scala.db.Db
import dev.myclinic.scala.masterdb.CSVRecordEx.*
import java.time.LocalDate
import cats.effect.unsafe.implicits.global
import cats.*
import cats.syntax.all.*
import cats.effect.*
import fs2.Stream

object Update:
  def updateShinryou(): IO[Unit] =
    val master = getShinryouFile
    val parser = CSVParser.parse(master.toFile, Charset.forName("MS932"), CSVFormat.RFC4180)
    Stream.unfold(parser.iterator)(i => if i.hasNext then Some(i.next, i) else None)
      .covary[IO]
      .evalMap(csv => {
        val r = ShinryouMasterCSV.from(csv)
        for
          m <- Db.findShinryouMaster(r.shinryoucode, LocalDate.now)
        yield 
          (r, m)
      })
      .scanChunksOpt(0)(s => {
        
      })
      .compile
      .drain

  // extension (r: CSVRecord)
  //   def getString(index: Int): String =
  //     r.get(index - 1)

  def getMasterFilesDir: File =
    val dir = new File("./work/master-files")
    if !dir.isDirectory then
      throw new RuntimeException("Directory does not exist: " + dir.toString)
    dir
  
  def getShinryouFile: Path = 
    getMasterFilesDir.toPath.resolve("s/s.csv")

