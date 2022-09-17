package dev.myclinic.scala.web.appbase.patientdialog

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.{TransNode, TransNodeRuntime}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.appbase.PatientReps
import dev.myclinic.scala.apputil.HokenUtil
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.web.appbase.reception.PatientImagesDialog

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
              onclick := (() => dispatchDispHoken(h, state))
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
            // next(GoForward(doRegister, state))
          })
        ),
        button("閉じる", onclick := (() => goExit()))
      ),
      div(
        cls := "domq-mt-4 reception-cashier-patient-search-result-dialog-disp-link-commands",
        a("編集", onclick := (() => {
          // next(GoForward(editPatient, state))
        })),
        "|",
        a(
          "新規社保国保",
          onclick := (() => {
            // next(GoForward(newShahokokuho(new ShahokokuhoInputs(None)), state))
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
                // next(GoForward(newKoukikourei(inputs), state))
              }
            ()
          )
        ),
        "|",
        a(
          "新規公費",
          onclick := (() => {
            // next(GoForward(newKouhi(new KouhiInputs(None)), state))
          })
        ),
        "|",
        a("保険履歴", onclick := (() => {
          // next(GoForward(hokenHistory, state))
        })),
        "|",
        a("保存画像", onclick := (() => PatientImagesDialog.open(state.patient)))
      )
    )

  private def dispatchDispHoken(
      hoken: Hoken,
      state: State
  ): Unit =
    hoken match {
      case s: Shahokokuho =>
        // next(
        //   GoForward(dispShahokokuho(s.shahokokuhoId), state)
        // )
        ()
      case k: Koukikourei =>
        // next(
        //   GoForward(dispKoukikourei(k.koukikoureiId), state)
        // )
        ()
      case k: Kouhi =>
        // next(GoForward(dispKouhi(k.kouhiId), state))
        ()
      case r: Roujin =>
        // next(GoForward(dispRoujin(r.roujinId), state))
        ()
    }


