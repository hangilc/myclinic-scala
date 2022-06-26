package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.myclinic.scala.webclient.{Api, global}
import java.time.LocalDateTime

case class PatientSearchResultDialog(patients: List[Patient]):
  val selection = Selection[Patient](patients, p => div(format(p)))
  selection.onSelect(disp _)
  val dlog = new ModalDialog3()
  dlog.content(cls := "reception-cashier-search-patient-result-dialog")
  dlog.title("患者検索結果")
  dlog.body(selection.ele(cls := "selection"))
  dlog.commands(
    button("閉じる", onclick := (() => dlog.close()))
  )

  def open(): Unit =
    dlog.open()

  def format(patient: Patient): String =
    String.format("(%04d) %s %s", patient.patientId, patient.lastName, patient.firstName)

  private def disp(patient: Patient): Unit =
    val disp = PatientDisp(patient)
    dlog.body(clear, disp.ele)
    dlog.commands(clear,
      div(
      button("診察受付", onclick := (() => doRegister(patient.patientId))),
      button("閉じる", onclick := (() => dlog.close()))
      ),
      div(cls := "domq-mt-4",
        a("編集", onclick := (() => edit(patient)))
      )
    )

  private def edit(patient: Patient): Unit =
    val panel = PatientForm(Some(patient))
    dlog.body(clear, panel.ele)
    dlog.commands(clear)

  private def doRegister(patientId: Int): Unit =
    for
      _ <- Api.startVisit(patientId, LocalDateTime.now())
    yield
      dlog.close()


