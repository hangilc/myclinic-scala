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

object DbTextPrim:
  def listTextForVisit(visitId: Int): ConnectionIO[List[Text]] =
    sql"""
      select * from visit_text where visit_id = $visitId order by text_id
    """.query[Text].to[List]

  def getText(textId: Int): Query0[Text] =
    sql"""
      select * from visit_text where text_id = ${textId}
    """.query[Text]

  def enterText(text: Text): ConnectionIO[(Text, AppEvent)] =
    val q = sql"""
      insert into visit_text (visit_id, content) values (${text.visitId}, ${text.content}) 
    """
    for
      id <- q.update.withUniqueGeneratedKeys[Int]("text_id")
      entered <- getText(id).unique
      event <- DbEventPrim.logTextCreated(entered)
    yield (entered, event)

  def updateText(text: Text): ConnectionIO[AppEvent] =
    val q = sql"""
      update visit_text set content = ${text.content} where text_id = ${text.textId}
    """
    for
      affected <- q.update.run
      _ = if affected != 1 then throw new RuntimeException(s"Failed to update text: ${text.textId}")
      updated <- getText(text.textId).unique
      event <- DbEventPrim.logTextUpdated(updated)
    yield event

  def deleteText(textId: Int): ConnectionIO[AppEvent] =
    val q = sql"""
      delete from visit_text where text_id = ${textId}
    """
    for
      t <- getText(textId).unique
      affected <- q.update.run
      _ = if affected != 1 then throw new RuntimeException(s"Failed to delete text: ${textId}")
      ndel <- q.update.run
      _ = if ndel != 1 then throw new RuntimeException(s"Failed to delete multiple texts: ${textId}")
      event <- DbEventPrim.logTextDeleted(t)
    yield event