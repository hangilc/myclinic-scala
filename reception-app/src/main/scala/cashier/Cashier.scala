package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{ShowMessage, Icons, Colors, ContextMenu, Table}
import scala.language.implicitConversions
import dev.myclinic.scala.web.appbase.{SideMenu, EventPublishers}
import dev.myclinic.scala.model.{HotlineCreated, Patient}
import dev.myclinic.scala.webclient.Api
import org.scalajs.dom.raw.{HTMLElement, MouseEvent}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure
import scala.concurrent.Future

class Cashier(using publishers: EventPublishers):
  val table = makeTable()
  val ele = div(
    h1("受付患者"),
    table.ele,
    div(
      button("更新", onclick := (onRefreshClick _), mt := "10px")
    )
  )

  private def makeTable(): Table =
    val tab = Table()
    tab.setColumns(List(
      e => e(width := "3rem", textAlign := "center"),
      e => e(width := "5rem", textAlign := "center"),
      e => e(width := "6rem", textAlign := "center"),
      e => e(width := "8rem", textAlign := "center"),
      e => e(width := "3rem", textAlign := "center"),
      e => e(width := "5rem", textAlign := "center"),
      e => e(width := "3rem", textAlign := "center"),
      e => e(width := "3rem", textAlign := "center"),
    ))
    val heads = List("状態", "患者番号", "氏名", "よみ", "性別", "生年月日", "年齢", "操作")
    tab.addHeaderRow(heads.map(h => e => e(h)))
    tab

  private def onRefreshClick(): Unit = 
    for
      list <- Api.listWqueue()
      visitMap <- Api.batchGetVisit(list.map(_.visitId))
      patientMap <- Api.batchGetPatient(visitMap.values.toList.map(_.patientId))
    yield {
      table.clear()
      list.foreach(wq => {
        val visit = visitMap(wq.visitId)
        val patient = patientMap(visit.patientId)
        table.addRow(List(
          e => e(wq.waitState.label),
          e => e(patient.patientId.toString),
          e => e(patient.fullName()),
          e => e(patient.fullNameYomi()),
          e => e(patient.sex.rep),
        ))
      })
    }

