package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.{DbPatientPrim => Prim}
import doobie.*
import doobie.implicits.*

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

trait DbPatient extends Mysql:
  def getPatient(patientId: Int): IO[Patient] =
    mysql(Prim.getPatient(patientId).unique)

  def findPatient(patientId: Int): IO[Option[Patient]] =
    mysql(Prim.getPatient(patientId).option)

  def searchPatient(text: String): IO[List[Patient]] =
    val pat = Pattern.compile(raw"\s+", Pattern.UNICODE_CHARACTER_CLASS)
    val parts: Array[String] = pat.split(text, 2)
    if parts.size == 0 then IO(List.empty)
    else if parts.size == 1 then mysql(Prim.searchPatient(text).to[List])
    else mysql(Prim.searchPatient(parts(0), parts(1)).to[List])

  def batchGetPatient(patientIds: List[Int]): IO[Map[Int, Patient]] =
    mysql(DbPatientPrim.batchGetPatient(patientIds))

  def enterPatient(patient: Patient): IO[AppEvent] =
    mysql(Prim.enterPatient(patient))

  def updatePatient(patient: Patient): IO[AppEvent] =
    mysql(Prim.updatePatient(patient))
