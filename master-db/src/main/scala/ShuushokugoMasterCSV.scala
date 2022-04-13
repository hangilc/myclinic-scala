package dev.myclinic.scala.masterdb

import org.apache.commons.csv.CSVRecord
import CSVRecordEx.*
import java.time.LocalDate
import dev.myclinic.scala.model.ShuushokugoMaster

case class ShuushokugoMasterCSV(
  kubun: Int,
  shuushokugocode: Int,
  name: String
): 
  def toMaster: ShuushokugoMaster =
    new ShuushokugoMaster(
      shuushokugocode = this.shuushokugocode,
      name = this.name
    )

object ShuushokugoMasterCSV:
  def from(row: CSVRecord): ShuushokugoMasterCSV =
    new ShuushokugoMasterCSV(
        kubun = row.getInt(1),
        shuushokugocode = row.getInt(3),
        name = row.getString(7)
    )