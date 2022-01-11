package dev.myclinic.scala.web.reception.scan.patientimages

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.{Patient}
import dev.myclinic.scala.web.reception.scan.{PatientDisp}

class PatientImages(ui: PatientImages.UI, patient: Patient):
  val ele = ui.ele
  val patientDisp = new PatientDisp(ui.patientDispUI)
  patientDisp.setPatient(patient)


object PatientImages:
  class UI:
    val patientDispUI = new PatientDisp.UI
    val ele = div(cls := "patient-images")(
      div("保存画像", fontWeight := "bold"),
      patientDispUI.ele
    )

  def apply(patient: Patient): PatientImages =
    val ui = new UI
    new PatientImages(ui, patient)


