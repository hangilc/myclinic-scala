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

  def getKizaiMasterByName(name: String, at: LocalDate): Query0[KizaiMaster] =
    sql"""
      select * from tokuteikizai_master_arch where name = ${name}
        and valid_from <= ${at} and (${at} <= valid_upto or valid_upto = '0000-00-00')
    """.query[KizaiMaster]

  def findKizaicodeByName(name: String, at: LocalDate): Query0[Int] =
    getKizaiMasterByName(name, at).map(_.kizaicode)

  def searchKizaiMaster(text: String, at: LocalDate): Query0[KizaiMaster] =
    val like = s"%${text}%"
    sql"""
      select * from tokuteikizai_master_arch
        where name like ${like} 
        and valid_from <= ${at}
        and (valid_upto = '0000-00-00' or ${at} <= valid_upto)
        order by yomi
    """.query[KizaiMaster]


