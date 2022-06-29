package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.myclinic.scala.webclient.{Api, global}
import java.time.LocalDateTime
import java.time.LocalDate
import scala.concurrent.Future
import dev.myclinic.scala.apputil.HokenUtil

case class PatientSearchResultDialog(patients: List[Patient]):
  val selection = Selection[Patient](patients, p => div(format(p)))
  selection.onSelect(patient => { invokeDisp(patient); () })
  val dlog = new ModalDialog3()
  dlog.content(cls := "reception-cashier-search-patient-result-dialog")
  dlog.title("患者検索結果")
  dlog.body(selection.ele(cls := "selection"))
  dlog.commands(
    button("閉じる", onclick := (() => dlog.close()))
  )
  if patients.size == 1 then invokeDisp(patients.head)

  def open(): Unit =
    dlog.open()

  def format(patient: Patient): String =
    String.format(
      "(%04d) %s %s",
      patient.patientId,
      patient.lastName,
      patient.firstName
    )

  private def listHoken(patientId: Int): Future[List[Hoken]] =
    for
      result <- Api.getPatientHoken(patientId, LocalDate.now())
      (_, _, shahokokuho, koukikourei, roujin, kouhi) = result
    yield List.empty[Hoken] ++ shahokokuho ++ koukikourei ++ roujin ++ kouhi

  private def invokeDisp(patient: Patient): Future[Unit] =
    for hoken <- listHoken(patient.patientId)
    yield disp(patient, hoken)

  private def disp(patient: Patient, hokenList: List[Hoken]): Unit =
    val hokenArea = div
    val disp = PatientDisp(patient)
    dlog.body(
      clear,
      disp.ele,
      hokenArea(cls := "hoken-area", hokenList.map(h => {
        a(HokenUtil.hokenRep(h))
      }))
    )
    dlog.commands(
      clear,
      div(
        button("診察受付", onclick := (() => doRegister(patient.patientId))),
        button("閉じる", onclick := (() => dlog.close()))
      ),
      div(
        cls := "domq-mt-4",
        a("編集", onclick := (() => edit(patient))),
        "|",
        a("新規社保国保", onclick := (() => newShahokokuho(patient))), "|",
        a("新規後期高齢", onclick := (() => newKoukikourei(patient, hokenList)))
      )
    )

  private def newShahokokuho(patient: Patient): Unit =
    val form = ShahokokuhoForm(None)
    val errBox = ErrorBox()
    dlog.body(clear, form.ele, errBox.ele)
    dlog.commands(
      clear,
      button("入力", onclick := (() => {
        form.validateForEnter(patient.patientId) match {
          case Left(msg) => errBox.show(msg)
          case Right(newShahokokuho) => 
            for
              entered <- Api.enterShahokokuho(newShahokokuho)
            yield invokeDisp(patient)
        }
        ()
      })),
      button("キャンセル", onclick := (() => { invokeDisp(patient); () }))
    )

  private def newKoukikourei(patient: Patient, hokenList: List[Hoken]): Unit =
    val form = new KoukikoureiForm(None)
    val errBox = ErrorBox()
    dlog.body(clear, form.ele, errBox.ele)
    dlog.commands(clear,
      button("入力", onclick := (() => {
        form.validateForEnter(patient.patientId) match {
          case Left(msg) => errBox.show(msg)
          case Right(newKoukikourei) =>
            for
              entered <- Api.enterKoukikourei(newKoukikourei)
            yield invokeDisp(patient)
        }
        ()
      })),
      button("キャンセル", onclick := (() => disp(patient, hokenList)))
    )

  private def edit(patient: Patient): Unit =
    val panel = PatientForm(Some(patient))
    val errBox = ErrorBox()
    dlog.body(clear, panel.ele, errBox.ele)
    dlog.commands(
      clear,
      button(
        "入力",
        onclick := (() => {
          panel.validateForUpdate match {
            case Left(msg) => errBox.show(msg)
            case Right(newPatient) =>
              for
                _ <- Api.updatePatient(newPatient)
                updated <- Api.getPatient(patient.patientId)
                _ <- invokeDisp(updated)
              yield ()
          }
          ()
        })
      ),
      button("キャンセル", onclick := (() => { invokeDisp(patient); () }))
    )

  private def doRegister(patientId: Int): Unit =
    for _ <- Api.startVisit(patientId, LocalDateTime.now())
    yield dlog.close()
