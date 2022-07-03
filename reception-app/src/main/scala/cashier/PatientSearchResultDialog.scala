package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.myclinic.scala.webclient.{Api, global}
import java.time.LocalDateTime
import java.time.LocalDate
import scala.concurrent.Future
import dev.myclinic.scala.apputil.HokenUtil
import dev.myclinic.scala.web.appbase.PatientProps
import dev.myclinic.scala.web.appbase.ShahokokuhoProps
import dev.myclinic.scala.web.appbase.KoukikoureiProps
import dev.myclinic.scala.web.appbase.KouhiProps
import org.scalajs.dom.HTMLElement

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

  type Modified = Boolean

  private def disp(patient: Patient, hokenList: List[Hoken]): Unit =
    val hokenArea = div
    val dispElement = PatientProps(Some(patient)).updateDisp().dispPanel
    def onHokenDispDone(modified: Boolean): Unit =
      if modified then invokeDisp(patient)
      else disp(patient, hokenList)
    dlog.body(
      clear,
      div(
        cls := "reception-cashier-patient-search-result-dialog-disp-body",
        dispElement,
        hokenArea(
          cls := "hoken-area",
          hokenList.map(h => {
            a(
              HokenUtil.hokenRep(h),
              onclick := (() => {
                h match {
                  case s: Shahokokuho => dispShahokokuho(s, patient, onHokenDispDone _)
                  case k: Koukikourei => dispKoukikourei(k, patient, onHokenDispDone _)
                  case k: Kouhi       => dispKouhi(k, patient, onHokenDispDone _)
                  case r: Roujin      => dispRoujin(r, patient, onHokenDispDone _)
                }
                ()
              })
            )
          })
        )
      )
    )
    dlog.commands(
      clear,
      div(
        button("診察受付", onclick := (() => doRegister(patient.patientId))),
        button("閉じる", onclick := (() => dlog.close()))
      ),
      div(
        cls := "domq-mt-4 reception-cashier-patient-search-result-dialog-disp-link-commands",
        a("編集", onclick := (() => edit(patient))),
        "|",
        a("新規社保国保", onclick := (() => newShahokokuho(patient))),
        "|",
        a("新規後期高齢", onclick := (() => newKoukikourei(patient, hokenList))),
        "|",
        a("新規公費", onclick := (() => newKouhi(patient, hokenList)))
      )
    )

  private def dispShahokokuho(
      shahokokuho: Shahokokuho,
      patient: Patient,
      onDone: Modified => Unit
  ) =
    val props = ShahokokuhoProps(Some(shahokokuho)).updateDisp()
    dlog.body(clear, patientBlock(patient), props.dispPanel)
    dlog.commands(clear, 
          button("戻る", onclick := (() => onDone(false)))
    )

  private def dispKoukikourei(
      koukikourei: Koukikourei,
      patient: Patient,
      onDone: Modified => Unit
  ) =
    val props = KoukikoureiProps(Some(koukikourei)).updateDisp()
    dlog.body(clear, patientBlock(patient), props.dispPanel)
    dlog.commands(clear, 
          button("戻る", onclick := (() => onDone(false)))
    )

  private def dispKouhi(
      kouhi: Kouhi,
      patient: Patient,
      onDone: Modified => Unit
  ) =
    val props = KouhiProps(Some(kouhi)).updateDisp()
    dlog.body(clear, patientBlock(patient), props.dispPanel)
    dlog.commands(clear, 
          button("戻る", onclick := (() => onDone(false)))
    )

  private def dispRoujin(
      roujin: Roujin,
      patient: Patient,
      onDone: Modified => Unit
  ) =
    val props = RoujinProps(Some(roujin)).updateDisp()
    dlog.body(clear, patientBlock(patient), props.dispPanel)
    dlog.commands(clear, 
          button("戻る", onclick := (() => onDone(false)))
    )

  private def patientBlock(patient: Patient): HTMLElement =
    div(
      innerText := s"(${patient.patientId} ${patient.lastName} ${patient.firstName}",
      cls := "patient-block"
    )

  private def newShahokokuho(patient: Patient): Unit =
    val props = ShahokokuhoProps(None).updateInput()
    val errBox = ErrorBox()
    dlog.body(clear, patientBlock(patient), props.formPanel, errBox.ele)
    dlog.commands(
      clear,
      button(
        "入力",
        onclick := (() => {
          props.validatedForEnter(patient.patientId) match {
            case Left(msg) => errBox.show(msg)
            case Right(newShahokokuho) =>
              for entered <- Api.enterShahokokuho(newShahokokuho)
              yield invokeDisp(patient)
          }
          ()
        })
      ),
      button("キャンセル", onclick := (() => { invokeDisp(patient); () }))
    )

  private def newKoukikourei(patient: Patient, hokenList: List[Hoken]): Unit =
    val props = KoukikoureiProps(None)
    val errBox = ErrorBox()
    dlog.body(clear, props.formPanel, errBox.ele)
    dlog.commands(
      clear,
      button(
        "入力",
        onclick := (() => {
          props.validatedForEnter(patient.patientId) match {
            case Left(msg) => errBox.show(msg)
            case Right(newKoukikourei) =>
              for entered <- Api.enterKoukikourei(newKoukikourei)
              yield invokeDisp(patient)
          }
          ()
        })
      ),
      button("キャンセル", onclick := (() => disp(patient, hokenList)))
    )

  private def newKouhi(patient: Patient, hokenList: List[Hoken]): Unit =
    val props = KouhiProps(None)
    val errBox = ErrorBox()
    dlog.body(clear, props.formPanel, errBox.ele)
    dlog.commands(
      clear,
      button(
        "入力",
        onclick := (() => {
          props.validatedForEnter(patient.patientId) match {
            case Left(msg) => errBox.show(msg)
            case Right(newKouhi) =>
              for _ <- Api.enterKouhi(newKouhi)
              yield invokeDisp(patient)
          }
          ()
        })
      ),
      button("キャンセル", onclick := (() => disp(patient, hokenList)))
    )

  private def edit(patient: Patient): Unit =
    val props = PatientProps(Some(patient)).updateInput()
    val errBox = ErrorBox()
    dlog.body(clear, props.formPanel, errBox.ele)
    dlog.commands(
      clear,
      button(
        "入力",
        onclick := (() => {
          props.validatedForUpdate match {
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
