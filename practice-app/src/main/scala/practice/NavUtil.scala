package dev.myclinic.scala.web.practiceapp.practice

import scala.concurrent.Future
import dev.myclinic.scala.webclient.{Api, global}

object NavUtil:
  def refreshNavSetting(): Future[Unit] =
    PracticeBus.currentPatient match {
      case Some(patient) =>
        for
          nVisits <- Api.countVisitByPatient(patient.patientId)
          nPages = Nav.calcNumPages(nVisits, PracticeBus.visitsPerPage)
          curPage = PracticeBus.navPageChanged.currentValue
          _ <- PracticeBus.navSettingChanged.publish(curPage, nPages)
          _ <- PracticeBus.navPageChanged.publish(curPage)
        yield ()
      case None => 
        for
          _ <- PracticeBus.navSettingChanged.publish(0, 0)
          _ <- PracticeBus.navPageChanged.publish(0)
        yield ()
    }


