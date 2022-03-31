package dev.myclinic.scala.masterdb

import java.io.File
import java.nio.file.{Path}
import org.apache.commons.csv.{CSVParser, CSVFormat, CSVRecord}
import java.nio.charset.Charset
import collection.convert.ImplicitConversions.*

object Update:
  def updateShinryou(): Unit =
    val master = getShinryouFile
    val parser = CSVParser.parse(master.toFile, Charset.forName("MS932"), CSVFormat.RFC4180)
    var count = 0
    for rec <- parser do
      val data = ShinryouMasterCSV.from(rec)
      println(data.name)

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

