package dev.myclinic.scala.db

import cats._
import cats.implicits._
import doobie.util.query.Query0
import dev.myclinic.scala.model.Patient
import doobie._
import doobie.implicits._
import doobie.util.log.LogHandler.jdkLogHandler
import dev.myclinic.scala.db.DoobieMapping._
import dev.myclinic.scala.model.PatientSummary

object DbPatientSummaryPrim:
  def getPatientSummary(patientId: Int): Query0[PatientSummary] =
    sql"select * from patient_summary where patient_id = ${patientId}".query[PatientSummary]

  def enterPatientSummary(summary: PatientSummary): ConnectionIO[Unit] =
    val op = sql"""insert into patient_summary (patient_id, content) 
      values (${summary.patientId}, ${summary.content})"""
    op.update.run.map(_ => ())

  def updatePatientSummary(summary: PatientSummary): ConnectionIO[Unit] =
    val op = sql"""
      update patient_summary set content = ${summary.content} where patient_id = ${summary.patientId}
    """
    op.update.run.map(_ => ())

  def setPatientSummary(summary: PatientSummary): ConnectionIO[Unit] =
    for
      s <- getPatientSummary(summary.patientId).option
      _ <- s match
        case Some(_) => updatePatientSummary(summary)
        case None => enterPatientSummary(summary)
    yield ()