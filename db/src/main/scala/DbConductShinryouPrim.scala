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

object DbConductShinryouPrim:
  private val tConductShinryou = Fragment.const("visit_conduct_shinryou")
  private val cConductId = Fragment.const("visit_conduct_id")
  private val cConductShinryouId = Fragment.const("id")
  def listConductShinryouForConduct(conductId: Int): ConnectionIO[List[ConductShinryou]] =
    sql"""
      select * from $tConductShinryou where $cConductId = ${conductId} 
        order by $cConductShinryouId
    """.query[ConductShinryou].to[List]
    
  def listConductShinryouIdForConduct(conductId: Int): ConnectionIO[List[Int]] =
    sql"""
      select $cConductShinryouId from $tConductShinryou where $cConductId = ${conductId} 
        order by $cConductShinryouId
    """.query[Int].to[List]
