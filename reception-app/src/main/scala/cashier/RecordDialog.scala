package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.reception.records.RecordUI
import org.scalajs.dom.HTMLElement

class RecordDialog(patient: Patient):
  val rec = new RecordUI(patient.patientId)
  rec.init()
  val dlog = new ModalDialog3()
  dlog.content(cls := "reception-cashier-record-dialog")
  dlog.title("診療記録")
  dlog.body(
    div(cls := "patient-block", patientBlock),
    rec.ele
  )
  dlog.commands(
    button("閉じる", onclick := (() => dlog.close()))
  )

  def open(): Unit = dlog.open()

  private def patientBlock: String =
    s"(${patient.patientId}) ${patient.lastName} ${patient.firstName}"

