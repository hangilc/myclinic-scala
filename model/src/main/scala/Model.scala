package dev.myclinic.scala.model

import java.time.LocalDate
import java.time.LocalTime
import java.time.LocalDateTime

case class Appoint(
  date: LocalDate,
  time: LocalTime,
  patientName: String,
  patientId: Int,
  memo: String
) {
  def isVacant: Boolean = patientName.isEmpty
}

object Appoint {

  def create(date: LocalDate, time: LocalTime): Appoint =
    Appoint(date, time, "", 0, "")
}

case class AppEvent(
  id: Int,
  createdAt: LocalDateTime,
  model: String,
  kind: String,
  data: String
)