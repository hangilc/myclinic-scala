package dev.myclinic.scala.web.reception.selectpatientlink

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.{HTMLElement, HTMLInputElement}
import scala.concurrent.Future
import org.scalajs.dom.MouseEvent
import dev.myclinic.scala.webclient.{Api, global}

import dev.myclinic.scala.model.Patient
import scala.util.Success
import scala.util.Failure
import java.time.LocalDate
import dev.fujiwara.domq.PullDownMenu

class SelectPatientPullDown:
  val onSelectPublisher = new LocalEventPublisher[Patient]
  val selectPatientLink = PullDown.createLinkAnchor("診療録")(onclick := (onSelectPatient _))
  val ele = selectPatientLink

  def onSelectPatient(event: MouseEvent): Unit =
    val m = new PullDownMenu()
    val c = m.createContent(
      List(
        "受付患者" -> (onSelectFromWqueue _),
        "患者検索" -> (onSelectFromSearch _),
        "最近の診察" -> (onSelectFromRecent _),
        "日付別" -> (onSelectByDate _)
      )
    )(cls := "record-menu-items")
    m.open(c, f => PullDown.locate(selectPatientLink, f))

  def onSelectFromWqueue(): Unit =
    for
      wqueue <- Api.listWqueue()
      visitMap <- Api.batchGetVisit(wqueue.map(_.visitId))
      patientMap <- Api.batchGetPatient(visitMap.values.toList.map(_.patientId))
    yield {
      val patients =
        wqueue.map(wq => visitMap(wq.visitId).patientId).map(patientMap(_))
      val m = new PullDownMenu()
      val c = m.createContent(patients.map(
        patient =>
          patient.fullName("") -> (() => onSelectPublisher.publish(patient))
      ))(cls := "wqueue-patient-list")
      m.open(c, f => PullDown.locate(selectPatientLink, f))
    }

  def onSelectFromSearch(): Unit =
    val m = new PullDownMenu()
    val box = new SearchPatientBox(patient => {
      m.close()
      onSelectPublisher.publish(patient)
    })
    box.ele(cls := "domq-context-menu")
    m.open(box.ele, f => PullDown.locate(selectPatientLink, f))
    
  def onSelectFromRecent(): Unit =
    val m = new PullDownMenu()
    val box = new RecentVisitBox(patient => {
      m.close()
      onSelectPublisher.publish(patient)
    })
    box.ele(cls := "domq-context-menu")
    (for
      _ <- box.init()
    yield {
      m.open(box.ele, f => PullDown.locate(selectPatientLink, f))
    }).onComplete {
      case Success(_) => ()
      case Failure(ex) => System.err.println(ex.getMessage)
    }

  def onSelectByDate(): Unit =
    val m = new PullDownMenu()
    val box = new SelectByDateBox(patient => {
      m.close()
      onSelectPublisher.publish(patient)
    })
    box.ele(cls := "domq-context-menu")
    for
      _ <- box.init()
    yield {
      m.open(box.ele, f => PullDown.locate(selectPatientLink, f))
    }

