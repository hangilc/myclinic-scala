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
  def listConductForVisit(visitId: Int): ConnectionIO[List[Conduct]] =
    sql"""
      select * from visit_conduct where visit_id = ${visitId} order by conduct_id
    """.query[Conduct].to[List]