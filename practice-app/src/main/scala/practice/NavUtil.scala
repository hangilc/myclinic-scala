package dev.myclinic.scala.web.practiceapp.practice

import scala.concurrent.Future
import dev.myclinic.scala.webclient.{Api, global}
import dev.fujiwara.domq.SingleTask
import dev.myclinic.scala.web.practiceapp.PracticeBus
import dev.myclinic.scala.model.Patient

object NavUtil:
  private val refreshNavTask = new SingleTask[Int]()

  def refreshNavSetting(): Unit =
    PracticeBus.currentPatient match {
      case None =>
        PracticeBus.navSettingChanged.publish((0, 0))
        PracticeBus.navPageChanged.publish(0)
      case Some(patient) =>
        val curPage: Int = PracticeBus.navPageChanged.currentValue
        val fut: Future[Int] = Api.countVisitByPatient(patient.patientId)
        refreshNavTask.run(
          fut,
          nVisits => {
            val nPages = Nav.calcNumPages(nVisits, PracticeBus.visitsPerPage)
            val newPage = 
              if curPage < nPages then curPage else 0
            PracticeBus.navSettingChanged.publish(curPage, nPages)
            PracticeBus.navPageChanged.publish(newPage)
          }
        )
    }

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
