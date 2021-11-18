package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.DoobieMapping.*
import doobie.*
import doobie.implicits.*
import dev.myclinic.scala.util.DateTimeOrdering.{*, given}
import scala.math.Ordered.orderingToOrdered

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object DbConductPrim:
  private val tConduct = Fragment.const("visit_conduct")
  private val cConductId = Fragment.const("id")
  private val cVisitId = Fragment.const("visit_id")

  def listConductForVisit(visitId: Int): ConnectionIO[List[Conduct]] =
    sql"""
      select * from $tConduct where $cVisitId = ${visitId} order by $cConductId
    """.query[Conduct].to[List]

  def getConduct(conductId: Int): Query0[Conduct] =
    sql"""
      select * from $tConduct where $cConductId = $conductId
    """.query[Conduct]

  