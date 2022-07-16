package dev.myclinic.scala.web.appbase

import dev.myclinic.scala.drawerform.receipt.ReceiptDrawerData

import dev.myclinic.scala.model.*
import dev.fujiwara.kanjidate.KanjiDate
import java.time.LocalDate
import dev.myclinic.scala.apputil.HokenUtil
import scala.concurrent.Future
import dev.myclinic.scala.webclient.{Api, global}
import dev.fujiwara.scala.drawer.Op
import dev.fujiwara.scala.drawer.PaperSize

object ReceiptUtil:
  def receiptDrawerData(patient: Patient, visit: VisitEx, meisai: Meisai): ReceiptDrawerData =
    val data = ReceiptDrawerData()
    data.setPatient(patient)
    data.charge = meisai.charge
    data.visitDate = KanjiDate.dateToKanji(visit.visitedAt.toLocalDate, formatYoubi = _ => "")
    data.issueDate = KanjiDate.dateToKanji(LocalDate.now(), formatYoubi = _ => "")
    data.hoken = HokenUtil.hokenRep(visit)
    data.futanWari = 
      if meisai.futanWari == 10 then "" else meisai.futanWari.toString
    meisai.items.foreach(sect => {
      val ten = if sect.subtotal > 0 then sect.subtotal.toString else ""
      sect.section match {
        case MeisaiSection.ShoshinSaisin => data.shoshin = ten
        case MeisaiSection.IgakuKanri => data.kanri = ten
        case MeisaiSection.Zaitaku => data.zaitaku = ten
        case MeisaiSection.Kensa => data.kensa = ten
        case MeisaiSection.Gazou => data.gazou = ten
        case MeisaiSection.Touyaku => data.touyaku = ten
        case MeisaiSection.Chuusha => data.chuusha = ten
        case MeisaiSection.Shochi => data.shochi = ten
        case MeisaiSection.Sonota => data.sonota = ten
      }
    })
    data.souten = meisai.totalTen.toString
    data

  def receiptDrawerOps(data: ReceiptDrawerData): Future[List[Op]] =
    Api.drawReceipt(data)

  def printReceipt(patient: Patient, visit: VisitEx, meisai: Meisai, fileName: String): Future[Unit] =
    val data = receiptDrawerData(patient, visit, meisai)
    for
      ops <- receiptDrawerOps(data)
      _ <- Api.createPdfFile(ops, "A6_Landscape", fileName)
    yield ()
