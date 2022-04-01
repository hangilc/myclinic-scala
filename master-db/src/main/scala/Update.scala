package dev.myclinic.scala.masterdb

import java.io.File
import java.nio.file.{Path}
import org.apache.commons.csv.{CSVParser, CSVFormat, CSVRecord}
import java.nio.charset.Charset
import collection.convert.ImplicitConversions.*
import dev.myclinic.scala.db.Db
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
          println(m)
          (r, m)
      })
      .compile
      .drain
      
    
    // var total = 0
    // var newItem = 0
    // for rec <- parser do
    //   val data = ShinryouMasterCSV.from(rec)
    //   val m = Db.findShinryouMaster(data.shinryoucode, LocalDate.now).unsafeRunSync()
    //   m match {
    //     case Some(master) => ()
    //     case None => newItem += 1
    //   }
    //   println(s"${total} ${data.name}")
    //   total += 1
    // println(s"new items: ${newItem}")
    // println(s"total: ${total}")

  extension (r: CSVRecord)
    def getString(index: Int): String =
      r.get(index - 1)

  def getMasterFilesDir: File =
    val dir = new File("./work/master-files")
    if !dir.isDirectory then
      throw new RuntimeException("Directory does not exist: " + dir.toString)
    dir
  
  def getShinryouFile: Path = 
    getMasterFilesDir.toPath.resolve("s/s.csv")

