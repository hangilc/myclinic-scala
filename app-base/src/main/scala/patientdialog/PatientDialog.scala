package dev.myclinic.scala.web.appbase.patientdialog

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.{TransNode, TransNodeRuntime}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.appbase.PatientReps
import dev.myclinic.scala.util.NumberUtil.format
import scala.concurrent.Future
import dev.myclinic.scala.webclient.{Api, global}
import java.time.LocalDate

case class State(dialog: ModalDialog3, patient: Patient, hokenList: List[Hoken]):
  def add(hoken: Hoken): State =
    copy(hokenList = hokenList :+ hoken)

object PatientDialog:
  def open(patient: Patient): Unit =
    for
      hokenList <- listHoken(patient.patientId)
    yield
      val dialog = new ModalDialog3()
      dialog.setTitle("")
      val state = State(dialog, patient, hokenList)
      val runtime = new TransNodeRuntime[State]
      runtime.run(s => Main(s), state, s => s.dialog.close())
      dialog.open()

  private def listHoken(patientId: Int): Future[List[Hoken]] =
    for
      result <- Api.getPatientHoken(patientId, LocalDate.now())
      (_, _, shahokokuho, koukikourei, roujin, kouhi) = result
    yield List.empty[Hoken] ++ shahokokuho ++ koukikourei ++ roujin ++ kouhi


