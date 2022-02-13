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

object DbKizaiMasterPrim:
  def getKizaiMaster(kizaicode: Int, at: LocalDate): Query0[KizaiMaster] =
    sql"""
      select * from tokuteikizai_master_arch where kizaicode = ${kizaicode}
        and valid_from <= ${at} and (${at} <= valid_upto or valid_upto = '0000-00-00')
    """.query[KizaiMaster]