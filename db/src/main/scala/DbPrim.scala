package dev.myclinic.scala.db

import dev.myclinic.scala.model.*
import cats._
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.util.log.LogHandler.jdkLogHandler
import dev.myclinic.scala.db.DoobieMapping._

object DbPrim:
  type ShinryouId = Int
  type ConductId = Int

  def batchEnterShinryouConduct(
      req: CreateShinryouConductRequest
  ): ConnectionIO[(List[AppEvent], List[ShinryouId], List[ConductId])] =
    for
      shinryouResult <- req.shinryouList
        .map(DbShinryouPrim.enterShinryou(_).map {
          case (event, entered) => (event, entered.shinryouId)
        })
        .sequence
      (shinryouEvents, shinryouIds) = shinryouResult.unzip
      conductResult <- req.conducts.map(DbConductPrim.createConduct(_)).sequence
      (conductEvents, conductIds) = conductResult.unzip
    yield (
      shinryouEvents ++ conductEvents.flatten,
      shinryouIds,
      conductIds
    )
