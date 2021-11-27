package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.{DbKizaiMasterPrim => Prim}
import dev.myclinic.scala.db.DoobieMapping.*
import doobie.*
import doobie.implicits.*
import dev.myclinic.scala.util.DateTimeOrdering.{*, given}
import scala.math.Ordered.orderingToOrdered

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

trait DbKizaiMaster extends Mysql:
  def getKizaiMaster(kizaicode: Int, at: LocalDate): IO[KizaiMaster] =
    mysql(Prim.getKizaiMaster(kizaicode, at).unique)

  def batchResolveKizaiMaster(kizaicodes: List[Int], at: LocalDate): IO[Map[Int, KizaiMaster]] =
    val op = kizaicodes.map(code => Prim.getKizaiMaster(code, at).unique)
      .sequence
      .map(items => Map(items.map(m => (m.kizaicode, m)): _*))
    mysql(op)  