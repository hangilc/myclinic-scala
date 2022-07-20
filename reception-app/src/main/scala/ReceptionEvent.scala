package dev.myclinic.scala.web.reception

import dev.myclinic.scala.web.appbase.{EventFetcher, EventPublishers}
import dev.myclinic.scala.model.*

object ReceptionEvent:
  val publishers = new EventPublishers:
    override def onWqueueCreated(wqueue: Wqueue): Unit =
      ReceptionBus.wqueueCreatedPublisher.publish(wqueue)
    override def onWqueueUpdated(wqueue: Wqueue): Unit =
      ReceptionBus.wqueueUpdatedPublisher.publish(wqueue)
    override def onWqueueDeleted(wqueue: Wqueue): Unit =
      ReceptionBus.wqueueDeletedPublisher.publish(wqueue)
    override def onPatientCreated(patient: Patient): Unit =
      ReceptionBus.patientCreatedPublisher.publish(patient)
    override def onPatientUpdated(patient: Patient): Unit =
      ReceptionBus.patientUpdatedPublisher.publish(patient)
    override def onPatientDeleted(patient: Patient): Unit =
      ReceptionBus.patientDeletedPublisher.publish(patient)

  given fetcher: EventFetcher = new EventFetcher
  fetcher.appModelEventPublisher.subscribe(event => publishers.publish(event))
  fetcher.hotlineBeepEventPublisher.subscribe(event =>
    publishers.publish(event)
  )
