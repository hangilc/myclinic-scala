package dev.myclinic.scala.db

import cats.*
import cats.syntax.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.{DbIyakuhinMasterPrim => Prim}
import dev.myclinic.scala.db.DoobieMapping.*
import doobie.*
import doobie.implicits.*
import dev.myclinic.scala.util.DateTimeOrdering.{*, given}
import scala.math.Ordered.orderingToOrdered

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

trait DbIyakuhinMaster extends Mysql:
  def getIyakuhinMaster(iyakuhincode: Int, at: LocalDate): IO[IyakuhinMaster] =
    mysql(Prim.getIyakuhinMaster(iyakuhincode, at).unique)

  def batchResolveIyakuhinMaster(iyakuhincodes: List[Int], at: LocalDate): IO[Map[Int, IyakuhinMaster]] =
    val op = iyakuhincodes.map(code => DbIyakuhinMasterPrim.getIyakuhinMaster(code, at).unique)
      .sequence
      .map(items => Map(items.map(m => (m.iyakuhincode, m)): _*))
    mysql(op)