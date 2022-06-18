package dev.myclinic.scala.db

import cats.*
import cats.effect.IO
import cats.implicits.*
import dev.myclinic.scala.db.DoobieMapping.*
import dev.myclinic.scala.model.*
import doobie.*
import doobie.implicits.*

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import scala.math.Ordered.orderingToOrdered
import scala.runtime.IntRef

object DbDiseasePrim:
  def getDisease(diseaseId: Int): Query0[Disease] =
    sql"""
      select * from disease where disease_id = ${diseaseId}
    """.query[Disease]

  def listCurrentDisease(patientId: Int): Query0[Disease] =
    sql"""
      select * from disease where patient_id = ${patientId} and end_date = '0000-00-00'
      order by start_date
    """.query[Disease]

  def enterDisease(
      patientId: Int,
      shoubyoumeicode: Int,
      startDate: LocalDate,
      endDate: ValidUpto,
      endReason: String
  ): ConnectionIO[(Int, AppEvent)] =
    val op = sql"""
        insert into disease (patient_id, shoubyoumeicode, start_date, end_date, end_reason)
        values (${patientId}, ${shoubyoumeicode}, ${startDate}, ${endDate}, ${endReason})
      """
    for
      diseaseId <- op.update.withUniqueGeneratedKeys[Int]("disease_id")
      disease <- getDisease(diseaseId).unique
      event <- DbEventPrim.logDiseaseCreated(disease)
    yield (diseaseId, event)

  def updateDisease(d: Disease): ConnectionIO[AppEvent] =
    val op = sql"""
        update disease set shoubyoumeicode = ${d.shoubyoumeicode}, 
        start_date = ${d.startDate}, end_date = ${d.endDate}, end_reason = ${d.endReasonStore})
        where disease_id = ${d.diseaseId}
      """
    for
      affected <- op.update.run
      _ = if affected != 1 then throw new RuntimeException(s"Failed to update disease: ${d.diseaseId}")
      updated <- getDisease(d.diseaseId).unique
      event <- DbEventPrim.logDiseaseUpdated(updated)
    yield event


  def endDisease(
      diseaseId: Int,
      endDate: LocalDate,
      endReason: String
  ): ConnectionIO[AppEvent] =
    val op = sql"""
      update disease set end_date = ${endDate}, end_reason = ${endReason} 
      where disease_id = ${diseaseId}
    """
    for
      affected <- op.update.run
      _ = if affected != 1 then throw new RuntimeException(s"Failed to update disease: ${diseaseId}")
      updated <- getDisease(diseaseId).unique
      event <- DbEventPrim.logDiseaseUpdated(updated)
    yield event

  def deleteDisease(diseaseId: Int): ConnectionIO[AppEvent] =
    val op = sql"""
      delete from disease where disease_id = ${diseaseId}
    """
    for
      disease <- getDisease(diseaseId).unique
      affected <- op.update.run
      _ = if affected != 1 then throw new RuntimeException(s"Failed to delete disease: ${diseaseId}.")
      event <- DbEventPrim.logDiseaseDeleted(disease)
    yield event

