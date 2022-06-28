package dev.myclinic.scala.web.reception

import dev.fujiwara.domq.LocalEventPublisher

import dev.fujiwara.domq.LocalEventPublisher
import dev.myclinic.scala.model.*

object ReceptionBus:
  val patientCreatedPublisher = new LocalEventPublisher[Patient]
  val patientUpdatedPublisher = new LocalEventPublisher[Patient]
  val patientDeletedPublisher = new LocalEventPublisher[Patient]

  val visitCreatedPublisher = new LocalEventPublisher[Visit]
  val visitUpdatedPublisher = new LocalEventPublisher[Visit]
  val visitDeletedPublisher = new LocalEventPublisher[Visit]

