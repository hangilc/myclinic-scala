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

object DbShuushokugoMasterPrim:
  def getShuushokugoMasterByName(name: String): Query0[ShuushokugoMaster] =
    sql"""
      select * from shuushokugo_master where name = ${name}
    """.query[ShuushokugoMaster]
