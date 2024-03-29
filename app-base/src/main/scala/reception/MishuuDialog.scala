package dev.myclinic.scala.web.appbase.reception

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.transition.*
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.appbase.selectpatientlink.SearchPatientBox
import java.time.LocalDate
import dev.myclinic.scala.webclient.{Api, global}
import scala.util.Failure
import scala.util.Success
import dev.fujiwara.kanjidate.KanjiDate
import org.scalajs.dom.HTMLElement
import cats.syntax.all.*

class MishuuDialog:
  case class State()
  type Node = TransitionNode[State]
  type Edge = Transition[State]

  val dlog = new ModalDialog3()

  def open(): Unit =
    dlog.open(Some("mishuu-dialog"))
    Transition.run[State](searchNode, State(), List.empty, () => dlog.close())

  def searchNode(state: State, next: Edge => Unit): Unit =
    val errBox = ErrorBox()
    def onSelectPatient(patient: Patient): Unit =
      (for result <- Api.listMishuuForPatient(patient.patientId, 10)
      yield result.map { (visit, charge) =>
        (visit, charge.charge)
      }).onComplete {
        case Failure(ex) => errBox.show(ex.toString)
        case Success(result) =>
          next(GoForward(showMishuu(patient, result), state))
      }
    val enter = button
    val search = new SearchPatientBox(
      _ => enter(disabled := false),
      patient => onSelectPatient(patient)
    )
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

    // def onEnter(): Unit =
    //   search.selection.marked.foreach(patient =>
    //     val since = LocalDate.now().minusYears(1)
    //     (
    //       for
    //         visits <- Api.listVisitByPatientReverse(patient.patientId, 0, 10)
    //         chargePays <- Api.batchGetChargePayment(visits.map(_.visitId))
    //         wqList <- Api.listWqueue()
    //         wqVisitIds = wqList.map(_.visitId)
    //       yield
    //         println(("wqVisitIds", wqVisitIds))
    //         println(("visitIds", visits.map(_.visitId)))
    //         visits
    //           .zip(chargePays)
    //           .map((v, cp) => (v, MishuuDialog.mishuuAmount(cp._2, cp._3)))
    //           .collect { case (v, Some(deficit)) =>
    //             (v, deficit)
    //           }
    //           .filter((v, _) => !wqVisitIds.contains(v.visitId))
    //     ).onComplete {
    //       case Failure(ex) => errBox.show(ex.toString)
    //       case Success(result) =>
    //         next(
    //           GoForward(showMishuu(search.selection.marked.get, result), state)
    //         )
    //     }
    //   )

    def onEnter(): Unit =
      search.selection.marked.foreach(patient => onSelectPatient(patient))

  def showMishuu(patient: Patient, mishuuList: List[(Visit, Int)])(
      state: State,
      next: Edge => Unit
  ): Unit =
    val checkLabels = mishuuList.map((v, c) =>
      val cl = CheckLabel[Visit](
        v,
        _(
          KanjiDate.dateToKanji(v.visitedAt.toLocalDate),
          " ",
          s"${c}円"
        )
      )
      cl.checkElement(
        attr("data-visit-id") := v.visitId.toString
      )
      cl
    )
    checkLabels.foreach(_.check(true))
    val errBox = ErrorBox()
    def mishuuItems: List[HTMLElement] =
      if checkLabels.isEmpty then List(div("追加する未収はありません。"))
      else checkLabels.map(_.wrap(div))
    def doEnter(): Unit =
      (for
        wqList <- Api.listWqueue()
        wqVisitIds = wqList.map(_.visitId)
        _ <- checkLabels
          .filter(_.isChecked)
          .filter(cl => !wqVisitIds.contains(cl.value.visitId))
          .map(cl =>
            val wq = Wqueue(cl.value.visitId, WaitState.WaitCashier)
            Api.enterWqueue(wq)
          )
          .sequence_
      yield ()).onComplete {
        case Success(_)  => next(Exit())
        case Failure(ex) => errBox.show(ex.toString)
      }
    dlog.title(clear, "未収処理")
    dlog.body(
      clear,
      s"(${patient.patientId}) ${patient.lastName}${patient.firstName}",
      div(
        cls := "mishuu-items-wrapper",
        mishuuItems
      ),
      errBox.ele
    )
    dlog.commands(
      clear,
      if checkLabels.isEmpty then None
      else Some(button("会計に加える", onclick := (doEnter _))),
      button("閉じる", onclick := (() => next(Exit())))
    )

object MishuuDialog:
  def mishuuAmount(
      chargeOpt: Option[Charge],
      paymentOpt: Option[Payment]
  ): Option[Int] =
    (chargeOpt, paymentOpt) match {
      case (Some(_, charge), None)                              => Some(charge)
      case (Some(_, charge), Some(_, amount, _)) if amount == 0 => Some(charge)
      case _                                                    => None
    }
