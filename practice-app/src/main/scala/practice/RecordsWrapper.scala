package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.webclient.{Api, global}

class RecordsWrapper:
  import RecordsWrapper as Helper
  val ele = div()
  PracticeBus.patientChanged.subscribe(patientOpt => patientOpt match {
    case Some(patient) => ()
    case None => ele(clear)
  })

  PracticeBus.patientChanged.subscribe(optPatient => optPatient match {
    case None => div(clear)
    case Some(patient) => initPatient(patient)
  })


  def initPatient(patient: Patient): Unit =
    for
      total <- Api.countVisitByPatient(patient.patientId)
      numPages = Helper.calcNumPages(total)
      visitIds <- Api.listVisitIdByPatient(patient.patientId, 0, Helper.itemsPerPage)
    yield ()

object RecordsWrapper:
  val itemsPerPage = 10
  def calcNumPages(total: Int): Int =
    (total + itemsPerPage - 1) / itemsPerPage