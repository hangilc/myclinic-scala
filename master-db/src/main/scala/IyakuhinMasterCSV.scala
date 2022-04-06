package dev.myclinic.scala.masterdb

import org.apache.commons.csv.CSVRecord
import CSVRecordEx.*
import java.time.LocalDate
import dev.myclinic.scala.model.IyakuhinMaster
import dev.myclinic.scala.model.ValidUpto

case class IyakuhinMasterCSV(
    kubun: Int,
    masterShubetsu: String,
    iyakuhincode: Int,
    name: String,
    yomi: String,
    unit: String,
    kingakuShubetsu: Int,
    yakka: String,
    madoku: Int,
    kouhatsu: Int,
    zaikei: Int,
    yakkacode: String
):
  def toMaster(validFrom: LocalDate): IyakuhinMaster =
    new IyakuhinMaster(
      iyakuhincode = this.iyakuhincode,
      yakkacode = this.yakkacode,
      name = this.name,
      yomi = this.yomi,
      unit = this.unit,
      yakkaStore = this.yakka,
      madoku = this.madoku.toString,
      kouhatsu = this.kouhatsu.toString,
      zaikei = this.zaikei.toString,
      validFrom = validFrom,
      validUpto = ValidUpto(None)
    )

object IyakuhinMasterCSV:
  def from(row: CSVRecord): IyakuhinMasterCSV =
    new IyakuhinMasterCSV(
      kubun = row.getInt(1),
      masterShubetsu = row.getString(2),
      iyakuhincode = row.getInt(3),
      name = row.getString(5),
      yomi = row.getString(7),
      unit = row.getString(10),
      kingakuShubetsu = row.getInt(11),
      yakka = row.getString(12),
      madoku = row.getInt(14),
      kouhatsu = row.getInt(17),
      zaikei = row.getInt(28),
      yakkacode = row.getString(32)
    )
