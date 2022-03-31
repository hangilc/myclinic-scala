package dev.myclinic.scala.masterdb

import org.apache.commons.csv.CSVRecord

object CSVRecordEx:
  extension (r: CSVRecord)
    def getString(index: Int): String =
      r.get(index - 1)

    def getInt(index: Int): Int =
      getString(index).toInt

    def twoChars(index: Int): String =
      val v = getString(index)
      if v.size == 1 then "0" + v else v
