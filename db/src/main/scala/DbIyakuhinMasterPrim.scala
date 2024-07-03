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

object DbIyakuhinMasterPrim:
  def getIyakuhinMaster(iyakuhincode: Int, at: LocalDate): Query0[IyakuhinMaster] =
    sql"""
      select * from iyakuhin_master_arch where iyakuhincode = ${iyakuhincode}
        and valid_from <= ${at} and (${at} <= valid_upto or valid_upto = '0000-00-00')
    """.query[IyakuhinMaster]

  def getIyakuhinMasterByName(name: String, at: LocalDate): Query0[IyakuhinMaster] =
    sql"""
      select * from iyakuhin_master_arch where name = ${name}
        and valid_from <= ${at} and (${at} <= valid_upto or valid_upto = '0000-00-00')
    """.query[IyakuhinMaster]

  def searchIyakuhinMaster(text: String, at: LocalDate): Query0[IyakuhinMaster] =
    val like = s"%${text}%"
    sql"""
      select * from iyakuhin_master_arch
        where name like ${like} 
        and valid_from <= ${at}
        and (valid_upto = '0000-00-00' or ${at} <= valid_upto)
        order by yomi
    """.query[IyakuhinMaster]



