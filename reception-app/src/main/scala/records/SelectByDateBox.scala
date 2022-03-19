package dev.myclinic.scala.web.reception.records

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, ShowMessage, PullDown, PullDownMenu, Selection}
import scala.language.implicitConversions
import dev.fujiwara.dateinput.DateInput
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

class SelectByDateBox(cb: Patient => Unit):
  val selection = Selection[Patient](onSelect = cb)
  val dateInput = DateInput(onEnter = (listDate _), onChange = (listDate _), showYoubi = true)
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
    dateInput.setDate(today)
    listDate(today)

  def advance(f: LocalDate => LocalDate): Unit =
    dateInput.validate() match {
      case Valid(d) => 
        val dd = f(d)
        dateInput.setDate(dd)
        listDate(dd)
      case Invalid(_) => ()
    }

  def listDate(at: LocalDate): Future[Unit] =
    for
      visits <- Api.listVisitByDate(at)
      patientMap <- Api.batchGetPatient(visits.map(_.patientId))
    yield {
      val items = visits.map(visit => (visit, patientMap(visit.patientId)))
      setItems(items)
    }

  def setItems(items: List[(Visit, Patient)]): Unit =
    selection.clear()
    items.foreach {
      case (visit, patient) => 
        val label = String.format("(%04d) %s", patient.patientId, patient.fullName(""))
        selection.add(patient, _ => label, identity)
    }
    
