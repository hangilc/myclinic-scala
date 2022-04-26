package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.LocalEventPublisher
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.model.*

object PracticeBus:
  val addRightWidgetRequest = LocalEventPublisher[HTMLElement]

  private var currentPatientStore: Option[Patient] = None
  private var currentVisitIdStore: Option[Int] = None
  private var currentTempVisitIdStore: Option[Int] = None
  def currentPatient: Option[Patient] = currentPatientStore
  def currentVisitId: Option[Int] = currentVisitIdStore
  def currentTempVisitId: Option[Int] = currentTempVisitIdStore
  val patientChanged = LocalEventPublisher[Option[Patient]]
  val visitIdChanged = LocalEventPublisher[Option[Int]]
  val tempVisitIdChanged = LocalEventPublisher[Option[Int]]
  patientChanged.subscribe(currentPatientStore = _)
  visitIdChanged.subscribe(currentVisitIdStore = _)
  tempVisitIdChanged.subscribe(currentTempVisitIdStore = _)
  def copyTarget: Option[Int] = currentVisitId orElse currentTempVisitId

  val navPageChanged = LocalEventPublisher[Int]
  val navSettingChanged = LocalEventPublisher[(Int, Int)]

  val visitsPerPage: Int = 10

  val textEntered = LocalEventPublisher[Text]
