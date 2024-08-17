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

object DbUsageMasterPrim:
  def selectUsageMasterByUsageName(name: String): ConnectionIO[List[UsageMaster]] =
    val search = s"%${name}%"
    sql"""
      select * from usage_master where usage_name like ${search} order by usage_code
    """.query[UsageMaster].to[List]

  def listUsageMasterKubunName(): ConnectionIO[List[String]] =
    sql"""
      select distinct(kubun_name) from usage_master
    """.query[String].to[List]

  def listUsageMasterDetailKubunName(): ConnectionIO[List[String]] =
    sql"""
      select distinct(detail_kubun_name) from usage_master
    """.query[String].to[List]

  def listUsageMasterTimingName(): ConnectionIO[List[String]] =
    sql"""
      select distinct(timing_name) from usage_master
    """.query[String].to[List]


