package dev.myclinic.scala.masterdb

import org.apache.commons.csv.CSVRecord
import dev.myclinic.scala.masterdb.CSVRecordEx.*
import java.time.LocalDate
import dev.myclinic.scala.model.{KizaiMaster, ValidUpto}

case class KizaiMasterCSV(
    kubun: Int,
    masterShubetsu: String,
    kizaicode: Int,
    name: String,
    yomi: String,
    unit: String,
    kingakuShubetsu: String,
    kingaku: String
):
  def toMaster(validFrom: LocalDate): KizaiMaster =
    new KizaiMaster(
      kizaicode = this.kizaicode,
      name = this.name,
      yomi = this.yomi,
      unit = this.unit,
      kingakuStore = this.kingaku,
      validFrom = validFrom,
      validUpto = ValidUpto(None)
    )

object KizaiMasterCSV:
  def from(row: CSVRecord): KizaiMasterCSV =
    new KizaiMasterCSV(
      kubun = row.getInt(1),
      masterShubetsu = row.getString(2),
      kizaicode = row.getInt(3),
      name = row.getString(5),
      yomi = row.getString(7),
      unit = row.getString(10),
      kingakuShubetsu = row.getString(11),
      kingaku = row.getString(12)
    )

