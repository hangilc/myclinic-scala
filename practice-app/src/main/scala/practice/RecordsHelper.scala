package dev.myclinic.scala.web.practiceapp.practice

import dev.myclinic.scala.webclient.{Api, global}
import scala.concurrent.Future
import dev.fujiwara.domq.SingleTask
import dev.myclinic.scala.web.practiceapp.PracticeBus
import dev.myclinic.scala.model.Patient

object RecordsHelper:
  private val refreshRecordsTask = new SingleTask[Int]()

  def refreshRecords(patientOpt: Option[Patient], page: Int): Unit =
    patientOpt match {
      case Some(patient) =>
        val fut = Api.countVisitByPatient(patient.patientId)
        refreshRecordsTask.run(
          fut,
          total =>
            val numPages = calcNumPages(total)
            val p = page.min(numPages - 1).max(0)
            PracticeBus.navSettingChanged.publish(p, numPages)
            PracticeBus.navPageChanged.publish(p)
        )
      case None =>
        PracticeBus.navSettingChanged.publish(0, 0)
        PracticeBus.navPageChanged.publish(0)
    }

  def calcNumPages(total: Int): Int =
    val itemsPerPage = PracticeBus.visitsPerPage
    (total + itemsPerPage - 1) / itemsPerPage
