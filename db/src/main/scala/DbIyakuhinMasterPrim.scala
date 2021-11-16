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

object DbIyakuhinMasterPrim:
  def getIyakuhinMaster(iyakuhincode: Int, at: LocalDate): Query0[IyakuhinMaster] =
    sql"""
      select * from iyakuhin_master_arch where iyakuhincode = ${iyakuhincode}
        and valid_from <= ${at} and (${at} <= valid_upto or valid_upto = '0000-00-00')
    """.query[IyakuhinMaster]