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
    PracticeBus.navPageChanged.subscribe(page => startPage(page))
  )

  def dispose: Unit = 
    unsubscribers.foreach(_.unsubscribe)
    clearRecords

  def clearRecords: Unit =
    records.clear

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

object RecordsWrapper:
  given Dispose[RecordsWrapper] = _.dispose

