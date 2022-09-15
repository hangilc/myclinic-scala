package dev.myclinic.scala.web.appbase.reception

import dev.fujiwara.domq.SubscriberChannel
import dev.myclinic.scala.model.Wqueue
import dev.myclinic.scala.model.Patient

trait ReceptionSubscriberChannels:
  def wqueueCreatedChannel: SubscriberChannel[Wqueue]
  def wqueueUpdatedChannel: SubscriberChannel[Wqueue]
  def wqueueDeletedChannel: SubscriberChannel[Wqueue]
  def patientUpdatedChannel: SubscriberChannel[Patient]
