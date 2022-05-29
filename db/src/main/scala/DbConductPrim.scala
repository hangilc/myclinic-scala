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
import scala.collection.mutable.ListBuffer

object DbConductPrim:
  private val tConduct = Fragment.const("visit_conduct")
  private val cConductId = Fragment.const("id")
  private val cVisitId = Fragment.const("visit_id")

  def listConductForVisit(visitId: Int): ConnectionIO[List[Conduct]] =
    sql"""
      select * from $tConduct where $cVisitId = ${visitId} order by $cConductId
    """.query[Conduct].to[List]

  def listConductIdForVisit(visitId: Int): ConnectionIO[List[Int]] =
    sql"""
      select * from $tConduct where $cVisitId = ${visitId} order by $cConductId
    """.query[Int].to[List]

  def getConduct(conductId: Int): Query0[Conduct] =
    sql"""
      select * from $tConduct where $cConductId = $conductId
    """.query[Conduct]

  type ConductId = Int

  def enterConduct(c: Conduct): ConnectionIO[(AppEvent, Conduct)] =
    val op = sql"""
      insert into visit_conduct set visit_id = ${c.visitId}, kind = ${c.kindStore}
    """
    for
      id <- op.update.withUniqueGeneratedKeys[Int]("id")
      entered <- getConduct(id).unique
      event <- DbEventPrim.logConductCreated(entered)
    yield (event, entered)

  def createConduct(
      visitId: Int,
      kind: Int,
      labelOption: Option[String],
      shinryouList: List[ConductShinryou],
      drugs: List[ConductDrug],
      kizaiList: List[ConductKizai]
  ): ConnectionIO[(List[AppEvent], ConductId)] =
    val conduct = Conduct(0, visitId, kind)
    var events = new ListBuffer[AppEvent]()
    for
      createConductResult <- DbConductPrim.enterConduct(conduct)
      _ = events += createConductResult.head
      conductId = createConductResult(1).conductId
      createLabelEventOption <- labelOption match {
        case Some(label) =>
          DbGazouLabelPrim
            .enterGazouLabel(GazouLabel(conductId, label))
            .map(Some(_))
        case None => None.pure[ConnectionIO]
      }
      _ = createLabelEventOption.foreach(events += _)
      shinryouEvents <- shinryouList
        .map(cs => DbConductShinryouPrim.enterConductShinryou(cs.copy(conductId = conductId)).map(_.head))
        .sequence
      _ = events ++= shinryouEvents
      drugEvents <- drugs
        .map(cd => DbConductDrugPrim.enterConductDrug(cd.copy(conductId = conductId)).map(_.head))
        .sequence
      _ = events ++= drugEvents
      kizaiEvents <- kizaiList
        .map(ck => DbConductKizaiPrim.enterConductKizai(ck.copy(conductId = conductId)).map(_.head))
        .sequence
      _ = events ++= kizaiEvents
    yield (events.toList, conductId)

  def createConduct(req: CreateConductRequest): ConnectionIO[(List[AppEvent], ConductId)] =
    createConduct(
      req.visitId,
      req.kind,
      req.labelOption,
      req.shinryouList,
      req.drugs,
      req.kizaiList
    )
