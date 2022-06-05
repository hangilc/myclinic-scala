package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.LocalEventPublisher
import dev.fujiwara.domq.CachingEventPublisher
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.model.*
import scala.concurrent.Future
import dev.myclinic.scala.webclient.global

object PracticeBus:
  val addRightWidgetRequest = LocalEventPublisher[HTMLElement]

  type VisitId = Int

  private var pvState: PatientVisitState = NoSelection

  val patientVisitChanging = LocalEventPublisher[(PatientVisitState, PatientVisitState)]
  val patientVisitChanged = LocalEventPublisher[PatientVisitState]
  val tempVisitIdChanged = CachingEventPublisher[Option[Int]](None)
  def currentPatientVisitState: PatientVisitState = pvState
  def currentPatient: Option[Patient] = pvState.patientOption
  def currentVisitId: Option[Int] = pvState.visitIdOption
  def currentTempVisitId: Option[Int] = tempVisitIdChanged.currentValue
  def copyTarget: Option[VisitId] = currentVisitId orElse currentTempVisitId

  def setPatientVisitState(newState: PatientVisitState): Future[Unit] =
    for 
      _ <- patientVisitChanging.publish((pvState, newState))
      _ <- tempVisitIdChanged.publish(None)
      _ = pvState = newState
      _ <- patientVisitChanged.publish(pvState)
    yield ()
    
  val navPageChanged = LocalEventPublisher[Int]
  val navSettingChanged = LocalEventPublisher[(Int, Int)]

  val visitsPerPage: Int = 10

  val textEntered = LocalEventPublisher[Text]
  val shinryouEntered = LocalEventPublisher[ShinryouEx]
  val shinryouDeleted = LocalEventPublisher[Shinryou]
  val conductEntered = LocalEventPublisher[ConductEx]
  val conductDeleted = LocalEventPublisher[Conduct]
  val chargeUpdated = LocalEventPublisher[Charge]
  val paymentEntered = LocalEventPublisher[Payment]

  val hokenInfoChanged = LocalEventPublisher[(VisitId, HokenInfo)]
