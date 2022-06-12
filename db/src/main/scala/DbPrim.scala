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
        .map(DbShinryouPrim.enterShinryou(_).map { case (event, entered) =>
          (event, entered.shinryouId)
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

  def listDiseaseAdjEx(diseaseId: Int): ConnectionIO[List[(DiseaseAdj, ShuushokugoMaster)]] =
    sql"""
        select a.*, m.* from disease_adj as a inner join shuushokugo_master m
        on a.shuushokugocode = m.shuushokugocode
        where a.disease_id = ${diseaseId}
        order by a.disease_adj_id
    """.query[(DiseaseAdj, ShuushokugoMaster)].to[List]

  def listCurrentDiseaseEx(
      patientId: Int
  ): ConnectionIO[List[(Disease, ByoumeiMaster, List[(DiseaseAdj, ShuushokugoMaster)])]] =
    val diseaseOp = sql"""
        select d.*, m.* from disease as d inner join shoubyoumei_master_arch as m 
        on d.shoubyoumeicode = m.shoubyoumeicode and m.valid_from <= d.start_date
        and (m.valid_upto = '0000-00-00' or d.start_date <= m.valid_upto)
        where d.patient_id = ${patientId} and d.end_date = '0000-00-00'
        order by d.start_date
    """.query[(Disease, ByoumeiMaster)]
    for
      dlist <- diseaseOp.to[List]
      result <- (dlist.map {
        case (d, bm) => 
          listDiseaseAdjEx(d.diseaseId)
            .map(adjList => (d, bm, adjList))
      }).sequence
    yield result
