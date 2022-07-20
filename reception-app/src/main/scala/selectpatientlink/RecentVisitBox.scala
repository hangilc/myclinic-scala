package dev.myclinic.scala.web.reception.selectpatientlink

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.all.{*, given}
import org.scalajs.dom.{HTMLElement, HTMLInputElement}
import scala.concurrent.Future
import org.scalajs.dom.MouseEvent
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import dev.myclinic.scala.model.{Patient, Visit}
import dev.fujiwara.kanjidate.KanjiDate
import scala.util.Success
import scala.util.Failure
import scala.language.implicitConversions

class RecentVisitBox(cb: Patient => Unit):
  val selection = Selection(onSelect = cb)
  val pageDisp: HTMLElement = span()
  val ele: HTMLElement = div(cls := "records-recent-visit-box")(
    div("最近の診察", cls := "title"),
    div(
      a("最初", onclick := (onFirst _)),
      a("前へ", onclick := (onPrev _)),
      a("次へ", onclick := (onNext _)),
      pageDisp
    ),
    selection.ele
  )
  val itemsPerPage = 10
  var page = 0

  def init(): Future[Boolean] =
    for 
      ok <- fetch(0, itemsPerPage)
    yield {
      if ok then
        updatePageDisp()
      ok
    }

  def updatePageDisp(): Unit =
    pageDisp.innerText = s"[${page + 1}]"

  def onFirst(): Unit =
    for
      ok <- fetch(0, itemsPerPage)
    yield {
      if ok then
        page = 0
        updatePageDisp()
    }

  def onPrev(): Unit =
    if page > 0 then
      for
        ok <- fetch((page - 1) * itemsPerPage, itemsPerPage)
      yield {
        if ok then
          page = page - 1
          updatePageDisp()
      }

  def onNext(): Unit =
    for
      ok <- fetch((page + 1) * itemsPerPage, itemsPerPage)
    yield {
      if ok then
        page = page + 1
        updatePageDisp()
    }

  def fetch(offset: Int, count: Int): Future[Boolean] =
    for
      visits <- Api.listRecentVisit(offset, count)
      patientMap <- Api.batchGetPatient(visits.map(_.patientId))
    yield {
      if visits.size > 0 then
        setItems(visits.map(visit => (visit, patientMap(visit.patientId))))
        true
      else
        false
    }

  def setItems(items: List[(Visit, Patient)]): Unit =
    selection.clear()
    items.foreach {
      case (visit, patient) => 
        val patientLabel = String.format("(%04d) %s", patient.patientId, patient.fullName(""))
        val atLabel = KanjiDate.dateToKanji(visit.visitedAt.toLocalDate(),
        formatYoubi = info => s"（${info.youbi}）")
        val label = s"${patientLabel} -- ${atLabel}"
        selection.add(div(innerText := label), patient)
    }
    

