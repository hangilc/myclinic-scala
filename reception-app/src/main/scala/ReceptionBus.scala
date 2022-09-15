package dev.myclinic.scala.web.reception

import dev.fujiwara.domq.LocalEventPublisher

import dev.fujiwara.domq.LocalEventPublisher
import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.appbase.reception.ReceptionSubscriberChannels

object ReceptionBus:
  val patientCreatedPublisher = new LocalEventPublisher[Patient]
  val patientUpdatedPublisher = new LocalEventPublisher[Patient]
  val patientDeletedPublisher = new LocalEventPublisher[Patient]

  val visitCreatedPublisher = new LocalEventPublisher[Visit]
  val visitUpdatedPublisher = new LocalEventPublisher[Visit]
  val visitDeletedPublisher = new LocalEventPublisher[Visit]

  val wqueueCreatedPublisher = new LocalEventPublisher[Wqueue]
  val wqueueUpdatedPublisher = new LocalEventPublisher[Wqueue]
  val wqueueDeletedPublisher = new LocalEventPublisher[Wqueue]

  def subscriberChannels: ReceptionSubscriberChannels =
    new ReceptionSubscriberChannels:
      def wqueueCreatedChannel = wqueueCreatedPublisher
      def wqueueUpdatedChannel = wqueueUpdatedPublisher
      def wqueueDeletedChannel = wqueueDeletedPublisher
      def patientUpdatedChannel = patientUpdatedPublisher

