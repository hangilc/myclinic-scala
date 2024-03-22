package dev.myclinic.scala.db

import cats.effect.IO
import dev.myclinic.scala.model.PatientSummary
import doobie.*
import doobie.implicits.*

trait DbPatientSummary extends Mysql:
  def findPatientSummary(patientId: Int): IO[Option[PatientSummary]] =
    mysql(DbPatientSummaryPrim.getPatientSummary(patientId).option)

  def enterPatientSummary(summary: PatientSummary): IO[Unit] =
    mysql(DbPatientSummaryPrim.enterPatientSummary(summary))

  def updatePatientSummary(summary: PatientSummary): IO[Unit] =
    mysql(DbPatientSummaryPrim.updatePatientSummary(summary))

  def setPatientSummary(summary: PatientSummary): IO[Unit] =
    mysql(DbPatientSummaryPrim.setPatientSummary(summary))