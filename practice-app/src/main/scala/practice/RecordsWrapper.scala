package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.webclient.{Api, global}
import scala.concurrent.Future
import dev.myclinic.scala.web.practiceapp.practice.record.Record
import dev.fujiwara.domq.SortedComps
import org.scalajs.dom.HTMLElement

class RecordsWrapper:
  import RecordsWrapper as Helper
  import dev.fujiwara.domq.SortedComps.*
  val ele = div()
  val records: SortedComps[Record] = SortedComps[Record]()
  given Append[HTMLElement] = Append(ele)
  given PreInsert[HTMLElement] = PreInsert(ele)

  val unsubscribers: List[() => Unit] = List(
    PracticeBus.navPageChanged.subscribe(page => startPage(page))
  )

  def dispose: Unit = 
    unsubscribers.foreach(f => f())
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

