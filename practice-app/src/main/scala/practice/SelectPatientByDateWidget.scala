package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.all.{*, given}
import java.time.LocalDate
import dev.fujiwara.kanjidate.KanjiDate
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.model.Patient
import dev.myclinic.scala.model.Visit
import scala.concurrent.Future

class SelectPatientByDateWidget:
  import SelectPatientByDateWidget as Helper
  val origDate: LocalDate = LocalDate.now()
  val dateSpan = span
  val patientsDiv = div
  val content = div(
    dateSpan,
    patientsDiv
  )
  val widget = RightWidget("日付別患者リスト", content)
  def ele = widget.ele
  dateSpan(innerText := Helper.formatDate(origDate))
  for
    list <- Helper.listPatients(origDate)
  yield setPatients(list)

  def clearPatientsDiv(): Unit = patientsDiv(clear)

  def setPatients(items: List[(Patient, Visit)]): Unit =
    clearPatientsDiv()
    items.foreach (item => item match {
      case (patient, visit) => 
        val e = div(
          a(Helper.formatPatient(patient), onclick := (() => onItemClick(patient, visit)))
        )
        patientsDiv(e)
    })

  def onItemClick(patient: Patient, visit: Visit): Unit =
    PracticeBus.startPatient(patient, None)

object SelectPatientByDateWidget:
  def formatDate(date: LocalDate): String =
    KanjiDate.dateToKanji(date)

  def formatPatient(patient: Patient): String =
    String.format("[%04d] %s", patient.patientId, patient.fullName())

  def listPatients(date: LocalDate): Future[List[(Patient, Visit)]] =
    for
      visits <- Api.listVisitByDate(date)
      map <- Api.batchGetPatient(visits.map(_.patientId))
    yield visits.map(v => (map(v.patientId), v))

