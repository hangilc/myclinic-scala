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

import dev.myclinic.scala.model.Patient
import scala.util.Success
import scala.util.Failure
import java.time.LocalDate

class Records() extends SideMenuService:
  val selectPatientButton = PullDown.createButtonAnchor("患者選択")
  val eRecord = div()
  override def getElement: HTMLElement =
    div(cls := "records", cls := "content")(
      div(cls := "header")(
        h1("診療記録"),
        selectPatientButton(onclick := (onSelectPatient _))
      ),
      eRecord
    )

  def onSelectPatient(event: MouseEvent): Unit =
    val m = new PullDownMenu()
    val c = PullDown.createContent(
      () => m.close(),
      List(
        "受付患者" -> (onSelectFromWqueue _),
        "患者検索" -> (onSelectFromSearch _),
        "最近の診察" -> (onSelectFromRecent _),
        "日付別" -> (onSelectByDate _)
      )
    )
    m.open(c, f => PullDown.locatePullDownMenu(selectPatientButton, f))

  def onSelectFromWqueue(): Unit =
    for
      wqueue <- Api.listWqueue()
      visitMap <- Api.batchGetVisit(wqueue.map(_.visitId))
      patientMap <- Api.batchGetPatient(visitMap.values.toList.map(_.patientId))
    yield {
      val patients =
        wqueue.map(wq => visitMap(wq.visitId).patientId).map(patientMap(_))
      val m = new PullDownMenu()
      val c = PullDown.createContent(() => m.close(), patients.map(
        patient =>
          patient.fullName("") -> (() => start(patient))
      ))
      m.open(c, f => PullDown.locatePullDownMenu(selectPatientButton, f))
    }

  def onSelectFromSearch(): Unit =
    val m = new PullDownMenu()
    val box = new SearchPatientBox(patient => {
      m.close()
      start(patient)
    })
    box.ele(cls := "domq-context-menu")
    m.open(box.ele, f => PullDown.locatePullDownMenu(selectPatientButton, f))
    
  def onSelectFromRecent(): Unit =
    val m = new PullDownMenu()
    val box = new RecentVisitBox(patient => {
      m.close()
      start(patient)
    })
    box.ele(cls := "domq-context-menu")
    (for
      _ <- box.init()
    yield {
      m.open(box.ele, f => PullDown.locatePullDownMenu(selectPatientButton, f))
    }).onComplete {
      case Success(_) => ()
      case Failure(ex) => System.err.println(ex.getMessage)
    }

  def onSelectByDate(): Unit =
    val m = new PullDownMenu()
    val box = new SelectByDateBox(patient => {
      m.close()
      start(patient)
    })
    box.ele(cls := "domq-context-menu")
    for
      _ <- box.init()
    yield {
      m.open(box.ele, f => PullDown.locatePullDownMenu(selectPatientButton, f))
    }

  def start(patient: Patient): Unit =
    ()
    // val r = RecordUI(patient)
    // for
    //   _ <- r.init()
    // yield eRecord(clear, children := List(r.ele))
