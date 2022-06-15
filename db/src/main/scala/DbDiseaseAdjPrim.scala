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

object DbDiseaseAdjPrim:
  def listDiseaseAdj(diseaseId: Int): Query0[DiseaseAdj] =
    sql"""
      select * from disease_adj where disease_id = ${diseaseId} order by disease_adj_id
    """.query[DiseaseAdj]
  
  def getDiseaseAdj(diseaseAdjId: Int): Query0[DiseaseAdj] =
    sql"""
      select * from disease_adj where disease_adj_id = ${diseaseAdjId}
    """.query[DiseaseAdj]

  def enterDiseaseAdj(diseaseId: Int, shuushokugocode: Int): ConnectionIO[(Int, AppEvent)] =
    val op = sql"""
      insert into disease_adj (disease_id, shuushokugocode) 
      values (${diseaseId}, ${shuushokugocode})
    """
    for
      adjId <- op.update.withUniqueGeneratedKeys[Int]("disease_adj_id")
      adj <- getDiseaseAdj(adjId).unique
      event <- DbEventPrim.logDiseaseAdjCreated(adj)
    yield (adjId, event)

