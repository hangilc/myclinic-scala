package dev.myclinic.scala.web.reception.scan

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.model.Patient

class PatientDisp:
  val disp = span
  val ele = div(
    h2("患者"),
    disp,
    a("変更")
  )
  def setPatient(patient: Patient): Unit =
    disp(innerText := formatPatient(patient))

  private def formatPatient(patient: Patient): String =
    String.format("(%04d) %s", patient.patientId, patient.fullName())

