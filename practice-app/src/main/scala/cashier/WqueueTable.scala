package dev.myclinic.scala.web.practiceapp.cashier

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.domq.CompListSortList
import dev.fujiwara.domq.TypeClasses.{CompList, Dispose, given}
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.model.*
import dev.fujiwara.kanjidate.KanjiDate

class WqueueTable:
  import WqueueTable.Item
  val itemWrapper = div
  val wqueueList = new CompListSortList[Item](itemWrapper)
  val ele = div(
    cls := "practice-cashier-wqueue-table",
    titles
  )
  def add(wqueue: Wqueue, visit: Visit, patient: Patient, charge: Int): Unit =
    wqueueList.add(Item(wqueue, visit, patient, charge))

  def titles: List[HTMLElement] =
    List(
      div("患者"),
      div("診察日"),
      div("請求額"),
      div("操作")
    )

object WqueueTable:
  case class Item(wqueue: Wqueue, visit: Visit, patient: Patient, charge: Int):
    val eles: List[HTMLElement] = 
      List(
        elePatient,
        eleVisitDate,
        eleCharge,
        div(
          button("未収会計")
        )
      )

    def elePatient: HTMLElement =
      div(s"(${patient.patientId}) ${patient.lastName}${patient.firstName}")
    def eleVisitDate: HTMLElement =
      div(KanjiDate.dateToKanji(visit.visitedAt.toLocalDate))
    def eleCharge: HTMLElement =
      div(s"${charge}円")

  given Ordering[Item] = Ordering.by(_.wqueue.visitId)
  given CompList[Item] = _.eles
  given Dispose[Item] = _ => ()