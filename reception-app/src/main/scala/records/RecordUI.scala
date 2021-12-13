package dev.myclinic.scala.web.reception.records

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, ShowMessage, PullDown, PullDownMenu}
import scala.language.implicitConversions
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}
import scala.concurrent.Future
import dev.myclinic.scala.web.appbase.EventSubscriber
import org.scalajs.dom.raw.MouseEvent
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import dev.myclinic.scala.model.*
import scala.util.Success
import scala.util.Failure
import java.time.LocalDate

class RecordUI(patient: Patient):
  val patientBlock = PatientBlock(patient)
  val eRecords: HTMLElement = div()
  var totalVisits: Int = 0
  var itemsPerPage = 10
  var page = 0
  val ele = div(cls := "record")(
    patientBlock.ele,
    eRecords
  )

  def init(): Future[Unit] = 
    for
      count <- Api.countVisitByPatient(patient.patientId)
    yield {
      updateUI()
    }

  def updateUI(): Future[Unit] =
    for
      visitIds <- Api.listVisitIdByPatient(patient.patientId, page * itemsPerPage, itemsPerPage)
      visits <- Api.batchGetVisitEx(visitIds)
    yield {
      println(("records", visits))
    }


  def setVisits(visits: List[Visit]): Unit =
    ???

