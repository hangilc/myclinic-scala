package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.Patient

class PatientDisplay:
  import PatientDisplay as Helper
  val nameSpan = span
  val ele = div(
    nameSpan
  )

  PracticeBus.patientChanged.subscribe(optPatient => optPatient match {
    case Some(patient) => 
      nameSpan(innerText := Helper.formatPatient(patient))
      ele(displayDefault)
    case None => ele(displayNone)
  })

object PatientDisplay:
  def formatPatient(patient: Patient): String =
    String.format("[%d] %s", patient.patientId, patient.fullName())

