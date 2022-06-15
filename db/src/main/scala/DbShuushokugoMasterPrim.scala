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
  def getShuushokugoMaster(shuushokugocode: Int, at: LocalDate): Query0[ShuushokugoMaster] =
    sql"""
      select * from shuushokugo_master where shuushokugocode = ${shuushokugocode}
    """.query[ShuushokugoMaster]

  def getShuushokugoMasterByName(name: String, at: LocalDate): Query0[ShuushokugoMaster] =
    sql"""
      select * from shuushokugo_master where name = ${name}
    """.query[ShuushokugoMaster]

  def searchShuushokugoMaster(text: String, at: LocalDate): Query0[ShuushokugoMaster] =
    val like = s"%${text}%"
    sql"""
      select * from shuushokugo_master where name like ${like} order by name
    """.query[ShuushokugoMaster]
