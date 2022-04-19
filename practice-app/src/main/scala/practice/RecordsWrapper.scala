package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.webclient.{Api, global}
import scala.concurrent.Future
import dev.myclinic.scala.web.practiceapp.practice.record.Record

class RecordsWrapper:
  import RecordsWrapper as Helper
  val ele = div()
  val unsubscribers: List[() => Unit] = List(
    PracticeBus.navPageChanged.subscribe(page => startPage(page))
  )

  def dispose: Unit = 
    unsubscribers.foreach(f => f())

  def startPage(page: Int): Unit =
    ele(clear)
    PracticeBus.currentPatient match {
      case None => ()
      case Some(patient) =>
        for
          visitIds <- Api.listVisitIdByPatientReverse(
            patient.patientId,
            page * PracticeBus.visitsPerPage,
            PracticeBus.visitsPerPage
          )
          _ <- Future.sequence(visitIds.map(visitId => {
            val rec = new Record(visitId)
            ele(rec.ele)
            for ex <- Api.getVisitEx(visitId)
            yield rec.init(ex)
          }))
        yield ()
    }

