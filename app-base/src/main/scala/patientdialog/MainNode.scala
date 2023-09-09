package dev.myclinic.scala.web.appbase.patientdialog

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.{TransNode, TransNodeRuntime}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.appbase.PatientReps
import dev.myclinic.scala.apputil.HokenUtil
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.web.appbase.reception.PatientImagesDialog
import java.time.LocalDateTime
import scala.util.Success
import scala.util.Failure

case class Main(state: State) extends TransNode[State](state):
  import Common.*
  override def init(): Unit =
    val dlog = state.dialog
    dlog.changeTitle("患者情報")
    val hokenArea = div
    val dispElement = new PatientReps(Some(state.patient)).dispPanel
    dlog.body(
      clear,
      div(
        cls := "reception-cashier-patient-search-result-dialog-disp-body",
        dispElement,
        hokenArea(
          cls := "hoken-area",
          state.hokenList.map(h => {
            a(
              HokenUtil.hokenRep(h),
              onclick := (() => dispatchDispHoken(h))
            )
          })
        )
      )
    )
    dlog.commands(
      clear,
      div(
        button(
          "診察受付",
          onclick := (() => {
            doRegister()
          })
        ),
        button("閉じる", onclick := (() => goExit()))
      ),
      div(
        cls := "domq-mt-4 reception-cashier-patient-search-result-dialog-disp-link-commands",
        a("編集", onclick := (() => {
          goForward(EditPatientNode.apply, state)
        })),
        "|",
        a(
          "新規社保国保",
          onclick := (() => {
            println("CLICK")
            goForward(NewShahokokuhoNode.apply, state)
          })
        ),
        "|",
        a(
          "新規後期高齢",
          onclick := (() =>
            for
              inputs <- createKoukikoureiInputs(None)
            yield
              {
                goForward(s => NewKoukikoureiNode(state, inputs))
              }
            ()
          )
        ),
        "|",
        a(
          "新規公費",
          onclick := (() => {
            goForward(NewKouhiNode.apply)
          })
        ),
        "|",
        a("保険履歴", onclick := (() => {
          goForward(HokenHistoryNode.apply)
        })),
        "|",
        a("保存画像", onclick := (() => PatientImagesDialog.open(state.patient)))
      )
    )

  private def dispatchDispHoken(
      hoken: Hoken
  ): Unit =
    hoken match {
      case s: Shahokokuho =>
        goForward(state => EditShahokokuhoNode(s, state))
      case k: Koukikourei =>
        goForward(state => EditKoukikoureiNode(k, state))
      case k: Kouhi =>
        goForward(state => EditKouhiNode(k, state))
      case r: Roujin =>
        ()
    }

  private def doRegister(): Unit =
    val patient: Patient = state.patient
    Api.startVisit(patient.patientId, LocalDateTime.now()).onComplete {
      case Success(_) => goExit()
      case Failure(ex) => ShowMessage.showError(ex.toString)
    }



