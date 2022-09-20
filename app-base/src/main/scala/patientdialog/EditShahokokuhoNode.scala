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

case class EditShahokokuhoNode(shahokokuho: Shahokokuho, state: State)
    extends TransNode[State](state):
  override def init(): Unit =
    val dlog = state.dialog
    val inputs = new ShahokokuhoInputs(Some(shahokokuho))
    val errBox = ErrorBox()
    dlog.changeTitle("社保国保編集")
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
            case Left(msg) => errBox.show(msg)
            case Right(newShahokokuho) =>
              checkKenshoDigit(newShahokokuho.hokenshaBangou, () => {
                for
                  _ <- Api.updateShahokokuho(newShahokokuho)
                  updated <- Api.getShahokokuho(shahokokuho.shahokokuhoId)
                yield 
                  goBack(state.add(updated))
              })
              ()
          }
        )
      ),
      button("キャンセル", onclick := (() => goBack()))
    )