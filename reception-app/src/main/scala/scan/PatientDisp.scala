package dev.myclinic.scala.web.reception.scan

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.model.Patient

class PatientDisp:
  val ele = div(displayNone)

  def setPatient(patient: Patient): Unit =
    ele.innerText = formatPatient(patient)
    ele(displayDefault)

  private def formatPatient(patient: Patient): String =
    String.format("(%04d) %s", patient.patientId, patient.fullName())


