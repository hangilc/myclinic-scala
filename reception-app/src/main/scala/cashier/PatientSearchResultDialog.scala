package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.myclinic.scala.webclient.{Api, global}
import java.time.LocalDateTime
import java.time.LocalDate

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
  if patients.size == 1 then
    val patient = patients.head
    for
      hoken <- Api.getPatientHoken(patient.patientId, LocalDate.now())
    disp(patient)

  def open(): Unit =
    dlog.open()

  def format(patient: Patient): String =
    String.format("(%04d) %s %s", patient.patientId, patient.lastName, patient.firstName)

  private def disp(patient: Patient, hokenList: List[Hoken]): Unit =
    val disp = PatientDisp(patient)
    dlog.body(clear, disp.ele)
    dlog.commands(clear,
      div(
      button("診察受付", onclick := (() => doRegister(patient.patientId))),
      button("閉じる", onclick := (() => dlog.close()))
      ),
      div(cls := "domq-mt-4",
        a("編集", onclick := (() => edit(patient))), "|", 
        a("新規社保国保", onclick := (() => newShahokokuho(patient)))
      )
    )

  private def newShahokokuho(patient: Patient): Unit =
    val form = ShahokokuhoForm(None)
    dlog.body(clear, form.ele)
    dlog.commands(clear,
      button("キャンセル", onclick := (() => disp(patient)))
    )

  private def edit(patient: Patient): Unit =
    val panel = PatientForm(Some(patient))
    val errBox = ErrorBox()
    dlog.body(clear, panel.ele, errBox.ele)
    dlog.commands(clear,
      button("入力", onclick := (() => {
        panel.validateForUpdate match {
          case Left(msg) => errBox.show(msg)
          case Right(newPatient) => 
            for
              _ <- Api.updatePatient(newPatient)
              updated <- Api.getPatient(patient.patientId)
            yield 
              disp(updated)
        }
        ()
      })),
      button("キャンセル", onclick := (() => disp(patient)))
    )

  private def doRegister(patientId: Int): Unit =
    for
      _ <- Api.startVisit(patientId, LocalDateTime.now())
    yield
      dlog.close()


