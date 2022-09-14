package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.reception.records.RecordUI
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.web.reception.records.VisitBlock

class RecordDialog(patient: Patient, focusVisitId: Int = 0):
  val rec = new RecordUI(patient.patientId, modifyVisitBlock)
  rec.init()
  val dlog = new ModalDialog3()
  dlog.content(cls := "reception-cashier-record-dialog")
  dlog.setTitle("診療記録")
  dlog.body(
    div(cls := "patient-block", patientBlock),
    rec.ele
  )

  private def modifyVisitBlock(vb: VisitBlock): Unit =
    if vb.visitId == focusVisitId then vb.ele(cls := "focus")

  def open(): Unit = dlog.open()

  private def patientBlock: String =
    s"(${patient.patientId}) ${patient.lastName} ${patient.firstName}"

