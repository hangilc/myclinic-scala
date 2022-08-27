package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.all.{*, given}
import java.time.LocalDate
import dev.fujiwara.kanjidate.KanjiDate
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.model.Patient
import dev.myclinic.scala.model.Visit
import scala.concurrent.Future
import scala.language.implicitConversions
import dev.myclinic.scala.web.practiceapp.PracticeBus

class SelectPatientByDateWidget:
  import SelectPatientByDateWidget as Helper
  val patientSelected = new LocalEventPublisher[Patient]
  val origDate: LocalDate = LocalDate.now()
  val dateSpan = span
  val patientsDiv = div
  val content = div(
    dateSpan,
    patientsDiv,
    div(
      cls := "domq-text-align-end",
      button("閉じる", onclick := (doClose _))
    )
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
    patientSelected.publish(patient)

  private def doClose(): Unit =
    PracticeBus.removeRightWidgetRequest.publish(widget)

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

