package dev.myclinic.scala.web.practiceapp

import dev.fujiwara.domq.LocalEventPublisher
import dev.fujiwara.domq.CachingEventPublisher
import dev.fujiwara.domq.SingleTask
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.model.*
import scala.concurrent.Future
import dev.myclinic.scala.webclient.global
import scala.language.implicitConversions
import dev.myclinic.scala.web.practiceapp.practice.RightWidget
import dev.myclinic.scala.web.practiceapp.practice.twilio.TwilioPhone
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.web.practiceapp.practice.PatientStateController
import dev.myclinic.scala.web.practiceapp.practice.PatientStateController.State
import dev.myclinic.scala.web.practiceapp.practice.TempVisitController
import dev.fujiwara.domq.SubscriberChannel
import dev.myclinic.scala.web.appbase.reception.ReceptionSubscriberChannels

object PracticeBus:
  val addRightWidgetRequest = LocalEventPublisher[RightWidget]
  val removeRightWidgetRequest = LocalEventPublisher[RightWidget]

  type VisitId = Int

  val patientStateController = new PatientStateController()
  def patientStartingSubscriberChannel = patientStateController.patientStartingSubscriberChannel
  def patientClosingSubscriberChannel = patientStateController.patientClosingSubscriberChannel
  def currentPatient: Option[Patient] = patientStateController.currentPatient
  def currentVisitId: Option[Int] = patientStateController.currentVisitId
  def currentPatientState: Option[State] = patientStateController.currentState
  val tempVisitController = new TempVisitController(patientStateController)
  def tempVisitIdChangedSubscriberChannel: SubscriberChannel[Option[Int]] =
    tempVisitController.tempVisitIdChangedSubscriberChannel
  def currentTempVisitId: Option[Int] = tempVisitController.currentTempVisitId
  def copyTarget: Option[VisitId] = currentVisitId orElse currentTempVisitId

  val navPageChanged = CachingEventPublisher[Int](0)
  val navSettingChanged = LocalEventPublisher[(Int, Int)]

  val visitsPerPage: Int = 10

  val textEntered = LocalEventPublisher[Text]
  val shinryouEntered = LocalEventPublisher[ShinryouEx]
  val shinryouDeleted = LocalEventPublisher[Shinryou]
  val conductEntered = LocalEventPublisher[ConductEx]
  val conductDeleted = LocalEventPublisher[Conduct]
  val chargeEntered = LocalEventPublisher[Charge]
  val chargeUpdated = LocalEventPublisher[Charge]
  val paymentEntered = LocalEventPublisher[Payment]
  val paymentUpdated = LocalEventPublisher[Payment]
  val paymentDeleted = LocalEventPublisher[Payment]
  val wqueueEntered = LocalEventPublisher[Wqueue]
  val wqueueUpdated = LocalEventPublisher[Wqueue]
  val wqueueDeleted = LocalEventPublisher[Wqueue]
  val visitUpdated = LocalEventPublisher[VisitEx]
  val patientUpdated = LocalEventPublisher[Patient]

  val hokenInfoChanged = LocalEventPublisher[(VisitId, HokenInfo)]

  private var mishuuList: List[(Visit, Patient, Meisai)] = List.empty
  val mishuuListChanged = LocalEventPublisher[List[(Visit, Patient, Meisai)]]
  def addMishuu(visit: Visit, patient: Patient, meisai: Meisai): Unit = 
    if mishuuList.contains((visit, patient, meisai)) then Future.successful(())
    else
      mishuuList = (mishuuList :+ (visit, patient, meisai)).sortBy((v, _, _) => v.visitId)
      mishuuListChanged.publish(mishuuList)
  def clearMishuuList(): Unit =
    mishuuList = List.empty
    mishuuListChanged.publish(mishuuList)
  patientClosingSubscriberChannel.subscribe(s => clearMishuuList())

  val twilioPhone = new TwilioPhone(Api.getWebphoneToken)

  def receptionSubscriberChannels: ReceptionSubscriberChannels =
    new ReceptionSubscriberChannels:
      def wqueueCreatedChannel = wqueueEntered
      def wqueueUpdatedChannel = wqueueUpdated
      def wqueueDeletedChannel = wqueueDeleted
      def patientUpdatedChannel = patientUpdated
    
  
