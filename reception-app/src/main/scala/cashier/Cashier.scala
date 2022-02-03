package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{ShowMessage, Icons, Colors, ContextMenu, Table}
import scala.language.implicitConversions
import dev.myclinic.scala.web.appbase.{SideMenu, EventPublishers, PrintDialog}
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
import dev.myclinic.scala.web.appbase.EventSubscriber
import dev.myclinic.scala.web.reception.ReceptionEventFetcher
import scala.collection.mutable
import dev.myclinic.scala.web.appbase.ElementDispatcher.*
import dev.myclinic.scala.web.appbase.EventFetcher

class Cashier(using publishers: EventPublishers, fetcher: EventFetcher) extends SideMenuService:
  val table = makeTable()
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
  val rowMap: mutable.Map[Int, HTMLElement] = mutable.Map.empty

  override def getElement: HTMLElement = ele
  override def init(): Future[Unit] = refresh()
  override def onReactivate: Future[Unit] = refresh()

  def registerEventListeners(): Unit =
    ele.addDeletedListener(publishers.wqueue, (gen, event) => {})

  def unregisterEventListeners(): Unit =
    ele.removeDeletedListener(publishers.wqueue)

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

  private def makeTable(): Table =
    val tab = Table()
    tab.setColumns(
      List(
        e => e(width := "3rem", textAlign := "center"),
        e => e(width := "5rem", textAlign := "center"),
        e => e(width := "6rem", textAlign := "center"),
        e => e(width := "8rem", textAlign := "center"),
        e => e(width := "3rem", textAlign := "center"),
        e => e(width := "6rem", textAlign := "center"),
        e => e(width := "3rem", textAlign := "center"),
        e => e(width := "3rem", textAlign := "center")
      )
    )
    val heads = List("状態", "患者番号", "氏名", "よみ", "性別", "生年月日", "年齢", "操作")
    tab.addHeaderRow(heads.map(h => e => e(h)))
    tab

  private def addRow(wq: Wqueue, visit: Visit, patient: Patient): Unit =
    val birthday = KanjiDate.dateToKanji(
      patient.birthday,
      formatYear = i => s"${i.gengouAlphaChar}${i.nen}",
      formatMonth = i => s".${i.month}",
      formatDay = i => s".${i.day}",
      formatYoubi = _ => ""
    )
    val age = DateUtil.calcAge(patient.birthday, LocalDate.now())
    val row = table.addRow(
      List(
        e => e(wq.waitState.label),
        e => e(patient.patientId.toString),
        e => e(patient.fullName()),
        e => e(patient.fullNameYomi()),
        e => e(patient.sex.rep),
        e => e(birthday),
        e => e(s"${age}才"),
        e => {
          if wq.waitState == WaitState.WaitCashier then
            e(
              button("会計")(
                onclick := (() =>
                  doCashier(wq.visitId, patient, visit.visitedAt.toLocalDate)
                )
              )
            )
          if wq.waitState == WaitState.WaitExam then
            e(a("削除", onclick := (() => doDelete(visit, patient))))
        }
      )
    )
    rowMap.addOne(wq.visitId, row)

  private def removeRow(visitId: Int): Unit =
    rowMap.get(visitId).foreach(row => row.remove())

  def refresh(): Future[Unit] =
    unregisterEventListeners()
    for (gen, list, visitMap, patientMap) <- Api.listWqueueFull()
    yield {
      table.clear()
      list.foreach(wq => {
        val visit = visitMap(wq.visitId)
        val patient = patientMap(visit.patientId)
        addRow(wq, visit, patient)
      })
      fetcher.catchup(gen, (_, event) => event match {
        case WqueueCreated(_, created) => 
          for
            ()
        
      })
      registerEventListeners()
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
