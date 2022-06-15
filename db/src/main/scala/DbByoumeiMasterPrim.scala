package dev.myclinic.scala.db

import java.time._
import cats._
import cats.implicits._
import doobie._
import doobie.implicits._
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.DoobieMapping._

object DbByoumeiMasterPrim:
  def getByoumeiMaster(
      shoubyoumeicode: Int,
      at: LocalDate
  ): Query0[ByoumeiMaster] =
    sql"""
      select * from shoubyoumei_master_arch where shoubyoumeicode = ${shoubyoumeicode}
        and valid_from <= ${at} 
        and (valid_upto = '0000-00-00' or valid_upto >= ${at})
    """.query[ByoumeiMaster]

  def getByoumeiMasterByName(
      name: String,
      at: LocalDate
  ): Query0[ByoumeiMaster] =
    sql"""
      select * from shoubyoumei_master_arch where name = ${name}
        and valid_from <= ${at} 
        and (valid_upto = '0000-00-00' or valid_upto >= ${at})
    """.query[ByoumeiMaster]

  def searchByoumeiMaster(text: String, at: LocalDate): Query0[ByoumeiMaster] =
    val like = s"%${text}%"
    sql"""
      select * from shoubyoumei_master_arch where name like ${like}
      and valid_from <= ${at}
      and (valid_upto = '0000-00-00' or valid_upto >= ${at})
      order by name
    """.query[ByoumeiMaster]
