package dev.myclinic.scala.masterdb

import org.apache.commons.csv.CSVRecord
import CSVRecordEx.*
import java.time.LocalDate
import dev.myclinic.scala.model.ByoumeiMaster
import dev.myclinic.scala.model.ValidUpto

case class ByoumeiMasterCSV(
    kubun: Int,
    shoubyoumeicode: Int,
    name: String
):
  def toMaster: ByoumeiMaster =
    new ByoumeiMaster(
      shoubyoumeicode = this.shoubyoumeicode,
      name = this.name
    )

object ByoumeiMasterCSV:
  def from(row: CSVRecord): ByoumeiMasterCSV =
    new ByoumeiMasterCSV(
      kubun = row.getInt(1),
      shoubyoumeicode = row.getInt(3),
      name = row.getString(6)
    )
