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
import dev.myclinic.scala.model.Patient
import scala.util.Success
import scala.util.Failure

class Records() extends SideMenuService:
  val selectPatientButton = PullDown.createButtonAnchor("患者選択")
  def getElement: HTMLElement =
    div(cls := "records")(
      div(cls := "header")(
        h1("診療記録"),
        selectPatientButton(onclick := (onSelectPatient _))
      )
    )

  def onSelectPatient(event: MouseEvent): Unit =
    val m = new PullDownMenu()
    val c = PullDown.createContent(
      () => m.close(),
      List(
        "受付患者" -> (onSelectFromWqueue _),
        "患者検索" -> (onSelectFromSearch _),
        "最近の診察" -> (onSelectFromRecent _),
        "日付別" -> (() =>())
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

  def start(patient: Patient): Unit =
    println(("start", patient))
