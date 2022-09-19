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
import Common.*

case class State(
    dialog: ModalDialog3,
    patient: Patient,
    hokenList: List[Hoken]
):
  import Hoken.HokenId
  def add(hoken: Hoken): State =
    val hokenId = HokenId(hoken)
    val (pre, post) = hokenList.span(h => HokenId(h) != hokenId)
    val newList =
      if post.isEmpty then pre :+ hoken
      else (pre :+ hoken) ++ post.tail
    copy(hokenList = newList)
  def remove(hoken: Hoken): State =
    val hokenId = HokenId(hoken)
    copy(hokenList = hokenList.filter(h => HokenId(h) != hokenId))

object PatientDialog:
  def open(patient: Patient): Unit =
    for hokenList <- listCurrentHoken(patient.patientId)
    yield
      val dialog = new ModalDialog3()
      dialog.setTitle("")
      val state = State(dialog, patient, hokenList)
      val runtime = new TransNodeRuntime[State]
      runtime.run(s => Main(s), state, s => s.dialog.close())
      dialog.open()
