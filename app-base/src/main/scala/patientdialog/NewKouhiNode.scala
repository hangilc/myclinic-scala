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
import dev.myclinic.scala.web.appbase.ShahokokuhoInputs
import dev.myclinic.scala.web.appbase.KoukikoureiInputs
import dev.myclinic.scala.web.appbase.KouhiInputs

case class NewKouhiNode(state: State) extends TransNode[State](state):
  override def init(): Unit =
    val inputs = new KouhiInputs(None)
    val errBox = ErrorBox()
    val dlog = state.dialog
    dlog.changeTitle("公費入力")
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
          inputs.validateForEnter(state.patient.patientId) match {
            case Left(msg) => errBox.show(msg)
            case Right(formKouhi) =>
              for entered <- Api.enterKouhi(formKouhi)
              yield goBack(state.add(entered))
          }
          ()
        )
      ),
      button("キャンセル", onclick := (() => goBack()))
    )

