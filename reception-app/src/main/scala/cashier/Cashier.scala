package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.web.appbase.{SideMenu, EventPublishers, PrintDialog}
import dev.myclinic.scala.web.appbase.ElementEvent.*
import dev.myclinic.scala.model.*
import dev.fujiwara.kanjidate.KanjiDate
import dev.fujiwara.kanjidate.DateUtil
import dev.myclinic.scala.webclient.Api
import org.scalajs.dom.{HTMLElement, MouseEvent}
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._
import scala.util.Success
import scala.util.Failure
import scala.concurrent.Future
import java.time.LocalDate
import dev.myclinic.scala.web.appbase.SideMenuService
import scala.collection.mutable
import dev.fujiwara.domq.CompSortList
import dev.fujiwara.domq.ResourceCleanups
import dev.myclinic.scala.web.reception.ReceptionBus

class Cashier extends SideMenuService:
  val unsubs = List(
    ReceptionBus.wqueueCreatedPublisher.subscribe(onWqueueCreated),
    ReceptionBus.wqueueDeletedPublisher.subscribe(onWqueueDeleted)
  )
  val searchTextInput = input
  val table = new Table
  initTable()
  private val rows = new CompSortList[WqueueRow](table.ele)
  val ele: HTMLElement = div(
    cls := "reception-cashier-service",
    div(
      cls := "reception-cashier-head-box",
      div("受付患者", cls := "reception-service-title"),
      form(onsubmit := (doSearch _),
        searchTextInput(cls := "reception-cashier-search-text-input"),
        button(
          "検索",
          attr("type") := "submit",
          cls := "reception-cashier-search-button"
        )
      ),
      button("新規患者", cls := "reception-cashier-new-patient-button", onclick := (doNewPatient _)),
      Icons.menu(
        cls := "reception-cashier-menu-icon",
        onclick := (onMenu _),
        cursor := "pointer"
      )
    ),
    table.ele(cls := "reception-cashier-wqueue-table"),
    div(
      button(
        "更新",
        onclick := (() => {
          refresh().onComplete {
            case Success(_)  => ()
            case Failure(ex) => ShowMessage.showError(ex.getMessage)
          }
        })
      )
    )
  )

  override def getElement: HTMLElement = ele(cls := "content")
  override def init(): Future[Unit] = refresh()
  override def onReactivate: Future[Unit] = refresh()

  override def dispose(): Unit =
    unsubs.foreach(_.proc())

  private def onWqueueCreated(wqueue: Wqueue): Unit =
    for
      visit <- Api.getVisit(wqueue.visitId)
      patient <- Api.getPatient(visit.patientId)
    yield addRow(wqueue, visit, patient)

  private def onWqueueDeleted(wqueue: Wqueue): Unit =
    rows.remove(r => r.wqueue.visitId == wqueue.visitId)

  private def onMenu(event: MouseEvent): Unit =
    val m = ContextMenu(
      List(
        "手書き領収書印刷" -> doPrintBlankReceipt
      )
    )
    m.open(event)

  private def doNewPatient(): Unit =
    val dlog = new NewPatientDialog(newPatient => 
        val d = new PatientSearchResultDialog(List(newPatient))
        d.open()
    )
    dlog.open()
    dlog.initFocus()

  private def doSearch(): Unit =
    val text = searchTextInput.value
    for patients <- Api.searchPatientSmart(text)
    yield 
      if patients.size == 0 then
        ShowMessage.showMessage("該当する患者がありませんでした。")
      else
        val dlog = PatientSearchResultDialog(patients)
        dlog.open()
        searchTextInput.value = ""

  private def doPrintBlankReceipt(): Unit =
    val f =
      for ops <- Api.drawBlankReceipt()
      yield CashierLib.openPrintDialog("手書き領収書", ops)
    f.onComplete {
      case Success(_)  => ()
      case Failure(ex) => ShowMessage.showError(ex.getMessage)
    }

  private def initTable(): Unit =
    table.addColumns(
      List(
        Table.column(cls := "col-state"),
        Table.column(cls := "col-patient-id"),
        Table.column(cls := "col-name"),
        Table.column(cls := "col-yomi"),
        Table.column(cls := "col-sex"),
        Table.column(cls := "col-age"),
        Table.column(cls := "col-birthday"),
        Table.column(cls := "col-manip")
      )
    )
    val heads = List("状態", "患者番号", "氏名", "よみ", "性別", "年齢", "生年月日", "操作")
    val headerCells: List[HTMLElement] =
      heads.map(label => Table.headerCell(innerText := label))
    table.addRow(Table.row(children := headerCells))

  private def addRow(wq: Wqueue, visit: Visit, patient: Patient): Unit =
    val wqRow = new WqueueRow(wq, visit, patient)
    rows.insert(wqRow)

  private def removeRow(visitId: Int): Unit =
    rows.remove(_.wqueue.visitId == visitId)

  private def onCreated(event: AppModelEvent): Unit =
    Api.findWqueueFull(event.dataAs[Wqueue].visitId).onComplete {
      case Success(Some(gen, wq, visit, patient)) => addRow(wq, visit, patient)
      case Success(None)                          => ()
      case Failure(ex) => System.err.println(ex.getMessage)
    }

  def refresh(): Future[Unit] =
    for (gen, list, visitMap, patientMap) <- Api.listWqueueFull()
    yield {
      rows.clear()
      list.foreach(wq => {
        val visit = visitMap(wq.visitId)
        val patient = patientMap(visit.patientId)
        addRow(wq, visit, patient)
      })
    }

  private def doDelete(visit: Visit, patient: Patient): Unit =
    val msg = s"${patient.fullName()}\n削除していいですか？"
    ShowMessage.confirm(msg)(() =>
      Api.deleteVisit(visit.visitId).onComplete {
        case Success(_)  => ()
        case Failure(ex) => ShowMessage.showError("doDelete: " + ex.getMessage)
      }
    )

  private def doCashier(visitId: Int, patient: Patient, at: LocalDate): Unit =
    for
      meisai <- Api.getMeisai(visitId)
      visit <- Api.getVisitEx(visitId)
    yield {
      CashierDialog(meisai, visit).open()
    }
