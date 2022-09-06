package dev.myclinic.scala.web.practiceapp.practice

import scala.concurrent.Future
import dev.myclinic.scala.webclient.{Api, global}
import dev.fujiwara.domq.SingleTask
import dev.myclinic.scala.web.practiceapp.PracticeBus
import dev.myclinic.scala.model.Patient

object NavUtil:
  private val refreshNavTask = new SingleTask[Int]()

  def refreshNavSetting(): Unit =
    PracticeBus.patientStartingSubscriberChannel.subscribe(s => {
      val patient: Patient = s.patient
      val fut: Future[Int] = Api.countVisitByPatient(patient.patientId)
      refreshNavTask.run(
        fut,
        nVisits => {
          val nPages = Nav.calcNumPages(nVisits, PracticeBus.visitsPerPage)
          val curPage = PracticeBus.navPageChanged.currentValue
          PracticeBus.navSettingChanged.publish(curPage, nPages)
          PracticeBus.navPageChanged.publish(curPage)
        }
      )
    })
    PracticeBus.patientClosingSubscriberChannel.subscribe(s => {
      PracticeBus.navSettingChanged.publish(0, 0)
      PracticeBus.navPageChanged.publish(0)
    })

    // PracticeBus.currentPatient match {
    //   case Some(patient) =>
    //     val fut: Future[Int] = Api.countVisitByPatient(patient.patientId)
    //     refreshNavTask.run(
    //       fut,
    //       nVisits => {
    //         val nPages = Nav.calcNumPages(nVisits, PracticeBus.visitsPerPage)
    //         val curPage = PracticeBus.navPageChanged.currentValue
    //         PracticeBus.navSettingChanged.publish(curPage, nPages)
    //         PracticeBus.navPageChanged.publish(curPage)
    //       }
    //     )
    //   case None =>
    //     PracticeBus.navSettingChanged.publish(0, 0)
    //     PracticeBus.navPageChanged.publish(0)
    // }
