package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.LocalEventPublisher
import dev.fujiwara.domq.SubscriberChannel

class TempVisitController(patientStateController: PatientStateController):
  private var curVisitId: Option[Int] = None
  private var tempVisitId: Option[Int] = None

  private val publisher = new LocalEventPublisher[Option[Int]]()

  def tempVisitIdChangedSubscriberChannel: SubscriberChannel[Option[Int]] =
    publisher

  def currentTempVisitId: Option[Int] = tempVisitId

  val unsubs = List(
    patientStateController.patientStartingSubscriberChannel.subscribe(s => {
      curVisitId = s.visitId
    }),
    patientStateController.patientClosingSubscriberChannel.subscribe(s => {
      curVisitId = None
      tempVisitId = None
      publisher.publish(None)
    })
  )

  def setTempVisitId(visitId: Int): Boolean =
    if curVisitId.isDefined then false
    else
      tempVisitId = Some(visitId)
      publisher.publish(tempVisitId)
      true

  def clearTempVisitId(): Unit =
    tempVisitId = None
    publisher.publish(tempVisitId)
