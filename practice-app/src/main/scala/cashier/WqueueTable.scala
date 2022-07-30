package dev.myclinic.scala.web.practiceapp.cashier

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.domq.CompListSortList
import dev.fujiwara.domq.TypeClasses.{CompList, Dispose, given}
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.model.*
import dev.fujiwara.kanjidate.KanjiDate
import java.time.LocalDateTime
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.web.practiceapp.PracticeBus
import scala.concurrent.Future

class WqueueTable:
  import WqueueTable.Item
  val unsubs = List(
    PracticeBus.wqueueEntered.subscribe(onWqueueEntered),
    PracticeBus.wqueueUpdated.subscribe(onWqueueUpdated),
    PracticeBus.wqueueDeleted.subscribe(onWqueueDeleted),
    PracticeBus.chargeUpdated.subscribe(onChargeUpdated),
  )
  val ele = div(cls := "practice-cashier-wqueue-table", titles)
  val wqueueList = new CompListSortList[Item](ele)

  def add(wqueue: Wqueue, visit: Visit, patient: Patient, charge: Int): Unit =
    wqueueList.add(Item(wqueue, visit, patient, charge))

  def add(wqueue: Wqueue): Unit =
    for
      tuple <- collectData(wqueue)
    yield add.tupled(tuple)

  private def collectData(wqueue: Wqueue): Future[(Wqueue, Visit, Patient, Int)] =
    for
      visit <- Api.getVisit(wqueue.visitId)
      patient <- Api.getPatient(visit.patientId)
      charge <- Api.getCharge(wqueue.visitId)
    yield (wqueue, visit, patient, charge.charge)

  def clear(): Unit =
    wqueueList.clear()

  def dispose(): Unit =
    unsubs.foreach(_.proc())

  def titles: List[HTMLElement] =
    List(
      div("患者"),
      div("診察日"),
      div("請求額"),
      div("操作")
    )

  private def onWqueueEntered(wqueue: Wqueue): Unit =
    if wqueue.waitState == WaitState.WaitCashier then add(wqueue)

  private def onWqueueUpdated(wqueue: Wqueue): Unit =
    println(("updated", wqueue))
    if wqueue.waitState == WaitState.WaitCashier then
      wqueueList.list
        .find(_.wqueue.visitId == wqueue.visitId)
        .fold(add(wqueue))(item =>
          for
            data <- collectData(wqueue)
          yield
            val newItem = Item.apply.tupled(data)
            println(("modify", item, newItem))
            wqueueList.replace(item, newItem)
        )
    else wqueueList.remove(_.wqueue.visitId == wqueue.visitId)

  private def onWqueueDeleted(wqueue: Wqueue): Unit =
    wqueueList.remove(_.wqueue.visitId == wqueue.visitId)

  private def onChargeUpdated(charge: Charge): Unit =
    wqueueList.list.find(_.wqueue.visitId == charge.visitId).foreach(item =>
      val newItem = Item(item.wqueue, item.visit, item.patient, charge.charge)
      wqueueList.replace(item, newItem)
    )

object WqueueTable:
  case class Item(wqueue: Wqueue, visit: Visit, patient: Patient, charge: Int):
    val eles: List[HTMLElement] =
      List(
        elePatient,
        eleVisitDate,
        eleCharge,
        div(
          button("未収会計", onclick := (onMishuu _))
        )
      )

    private def onMishuu(): Unit =
      ShowMessage.confirm("この会計を未収処理にしますか？")(doMishuu)

    private def doMishuu(): Unit =
      val pay = Payment(wqueue.visitId, 0, LocalDateTime.now())
      for _ <- Api.finishCashier(pay)
      yield ()

    def elePatient: HTMLElement =
      div(s"(${patient.patientId}) ${patient.lastName}${patient.firstName}")
    def eleVisitDate: HTMLElement =
      div(KanjiDate.dateToKanji(visit.visitedAt.toLocalDate))
    def eleCharge: HTMLElement =
      div(s"${charge}円")

  given Ordering[Item] = Ordering.by(_.wqueue.visitId)
  given CompList[Item] = _.eles
  given Dispose[Item] = _ => ()
