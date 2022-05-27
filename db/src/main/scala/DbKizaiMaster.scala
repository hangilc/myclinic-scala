package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.{DbKizaiMasterPrim => Prim}
import dev.myclinic.scala.db.DoobieMapping.*
import doobie.*
import doobie.implicits.*
import scala.math.Ordered.orderingToOrdered

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

trait DbKizaiMaster extends Mysql:
  def getKizaiMaster(kizaicode: Int, at: LocalDate): IO[KizaiMaster] =
    mysql(Prim.getKizaiMaster(kizaicode, at).unique)

  def findKizaiMaster(kizaicode: Int, at: LocalDate): IO[Option[KizaiMaster]] =
    mysql(Prim.getKizaiMaster(kizaicode, at).option)

  def findKizaiMasterByName(name: String, at: LocalDate): IO[Option[KizaiMaster]] =
    mysql(Prim.getKizaiMasterByName(name, at).option)

  def findKizaicodeByName(name: String, at: LocalDate): IO[Option[Int]] =
    mysql(Prim.getKizaiMasterByName(name, at).map(_.kizaicode).option)

  def batchResolveKizaiMaster(kizaicodes: List[Int], at: LocalDate): IO[Map[Int, KizaiMaster]] =
    val op = kizaicodes.map(code => Prim.getKizaiMaster(code, at).unique)
      .sequence
      .map(items => Map(items.map(m => (m.kizaicode, m)): _*))
    mysql(op)  

  def setKizaiMasterValidUpto(validUpto: LocalDate): IO[Int] =
    mysql(sql"""
      update tokuteikizai_master_arch set valid_upto = ${validUpto} 
      where valid_upto = '0000-00-00'
    """.update.run)

  def enterKizaiMaster(m: KizaiMaster): IO[Unit] =
    mysql(sql"""
      insert into tokuteikizai_master_arch (
        kizaicode, name, yomi, unit, kingaku, valid_from, valid_upto
      ) values (
        ${m.kizaicode}, ${m.name}, ${m.yomi}, ${m.unit}, ${m.kingakuStore},
        ${m.validFrom}, ${m.validUpto}
      )
    """.update.run.void)
