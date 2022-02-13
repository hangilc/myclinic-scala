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

object DbShinryouPrim:
  def listShinryouForVisit(visitId: Int): ConnectionIO[List[Shinryou]] =
    sql"""
      select * from visit_shinryou where visit_id = ${visitId} order by shinryou_id
    """.query[Shinryou].to[List]
    
  def listShinryouIdForVisit(visitId: Int): ConnectionIO[List[Int]] =
    sql"""
      select shinryou_id from visit_shinryou where visit_id = ${visitId} order by shinryou_id
    """.query[Int].to[List]
    