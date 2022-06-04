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

  val patientFinished = LocalEventPublisher[Unit]
  val patientVisitChanged = new CachingEventPublisher[PatientVisitState](NoSelection):
    override def publishFuture(newState: PatientVisitState): Future[Unit] =
      currentValue match {
        case Browsing(_) | Practicing(_, _) => 
          for 
            _ <- patientFinished.publishFuture(())
            _ <- super.publishFuture(newState)
          yield ()
        case _ => super.publishFuture(newState)
      }
  val tempVisitIdChanged = CachingEventPublisher[Option[Int]](None)
  def currentPatient: Option[Patient] = patientVisitChanged.currentValue.patientOption
  def currentVisitId: Option[Int] = patientVisitChanged.currentValue.visitIdOption
  def currentTempVisitId: Option[Int] = tempVisitIdChanged.currentValue
  def copyTarget: Option[VisitId] = currentVisitId orElse currentTempVisitId

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
