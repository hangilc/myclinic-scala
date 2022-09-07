package dev.myclinic.scala.web.practiceapp.practice

import dev.myclinic.scala.webclient.{Api, global}
import scala.concurrent.Future
import dev.myclinic.scala.model.WaitState
import dev.myclinic.scala.util.NumberUtil.format
import dev.fujiwara.domq.LocalEventPublisher
import dev.fujiwara.domq.SubscriberChannel
import dev.myclinic.scala.model.Patient

class PatientStateController:
  import PatientStateController.State

  private var cur: Option[State] = None

  private val patientStartingPublisher = new LocalEventPublisher[State]
  private val patientClosingPublisher = new LocalEventPublisher[State]

  def currentPatient: Option[Patient] = cur.map(_.patient)
  def currentVisitId: Option[Int] = cur.fold(None)(_.visitId)
  def currentState: Option[State] = cur

  def patientStartingSubscriberChannel: SubscriberChannel[State] =
    patientStartingPublisher

  def patientClosingSubscriberChannel: SubscriberChannel[State] =
    patientClosingPublisher

  def startPatient(
      patient: Patient,
      visitId: Option[Int],
      modifier: State => State = identity
  ): Unit =
    cur = cur.map(modifier(_))
    for
      _ <- endPatient()
      _ <- {
        val newState = State(patient, visitId)
        cur = Some(newState)
        patientStartingPublisher.publish(newState)
        newState.visitId.fold(Future.successful(()))(visitId =>
          Api.changeWqueueState(visitId, WaitState.InExam)
        )
      }
    yield ()

  def endPatient(): Future[Unit] =
    closeCurrent(WaitState.WaitReExam)

  def endPatient(state: WaitState): Future[Unit] =
    closeCurrent(state)

  private def closeCurrent(state: WaitState): Future[Unit] =
    val curSave = cur
    cur = None
    val op = curSave match {
      case None => Future.successful(())
      case Some(s @ State(patientId, None)) =>
        patientClosingPublisher.publish(s)
        Future.successful(())
      case Some(s @ State(patientId, Some(visitId))) =>
        patientClosingPublisher.publish(s)
        Api.changeWqueueState(visitId, state).map(_ => ())
    }
    for _ <- op
    yield ()

object PatientStateController:
  case class State(patient: Patient, visitId: Option[Int])
