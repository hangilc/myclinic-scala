package dev.myclinic.scala.web.reception.scan

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.model.Patient

class SelectedPatientDisp extends ScanBoxUIComponent:
  val ele = div(displayNone)
  var cache: Option[Patient] = None

  def updateUI(state: ScanBoxState): Unit =
    if cache != state.patient then
      state.patient match {
        case None => 
          ele.clear()
          ele(displayNone)
        case Some(p) =>
          ele.innerText = formatPatient(p)
          ele(displayDefault)
      }
      cache = state.patient

  private def formatPatient(patient: Patient): String =
    String.format("(%04d) %s", patient.patientId, patient.fullName())


