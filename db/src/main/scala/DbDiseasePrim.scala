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

