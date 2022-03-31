package dev.myclinic.scala.masterdb

import dev.myclinic.scala.masterdb.CSVRecordEx.*
import org.apache.commons.csv.CSVRecord
import dev.myclinic.scala.model.{ShinryouMaster, ValidUpto}
import java.time.LocalDate

case class ShinryouMasterCSV(
    kubun: Int,
    masterShubetsu: String,
    nyuuGaiTekiyou: String,
    byouShinKubun: String,
    tensuuShikibetsu: String,
    shinryoucode: Int,
    name: String,
    tensuu: String,
    shuukeisaki: String,
    houkatsukensa: String,
    oushinKubun: String,
    kensaGroup: String
):
  def toMaster(argValidFrom: LocalDate): ShinryouMaster =
    ShinryouMaster(
      shinryoucode = this.shinryoucode,
      name = this.name,
      tensuuStore = this.tensuu,
      tensuuShikibetsu = this.tensuuShikibetsu,
      shuukeisaki = this.shuukeisaki,
      houkatsukensa = this.houkatsukensa,
      oushinkubun = this.oushinKubun,
      kensagroup = this.kensaGroup,
      validFrom = argValidFrom,
      validUpto = ValidUpto(None)
    )

object ShinryouMasterCSV:
  def from(row: CSVRecord): ShinryouMasterCSV =
    ShinryouMasterCSV(
      kubun = row.getInt(1),
      masterShubetsu = row.getString(2),
      nyuuGaiTekiyou = row.getString(13),
      byouShinKubun = row.getString(19),
      tensuuShikibetsu = row.getString(11),
      shinryoucode = row.getInt(3),
      name = row.getString(5),
      tensuu = row.getString(12),
      shuukeisaki = row.getString(15),
      houkatsukensa = row.twoChars(16),
      oushinKubun = row.getString(17),
      kensaGroup = row.twoChars(51)
    )
