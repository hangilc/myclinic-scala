package dev.myclinic.scala.web.reception

import dev.myclinic.scala.web.appbase.{EventFetcher, EventPublishers}
import dev.myclinic.scala.model.{AppModelEvent, HotlineBeep}

object ReceptionEvent:
  val publishers = new EventPublishers

  given fetcher: EventFetcher = new EventFetcher:
    override def publish(event: AppModelEvent): Unit = publishers.publish(event)
    override def publish(event: HotlineBeep): Unit = publishers.publish(event)