package dev.myclinic.scala.web.appbase.patientdialog

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.{TransNode, TransNodeRuntime}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.appbase.PatientReps

case class State(dialog: ModalDialog3, patient: Patient, hokenList: List[Hoken])

object PatientDialog:
  def open(patient: Patient): Unit =
    val dialog = new ModalDialog3()
    dialog.setTitle("")
    val state = State(dialog, patient, List.empty)
    val runtime = new TransNodeRuntime[State]
    runtime.run(s => Main(s), state, s => s.dialog.close())
    dialog.open()


