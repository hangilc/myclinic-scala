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
  def countTextForVisit(visitId: Int): ConnectionIO[Int] =
    sql"""
      select count(*) from visit_text where visit_id = ${visitId}
    """.query[Int].unique

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
      insert into visit_text (visit_id, content, memo) 
      values 
      (${text.visitId}, ${text.content}, ${text.memo}) 
    """
    for
      id <- q.update.withUniqueGeneratedKeys[Int]("text_id")
      entered <- getText(id).unique
      event <- DbEventPrim.logTextCreated(entered)
    yield (entered, event)

  def updateText(text: Text): ConnectionIO[AppEvent] =
    val q = sql"""
      update visit_text set content = ${text.content}, memo = ${text.memo} 
      where text_id = ${text.textId}
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
      event <- DbEventPrim.logTextDeleted(t)
    yield event

  def searchTextGlobally(text: String, limit: Int, offset: Int): Query0[(Text, Visit, Patient)] =
    val like = s"%${text}%"
    sql"""
      select t.*, v.*, p.* from visit_text as t inner join visit v on t.visit_id = v.visit_id 
        inner join patient as p on v.patient_id = p.patient_id
        where content like ${like} order by text_id desc limit ${limit} offset ${offset}
    """.query[(Text, Visit, Patient)]

  def countSearchTextGlobally(text: String): ConnectionIO[Int] =
    val like = s"%${text}%"
    sql"""
      select count(*) from visit_text where content like ${like} 
    """.query[Int].unique

  def searchTextForPatient(text: String, patientId: Int, limit: Int, offset: Int): Query0[(Text, Visit)] =
    val like = s"%${text}%"
    sql"""
      select t.*, v.* from visit_text as t inner join visit as v on t.visit_id = v.visit_id
        where v.patient_id = ${patientId} and content like ${like} order by text_id desc
        limit ${limit} offset ${offset}
    """.query[(Text, Visit)]

  def countSearchTextForPatient(text: String, patientId: Int): ConnectionIO[Int] =
    val like = s"%${text}%"
    sql"""
      select count(*) from visit_text as t inner join visit as v on t.visit_id = v.visit_id
        where v.patient_id = ${patientId} and content like ${like}
    """.query[Int].unique





