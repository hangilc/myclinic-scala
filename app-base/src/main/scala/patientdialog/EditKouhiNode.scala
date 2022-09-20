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
import dev.myclinic.scala.web.appbase.PatientInputs
import Common.*
import dev.myclinic.scala.web.appbase.KouhiInputs

case class EditKouhiNode(kouhi: Kouhi, state: State)
    extends TransNode[State](state):
  override def init(): Unit =
    val dlog = state.dialog
    val inputs = new KouhiInputs(Some(kouhi))
    val errBox = ErrorBox()
    dlog.changeTitle("公費")
    dlog.body(
      clear,
      patientBlock(state.patient),
      inputs.formPanel,
      errBox.ele
    )
    dlog.commands(
      clear,
      button(
        "入力",
        onclick := (() =>
          inputs.validateForUpdate() match {
            case Right(formKouhi) =>
              checkKenshoDigit(formKouhi.futansha, () => {
                for
                  _ <- Api.updateKouhi(formKouhi)
                  updated <- Api.getKouhi(kouhi.kouhiId)
                yield 
                  goBack(state.add(updated))
              })
            case Left(msg) => errBox.show(msg)
          }
          ()
        )
      ),
      button("戻る", onclick := (() => goBack()))
    )