package dev.myclinic.scala.appbase

import dev.myclinic.scala.model.{*, given}
import dev.fujiwara.domq.all.{*, given}

object Selections:
  def patientFormatter: Patient => String = patient =>
      String.format(
        "[%04d] %s",
        patient.patientId,
        patient.fullName()
      )

  def patientSelection(): Selection[Patient, Patient] =
    val s = new Selection[Patient, Patient](identity)
    s.formatter = patientFormatter
    s

  def patientSelectionWithData[D](): Selection[(Patient, D), Patient] =
    val s = new Selection[(Patient, D), Patient](_._1)
    s.formatter = arg => patientFormatter(arg._1)
    s
