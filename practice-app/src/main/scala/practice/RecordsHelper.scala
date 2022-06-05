package dev.myclinic.scala.web.practiceapp.practice

import dev.myclinic.scala.webclient.{Api, global}
import scala.concurrent.Future

object RecordsHelper:
  def refreshRecords(page: Int): Future[Unit] =
    PracticeBus.currentPatient match {
      case Some(patient) =>
        for
          total <- Api.countVisitByPatient(patient.patientId)
          numPages = calcNumPages(total)
          p = page.min(numPages - 1).max(0)
          _ <- PracticeBus.navSettingChanged.publish(p, numPages)
          _ <- PracticeBus.navPageChanged.publish(p)
        yield ()
      case None =>
        for
          _ <- PracticeBus.navSettingChanged.publish(0, 0)
          _ <- PracticeBus.navPageChanged.publish(0)
        yield ()
    }

  def calcNumPages(total: Int): Int =
    val itemsPerPage = PracticeBus.visitsPerPage
    (total + itemsPerPage - 1) / itemsPerPage


