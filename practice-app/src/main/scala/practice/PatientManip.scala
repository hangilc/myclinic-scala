package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.web.practiceapp.practice.patientmanip.CashierDialog
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.model.WaitState
import java.time.LocalDateTime
import scala.concurrent.Future
import dev.myclinic.scala.web.practiceapp.practice.patientmanip.SearchTextDialog
import dev.myclinic.scala.web.practiceapp.practice.patientmanip.PatientImageUploadIdalog
import dev.myclinic.scala.web.practiceapp.practice.patientmanip.GazouListDialog
import scala.language.implicitConversions
import dev.fujiwara.domq.SingleTask
import dev.myclinic.scala.model.Visit
import dev.myclinic.scala.model.Wqueue
import dev.myclinic.scala.model.Meisai
import dev.myclinic.scala.web.practiceapp.PracticeBus
import dev.myclinic.scala.web.practiceapp.practice.PatientStateController.State

object PatientManip:
  val cashierButton = button

  val ele = div(
    displayNone,
    attr("id") := "practice-patient-manip",
    cashierButton("会計", onclick := (doCashier _)),
    button("患者終了", onclick := (doEndPatient _)),
    a("診察登録", onclick := (doRegisterPractice _)),
    a("文章検索", onclick := (doSearchText _)),
    a("画像保存", onclick := (doImageUpload _)),
    a("画像一覧", onclick := (doGazouList _))
  )

  PracticeBus.patientStartingSubscriberChannel.subscribe(s => {
    ele(displayDefault)
    s match {
      case State(patient, Some(visitId)) => {
        cashierButton(enabled := true)
      }
      case State(patient, None) => {
        cashierButton(enabled := false)
      }
    }
  })
  PracticeBus.patientClosingSubscriberChannel.subscribe(_ => {
    ele(displayNone)
  })

  // PracticeBus.patientVisitChanged.subscribe {
  //   case NoSelection => ele(displayNone)
  //   case Browsing(_) | PracticeDone(_, _) =>
  //     ele(displayDefault)
  //     cashierButton(enabled := false)
  //   case Practicing(_, _) =>
  //     ele(displayDefault)
  //     cashierButton(enabled := true)
  // }

  def doGazouList(): Unit =
    PracticeBus.currentPatient.map(_.patientId).foreach(patientId => 
      for
        files <- Api.listPatientImage(patientId)
      yield
        val dlog = GazouListDialog(patientId, files)
        dlog.open()
    )

  def doImageUpload(): Unit =
    PracticeBus.currentPatient.map(_.patientId).foreach(patientId => 
      val dlog = PatientImageUploadIdalog(patientId)
      dlog.open()
    )

  def doSearchText(): Unit =
    PracticeBus.currentPatient.foreach(patient =>
      val dlog = SearchTextDialog(patient.patientId)
      dlog.open()
    )

  private val registerPracticeTask = new SingleTask[Visit]()

  def doRegisterPractice(): Unit =
    PracticeBus.currentPatient.foreach(patient =>
      val fut = Api.startVisit(patient.patientId, LocalDateTime.now())
      registerPracticeTask.run(fut, _ => 
        RecordsHelper.refreshRecords(PracticeBus.navPageChanged.currentValue)
      )
    )

  def doEndPatient(): Unit =
    // PracticeBus.setPatientVisitState(NoSelection)
    PracticeBus.patientStateController.endPatient()

  private val cashierTask = new SingleTask[Meisai]
  private val updateWqueueStateTask = new SingleTask[Wqueue]

  def doCashier(): Unit =
    PracticeBus.currentPatientState match {
      case Some(State(patientId, Some(visitId))) => {
        cashierTask.run(Api.getMeisai(visitId), meisai =>
            val dlog = CashierDialog(meisai, visitId, () =>
              updateWqueueStateTask.run(Api.changeWqueueState(visitId, WaitState.WaitCashier), _ =>
                PracticeBus.patientStateController.endPatient()
              ))
            dlog.open()
        )
      }
      case _ => ()
    }

    // PracticeBus.currentPatientVisitState match {
    //   case Practicing(patient, visitId) => 
    //     cashierTask.run(Api.getMeisai(visitId), meisai =>
    //         val dlog = CashierDialog(meisai, visitId, () =>
    //           updateWqueueStateTask.run(Api.changeWqueueState(visitId, WaitState.WaitCashier), _ =>
    //             PracticeBus.setPatientVisitState(PracticeDone(patient, visitId))
    //             PracticeBus.setPatientVisitState(NoSelection)
    //           ))
    //         dlog.open()
    //     )
    //   case _ => ()
    // }
