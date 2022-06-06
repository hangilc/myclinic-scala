package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.{DbTextPrim => Prim}
import doobie.*
import doobie.implicits.*

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

trait DbText extends Mysql:
  def countTextForVisit(visitId: Int): IO[Int] =
    mysql(sql"""
      select count(*) from visit_text where visit_id = ${visitId}
    """.query[Int].unique)

  def batchGetText(visitIds: List[Int]): IO[Map[Int, List[Text]]] =
    mysql(visitIds.map(visitId => {
      for
       texts <- Prim.listTextForVisit(visitId)
      yield (visitId, texts)
    }).sequence).map(list => list.foldLeft(Map.empty[Int, List[Text]])(
      (m, e) => e match {
        case (visitId, texts) => m + (visitId -> texts)
      }
    ))

  def getText(textId: Int): IO[Text] = mysql(DbTextPrim.getText(textId).unique)

  def enterText(text: Text): IO[(Text, AppEvent)] =
    mysql(DbTextPrim.enterText(text))

  def updateText(text: Text): IO[AppEvent] =
    mysql(DbTextPrim.updateText(text))

  def deleteText(textId: Int): IO[AppEvent] =
    mysql(DbTextPrim.deleteText(textId))

  def searchTextGlobally(text: String): IO[List[(Text, Visit, Patient)]] =
    mysql(DbTextPrim.searchTextGlobally(text).to[List])

  def searchTextForPatient(text: String, patientId: Int): IO[List[(Text, Visit)]] =
    mysql(DbTextPrim.searchTextForPatient(text, patientId).to[List])


  