package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.DoobieMapping.*
import doobie.*
import doobie.implicits.*
import scala.math.Ordered.orderingToOrdered

object DbGazouLabelPrim:
  def getGazouLabel(conductId: Int): Query0[GazouLabel] =
    sql"""
      select * from visit_gazou_label where visit_conduct_id = ${conductId}
    """.query[GazouLabel]

  def enterGazouLabel(gl: GazouLabel): ConnectionIO[AppEvent] =
    val op = sql"""
      insert into visit_gazou_label set visit_conduct_id = ${gl.conductId}, label = ${gl.label}
    """
    for
      _ <- op.update.run
      event <- DbEventPrim.logGazouLabelCreated(gl)
    yield event

  def deleteGazouLabel(conductId: Int): ConnectionIO[AppEvent] =
    val op = sql"""
      delete from visit_gazou_label where visit_conduct_id = ${conductId}
    """
    for
      gazouLabel <- getGazouLabel(conductId).unique
      affected <- op.update.run
      _ = if affected != 1 then throw new RuntimeException(s"Failed delete gazou label: ${conductId}")
      event <- DbEventPrim.logGazouLabelDeleted(gazouLabel)
    yield event

  def tryDeleteGazouLabel(conductId: Int): ConnectionIO[Option[AppEvent]] =
    for
      gazouLabelOption <- getGazouLabel(conductId).option
      eventOption <- gazouLabelOption match {
        case None => None.pure[ConnectionIO]
        case Some(_) => deleteGazouLabel(conductId).map(Some(_))
      }
    yield eventOption
