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

object DbConductKizaiPrim:
  def listConductKizaiForConduct(conductId: Int): ConnectionIO[List[ConductKizai]] =
    sql"""
      select * from visit_conduct_kizai where conduct_id = ${conductId} 
        order by conduct_kizai_id
    """.query[ConductKizai].to[List]