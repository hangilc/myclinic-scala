package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.DoobieMapping.*
import doobie.*
import doobie.implicits.*
import scala.math.Ordered.orderingToOrdered

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object DbTextPrim:
  def listTextForVisit(visitId: Int): ConnectionIO[List[Text]] =
    sql"""
      select * from visit_text where visit_id = $visitId order by text_id
    """.query[Text].to[List]