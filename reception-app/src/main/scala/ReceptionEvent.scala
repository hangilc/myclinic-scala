package dev.myclinic.scala.web.reception

import dev.myclinic.scala.web.appbase.{EventFetcher, EventPublishers}
import dev.myclinic.scala.model.{AppModelEvent, HotlineBeep}

object ReceptionEvent:
  val publishers = new EventPublishers

  given fetcher: EventFetcher = new EventFetcher
  fetcher.appModelEventPublisher.subscribe(event => publishers.publish(event))
  fetcher.hotlineBeepEventPublisher.subscribe(event => publishers.publish(event))