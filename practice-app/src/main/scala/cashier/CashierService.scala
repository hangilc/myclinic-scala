package dev.myclinic.scala.web.practiceapp.cashier

import dev.myclinic.scala.web.appbase.SideMenuService
import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import scala.concurrent.Future
import dev.myclinic.scala.model.*
import dev.myclinic.scala.webclient.{Api, global}
import cats.syntax.all.*

class CashierService extends SideMenuService:
  val workarea = div(cls := "practice-cashier-workarea")
  val wqTable = div(cls := "practice-cashier-wqueue-table")
  override def getElements: List[HTMLElement] =
    List(div(
      cls := "practice-cashier practice-sidemenu-service-main",
      div("会計", cls := "practice-sidemenu-service-title"),
      workarea(wqTable)
    ))

  override def init(): Future[Unit] = refresh()
  override def onReactivate: Future[Unit] = refresh()

  def refresh(): Future[Unit] = 
    for
      cashiers <- CashierService.listWqueue
    yield print(cashiers)

object CashierService:
  def listWqueue: Future[List[(Wqueue, Charge, Visit, Patient)]] =
    for
      wqFullList <- Api.listWqueueFull()
      (_, wqList, visitMap, patientMap) = wqFullList
      cashierList = wqList.filter(_.waitState == WaitState.WaitCashier)
      charges <- cashierList.map(c => Api.getCharge(c.visitId)).sequence
    yield cashierList.zip(charges).map(
      (wq, charge) => 
        val visit = visitMap(wq.visitId)
        (wq, charge, visit, patientMap(visit.patientId))
    )
