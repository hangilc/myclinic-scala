package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.transition.*
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.reception.selectpatientlink.SearchPatientBox
import java.time.LocalDate
import dev.myclinic.scala.webclient.{Api, global}
import scala.util.Failure
import scala.util.Success

class MishuuDialog:
  case class State()
  type Node = TransitionNode[State]
  type Edge = Transition[State]

  val dlog = new ModalDialog3()

  def open(): Unit =
    dlog.open()
    Transition.run[State](searchNode, State(), List.empty, () => dlog.close())

  def searchNode(state: State, next: Edge => Unit): Unit =
    val enter = button
    val search = new SearchPatientBox(_ => enter(disabled := false))
    val errBox = ErrorBox()
    dlog.title(clear, "未収処理（患者検索）")
    dlog.body(
      clear,
      search.ele,
      errBox.ele
    )
    dlog.commands(
      clear,
      enter("選択", disabled := true, onclick := (onEnter _)),
      button("閉じる", onclick := (() => dlog.close()))
    )
    search.initFocus()

    def onEnter(): Unit =
      search.selection.marked.foreach(patient =>
        val since = LocalDate.now().minusYears(1)
        (
          for
            visits <- Api.listVisitSince(patient.patientId, since)
            chargePays <- Api.batchGetChargePayment(visits.map(_.visitId))
          yield
            visits.zip(chargePays)
              .filter{ (v, cp) => cp._2.isDefined && cp._3.isEmpty}
              .map { (v, cp) => (v, cp._2.get)}
        ).onComplete {
            case Failure(ex) => errBox.show(ex.toString)
            case Success(result) => 
              GoForward(showMishuu(search.selection.marked.get, result)), state)
          }
      )

  def showMishuu(patient: Patient, mishuuList: List[(Visit, Charge)])(
      state: State,
      next: Edge => Unit
  ): Unit =
    ???
