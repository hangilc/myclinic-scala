package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.LocalEventPublisher
import dev.fujiwara.domq.CachingEventPublisher
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.model.*
import scala.concurrent.Future
import dev.myclinic.scala.webclient.global
import scala.language.implicitConversions

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
    
  val navPageChanged = CachingEventPublisher[Int](0)
  val navSettingChanged = LocalEventPublisher[(Int, Int)]

  val visitsPerPage: Int = 10

  val textEntered = LocalEventPublisher[Text]
  val shinryouEntered = LocalEventPublisher[ShinryouEx]
  val shinryouDeleted = LocalEventPublisher[Shinryou]
  val conductEntered = LocalEventPublisher[ConductEx]
  val conductDeleted = LocalEventPublisher[Conduct]
  val chargeUpdated = LocalEventPublisher[Charge]
  val paymentEntered = LocalEventPublisher[Payment]
  val visitUpdated = LocalEventPublisher[VisitEx]

  val hokenInfoChanged = LocalEventPublisher[(VisitId, HokenInfo)]

  private var mishuuList: List[(Visit, Patient, Meisai)] = List.empty
  val mishuuListChanged = LocalEventPublisher[List[(Visit, Patient, Meisai)]]
  def addMishuu(visit: Visit, patient: Patient, meisai: Meisai): Future[Unit] = 
    if mishuuList.contains((visit, patient, meisai)) then Future.successful(())
    else
      mishuuList = (mishuuList :+ (visit, patient, meisai)).sortBy((v, _, _) => v.visitId)
      mishuuListChanged.publish(mishuuList)
  def clearMishuuList(): Future[Unit] =
    mishuuList = List.empty
    mishuuListChanged.publish(mishuuList)
  
