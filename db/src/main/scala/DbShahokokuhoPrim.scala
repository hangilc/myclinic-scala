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

object DbShahokokuhoPrim:
  def getShahokokuho(shahokokuhoId: Int): Query0[Shahokokuho] =
    sql"""
      select * from hoken_shahokokuho where shahokokuho_id = $shahokokuhoId
    """.query[Shahokokuho]