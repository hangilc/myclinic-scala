package dev.myclinic.scala.web.reception.records

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, ShowMessage, PullDown, PullDownMenu, Selection}
import scala.language.implicitConversions
import dev.fujiwara.dateinput.DateInput
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}
import scala.concurrent.Future
import dev.myclinic.scala.web.appbase.EventSubscriber
import org.scalajs.dom.raw.MouseEvent
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import dev.myclinic.scala.model.{Patient, Visit}
import dev.fujiwara.kanjidate.KanjiDate
import scala.util.Success
import scala.util.Failure
import java.time.LocalDate

class SelectByDateBox(cb: Patient => Unit):
  val selection = Selection(cb)
  val dateInput = DateInput(onEnter = (listDate _), onChange = (listDate _))
  val ele = div(cls := "records-select-by-date-box")(
    div("日付別", cls := "title"),
    dateInput.ele,
    selection.ele
  )

  def init(): Future[Unit] =
    listDate(LocalDate.now())

  def listDate(at: LocalDate): Future[Unit] =
    println(("list-date", at))
    for
      visits <- Api.listVisitByDate(at)
      patientMap <- Api.batchGetPatient(visits.map(_.patientId))
    yield {
      val items = visits.map(visit => (visit, patientMap(visit.patientId)))
      println(("items", items))
      setItems(items)
    }

  def setItems(items: List[(Visit, Patient)]): Unit =
    selection.clear()
    items.foreach {
      case (visit, patient) => 
        val label = String.format("(%04d) %s", patient.patientId, patient.fullName(""))
        selection.add(label, patient)
    }
    
