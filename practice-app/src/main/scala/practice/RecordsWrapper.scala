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
    PracticeBus.patientChanged.subscribe(patientOpt =>
      patientOpt match {
        case Some(patient) => ()
        case None          => ele(clear)
      }
    ),
    PracticeBus.patientChanged.subscribe(optPatient =>
      optPatient match {
        case None          => div(clear)
        case Some(patient) => initPatient(patient)
      }
    )
  )

  def dispose: Unit = 
    unsubscribers.foreach(f => f())

  def initPatient(patient: Patient): Unit =
    ele(clear)
    for
      total <- Api.countVisitByPatient(patient.patientId)
      numPages = Helper.calcNumPages(total)
      visitIds <- Api.listVisitIdByPatientReverse(
        patient.patientId,
        0,
        Helper.itemsPerPage
      )
      _ = println(visitIds)
      _ <- Future.sequence(visitIds.map(visitId => {
        val rec = new Record(visitId)
        ele(rec.ele)
        for ex <- Api.getVisitEx(visitId)
        yield rec.init(ex)
      }))
    yield ()

object RecordsWrapper:
  val itemsPerPage = 10
  def calcNumPages(total: Int): Int =
    (total + itemsPerPage - 1) / itemsPerPage
