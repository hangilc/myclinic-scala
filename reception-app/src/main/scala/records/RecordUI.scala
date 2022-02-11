package dev.myclinic.scala.web.reception.records

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, ShowMessage, PullDown, PullDownMenu}
import scala.language.implicitConversions
import org.scalajs.dom.{HTMLElement, HTMLInputElement}
import scala.concurrent.Future
import org.scalajs.dom.MouseEvent
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import dev.myclinic.scala.model.*
import scala.util.Success
import scala.util.Failure
import java.time.LocalDate
import dev.myclinic.scala.util.Misc.countPages

class RecordUI(patient: Patient):
  val patientBlock = PatientBlock(patient)
  val eRecords: HTMLElement = div()
  var totalVisits: Int = 0
  var itemsPerPage = 10
  var page = 0
  val navs: List[RecordNav] =
    List(new RecordNav(gotoPage _), new RecordNav(gotoPage _))
  val ele = div(cls := "record")(
    patientBlock.ele,
    navs(0).ele,
    eRecords,
    navs(1).ele
  )

  def init(): Future[Unit] =
    for
      count <- Api.countVisitByPatient(patient.patientId)
      totalPages = countPages(count, itemsPerPage)
    yield {
      navs.foreach(nav => {
        nav.setTotal(totalPages)
        nav.setPage(0)
      })
      updateUI()
    }

  private def gotoPage(p: Int): Unit =
    page = p
    for _ <- updateUI()
    yield {
      navs.foreach(_.setPage(p))
    }

  def updateUI(): Future[Unit] =
    for
      visitIds <- Api.listVisitIdByPatientReverse(
        patient.patientId,
        page * itemsPerPage,
        itemsPerPage
      )
      visits <- Api.batchGetVisitEx(visitIds)
    yield {
      setVisits(visits)
    }

  def setVisits(visits: List[VisitEx]): Unit =
    eRecords(clear, children := visits.map(VisitBlock(_).ele))
