package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.LocalEventPublisher
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.model.*

object PracticeBus:
  val addRightWidgetRequest = LocalEventPublisher[HTMLElement]
  val startPatientRequest = LocalEventPublisher[Patient]
  val startVisitRequest = LocalEventPublisher[(Patient, Visit)]
  val patientChanged = LocalEventPublisher[Option[Patient]]

  private var currentPatientStore: Option[Patient] = None
  def currentPatient: Option[Patient] = currentPatientStore
  patientChanged.subscribe(currentPatientStore = _)

  val navPageChanged = LocalEventPublisher[Int]
  val navSettingChanged = LocalEventPublisher[(Int, Int)]

  val visitsPerPage: Int = 10

