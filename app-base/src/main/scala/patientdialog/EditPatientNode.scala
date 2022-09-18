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

case class EditPatientNode(state: State) extends TransNode[State](state):
  override def init(): Unit =
    val inputs = new PatientInputs(Some(state.patient))
    val errBox = ErrorBox()
    val dlog = state.dialog
    dlog.changeTitle("患者情報編集")
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
            case Right(formPatient) =>
              (
                for
                  _ <- Api.updatePatient(formPatient)
                  updated <- Api.getPatient(state.patient.patientId)
                yield updated
              ).onComplete {
                case Success(updated) => {
                  goBack(state.copy(patient = updated))
                }
                case Failure(ex) => errBox.show(ex.toString)
              }
          }
        )
      ),
      button(
        "キャンセル",
        onclick := (() => {
          goBack(state)
        })
      )
    )
