package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.webclient.{Api, global}
import scala.concurrent.Future
import dev.myclinic.scala.web.practiceapp.practice.record.Record
import org.scalajs.dom.HTMLElement

class RecordsWrapper:
  import RecordsWrapper as Helper
  val ele = div()
  val records: CompSortList[Record] = new CompSortList[Record](ele)

  val unsubscribers = List(
    PracticeBus.navPageChanged.subscribe(page => startPage(page)),
    PracticeBus.hokenInfoChanged.subscribe {
      case (visitId, hokenInfo) => onHokenInfoChanged(visitId, hokenInfo)
    },
    PracticeBus.shinryouEntered.subscribe(onShinryouEntered _),
    PracticeBus.shinryouDeleted.subscribe(onShinryouDeleted _),
    PracticeBus.conductEntered.subscribe(onConductEntered _),
    PracticeBus.conductDeleted.subscribe(onConductDeleted _),
    PracticeBus.chargeUpdated.subscribe(onChargeUpdated _),
    PracticeBus.paymentEntered.subscribe(onPaymentEntered _),
    PracticeBus.visitUpdated.subscribe(onVisitUpdated _)
  )

  def dispose: Unit = 
    unsubscribers.foreach(_.unsubscribe())
    clearRecords

  def clearRecords: Unit =
    records.clear()

  def startPage(page: Int): Unit =
    ele(clear)
    PracticeBus.currentPatient match {
      case None => ()
      case Some(patient) =>
        clearRecords
        for
          visitIds <- Api.listVisitIdByPatientReverse(
            patient.patientId,
            page * PracticeBus.visitsPerPage,
            PracticeBus.visitsPerPage
          )
          _ <- Future.sequence(visitIds.map(visitId => {
            for ex <- Api.getVisitEx(visitId)
            yield
              val rec = new Record(ex)
              records += rec
          }))
        yield ()
    }

  def findRecord(visitId: Int): Option[Record] = records.find(_.visitId == visitId)

  def onVisitUpdated(visitEx: VisitEx): Unit =
    findRecord(visitEx.visitId).foreach(or => records.replace(or, Record(visitEx)))

  def onHokenInfoChanged(visitId: Int, hoken: HokenInfo): Unit =
    findRecord(visitId).foreach(_.onHokenInfoChanged(hoken))

  def onShinryouEntered(shinryou: ShinryouEx): Unit =
    findRecord(shinryou.visitId).foreach(_.onShinryouEntered(shinryou))

  def onShinryouDeleted(shinryou: Shinryou): Unit =
    findRecord(shinryou.visitId).foreach(_.onShinryouDeleted(shinryou))
  
  def onConductEntered(conduct: ConductEx): Unit =
    findRecord(conduct.visitId).foreach(_.onConductEntered(conduct))
  
  def onConductDeleted(conduct: Conduct): Unit =
    findRecord(conduct.visitId).foreach(_.onConductDeleted(conduct))

  def onChargeUpdated(charge: Charge) =
    findRecord(charge.visitId).foreach(_.onChargeUpdated(charge))
  
  def onPaymentEntered(payment: Payment) =
    findRecord(payment.visitId).foreach(_.onPaymentEntered(payment))
  
object RecordsWrapper:
  given Dispose[RecordsWrapper] = _.dispose

