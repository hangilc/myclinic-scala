package dev.myclinic.scala.web.reception.records

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.dateinput.{DateInput, DateOptionInput}
import org.scalajs.dom.{HTMLElement, HTMLInputElement}
import scala.concurrent.Future
import org.scalajs.dom.MouseEvent
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import dev.myclinic.scala.model.{Patient, Visit}
import dev.fujiwara.kanjidate.KanjiDate
import scala.util.Success
import scala.util.Failure
import java.time.LocalDate
import cats.data.Validated.Valid
import cats.data.Validated.Invalid
import scala.language.implicitConversions

class SelectByDateBox(cb: Patient => Unit):
  val selection = Selection[Patient](onSelect = cb)
  val dateInput = DateOptionInput()
  dateInput.onChange(listDate _)
  val ele = div(cls := "records-select-by-date-box")(
    div("日付別", cls := "title"),
    dateInput.ele,
    div(
      a("今日", onclick := (() => advance(_ => LocalDate.now()))),
      a("前へ", onclick := (() => advance(_.plusDays(1)))),
      a("次へ", onclick := (() => advance(_.plusDays(-1))))
    ),
    selection.ele
  )

  def init(): Future[Unit] =
    val today = LocalDate.now()
    dateInput.init(Some(today))
    listDate(Some(today))

  def advance(f: LocalDate => LocalDate): Unit =
    dateInput.simulateChange(_.map(f))

  def listDate(atOption: Option[LocalDate]): Future[Unit] =
    atOption match {
      case None => 
        setItems(List.empty)
        Future.successful(())
      case Some(at) =>
        for
          visits <- Api.listVisitByDate(at)
          patientMap <- Api.batchGetPatient(visits.map(_.patientId))
        yield {
          val items = visits.map(visit => (visit, patientMap(visit.patientId)))
          setItems(items)
        }
    }

  def setItems(items: List[(Visit, Patient)]): Unit =
    selection.clear()
    items.foreach {
      case (visit, patient) => 
        val label = String.format("(%04d) %s", patient.patientId, patient.fullName(""))
        selection.add(div(innerText := label), patient)
    }
    
