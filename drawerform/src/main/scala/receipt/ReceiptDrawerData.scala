package dev.myclinic.scala.drawerform.receipt

import io.circe.*
import io.circe.syntax.*
import io.circe.parser.*
import io.circe.generic.semiauto._
import dev.myclinic.scala.model.Patient
import dev.myclinic.scala.model.Meisai
import dev.myclinic.scala.model.MeisaiSection
import java.time.LocalDate
import dev.fujiwara.kanjidate.KanjiDate
import dev.myclinic.scala.model.Visit

case class ReceiptDrawerData(
    var patientName: String = "",
    var charge: Int = 0,
    var visitDate: String = "",
    var issueDate: String = "",
    var patientId: String = "",
    var hoken: String = "",
    var futanWari: String = "",
    var shoshin: String = "",
    var kanri: String = "",
    var zaitaku: String = "",
    var kensa: String = "",
    var gazou: String = "",
    var touyaku: String = "",
    var chuusha: String = "",
    var shochi: String = "",
    var sonota: String = "",
    var souten: String = "",
    var hokengai: List[String] = List("", "", "", "")
):
  def setPatient(patient: Patient): Unit =
    patientName = patient.fullName()
    patientId = patient.patientId.toString

  def setMeisai(meisai: Meisai): Unit =
    charge = meisai.charge
    futanWari = if meisai.futanWari == 10 then "" else meisai.futanWari.toString
    meisai.items.foreach(sect => {
      val ten = if sect.subtotal > 0 then sect.subtotal.toString else ""
      sect.section match {
        case MeisaiSection.ShoshinSaisin => shoshin = ten
        case MeisaiSection.IgakuKanri    => kanri = ten
        case MeisaiSection.Zaitaku       => zaitaku = ten
        case MeisaiSection.Kensa         => kensa = ten
        case MeisaiSection.Gazou         => gazou = ten
        case MeisaiSection.Touyaku       => touyaku = ten
        case MeisaiSection.Chuusha       => chuusha = ten
        case MeisaiSection.Shochi        => shochi = ten
        case MeisaiSection.Sonota        => sonota = ten
      }
    })
    souten = meisai.totalTen.toString

  def setVisitDate(visitDate: LocalDate): Unit =
    this.visitDate = KanjiDate.dateToKanji(visitDate, formatYoubi = _ => "")

  def setIssueDate(issueDate: LocalDate): Unit =
    this.issueDate = KanjiDate.dateToKanji(issueDate, formatYoubi = _ => "")

object ReceiptDrawerData:
  given Encoder[ReceiptDrawerData] = deriveEncoder[ReceiptDrawerData]
  given Decoder[ReceiptDrawerData] = deriveDecoder[ReceiptDrawerData]

  def create(patient: Patient, meisai: Meisai, visit: Visit,
    issueDate: LocalDate): ReceiptDrawerData =
    val data = new ReceiptDrawerData()
    data.setPatient(patient)
    data.setMeisai(meisai)
    data.setVisitDate(visit.visitedAt.toLocalDate)
    data.setIssueDate(issueDate)
    data
