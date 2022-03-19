package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{ShowMessage, Icons, Colors, ContextMenu, Table, DataSource}
import scala.language.implicitConversions
import dev.myclinic.scala.web.appbase.{SideMenu, EventPublishers, PrintDialog}
import dev.myclinic.scala.web.appbase.ElementEvent.*
import dev.myclinic.scala.model.*
import dev.fujiwara.kanjidate.KanjiDate
import dev.myclinic.scala.util.DateUtil
import dev.myclinic.scala.webclient.Api
import org.scalajs.dom.{HTMLElement, MouseEvent}
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._
import scala.util.Success
import scala.util.Failure
import scala.concurrent.Future
import java.time.LocalDate
import dev.myclinic.scala.web.appbase.SideMenuService
import scala.collection.mutable
import dev.myclinic.scala.web.appbase.EventFetcher
import dev.myclinic.scala.web.appbase.{SyncedDataSource, SyncedDataSource3}


class Cashier(using EventFetcher) extends SideMenuService:
  val table = new Table
  initTable()
  val ele: HTMLElement = div(
    div(
      h1("受付患者", display := "inline-block"),
      Icons.menu(
        cssFloat := "right",
        onclick := (onMenu _),
        cursor := "pointer"
      )
    ),
    table.ele,
    div(
      button(
        "更新",
        onclick := (() => {
          refresh().onComplete {
            case Success(_)  => ()
            case Failure(ex) => ShowMessage.showError(ex.getMessage)
          }
        }),
        mt := "10px"
      )
    )
  )
  ele.addCreatedListener[Wqueue](onCreated _)

  //val rowMap: mutable.Map[Int, HTMLElement] = mutable.Map.empty

  override def getElement: HTMLElement = ele(cls := "content")
  override def init(): Future[Unit] = refresh()
  override def onReactivate: Future[Unit] = refresh()

  private def onMenu(event: MouseEvent): Unit =
    val m = ContextMenu(
      List(
        "手書き領収書印刷" -> printBlankReceipt
      )
    )
    m.open(event)

  private def printBlankReceipt(): Unit =
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
        Table.column(width := "3rem", textAlign := "center"),
        Table.column(width := "5rem", textAlign := "center"),
        Table.column(width := "6rem", textAlign := "center"),
        Table.column(width := "8rem", textAlign := "center"),
        Table.column(width := "3rem", textAlign := "center"),
        Table.column(width := "6rem", textAlign := "center"),
        Table.column(width := "3rem", textAlign := "center"),
        Table.column(width := "3rem", textAlign := "center")
      )
    )
    val heads = List("状態", "患者番号", "氏名", "よみ", "性別", "生年月日", "年齢", "操作")
    val headerCells: List[HTMLElement] = 
      heads.map(label => Table.headerCell(innerText := label))
    table.addRow(Table.row(children := headerCells))

  private def addRow(gen: Int, wq: Wqueue, visit: Visit, patient: Patient): Unit =
    val ds: DataSource[(Wqueue, Visit, Patient)] = SyncedDataSource3(gen, wq, visit, patient)
    val wqRow = new WqueueRow(ds)
    table.addRow(wqRow.ele)

  private def removeRow(visitId: Int): Unit =
    ele.qSelectorAll(s".wqueue-row-${visitId}").foreach(_.remove())

  private def onCreated(event: AppModelEvent): Unit =
    Api.findWqueueFull(event.dataAs[Wqueue].visitId).onComplete {
      case Success(Some(gen, wq, visit, patient)) => addRow(gen, wq, visit, patient)
      case Success(None) => ()
      case Failure(ex) => System.err.println(ex.getMessage)
    }

  def refresh(): Future[Unit] =
    for (gen, list, visitMap, patientMap) <- Api.listWqueueFull()
    yield {
      table.clear()
      initTable()
      list.foreach(wq => {
        val visit = visitMap(wq.visitId)
        val patient = patientMap(visit.patientId)
        addRow(gen, wq, visit, patient)
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
