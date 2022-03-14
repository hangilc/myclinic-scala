package dev.myclinic.scala.appbase

import dev.myclinic.scala.web.appbase.LocalEventPublisher
import dev.myclinic.scala.model.{*, given}
import dev.fujiwara.domq.all.{*, given}

object Selections:
  def patientFormatter: Patient => String = patient =>
      String.format(
        "[%04d] %s",
        patient.patientId,
        patient.fullName()
      )

  def patientSelection(): Selection[Patient] =
    val s = new Selection[Patient]
    s.formatter = patientFormatter
    s

  def patientSelectionWithData[D](): Selection[(Patient, D)] =
    val s = new Selection[(Patient, D)]
    s.formatter = arg => patientFormatter(arg._1)
    s
