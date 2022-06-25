package dev.myclinic.scala.web.reception

import dev.myclinic.scala.web.appbase.{EventFetcher, EventPublishers}
import dev.myclinic.scala.model.*

object ReceptionEvent:
  val publishers = new EventPublishers

  given fetcher: EventFetcher = new EventFetcher
  fetcher.appModelEventPublisher.subscribe(event => publishers.publish(event))
  fetcher.hotlineBeepEventPublisher.subscribe(event => publishers.publish(event))

  fetcher.appModelEventPublisher.subscribe(dispatch _)

  def dispatch(event: AppModelEvent): Unit =
    val C = AppModelEvent.createdSymbol
    val U = AppModelEvent.updatedSymbol
    val D = AppModelEvent.deletedSymbol
    (event.model, event.kind) match
      case (Patient.modelSymbol, C) => ReceptionBus.patientCreatedPublisher.publish(event.dataAs[Patient])
      case (Patient.modelSymbol, U) => ReceptionBus.patientUpdatedPublisher.publish(event.dataAs[Patient])
      case (Patient.modelSymbol, D) => ReceptionBus.patientDeletedPublisher.publish(event.dataAs[Patient])
      case _ => ()
