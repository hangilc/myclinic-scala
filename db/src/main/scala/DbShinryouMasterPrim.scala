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

object DbShinryouMasterPrim:
  //implicit val han: doobie.util.log.LogHandler = doobie.util.log.LogHandler.jdkLogHandler

  def getShinryouMaster(shinryoucode: Int, at: LocalDate): Query0[ShinryouMaster] =
    sql"""
      select * from shinryoukoui_master_arch where shinryoucode = ${shinryoucode}
        and valid_from <= ${at} and (${at} <= valid_upto or valid_upto = '0000-00-00')
    """.query[ShinryouMaster]