package dev.myclinic.scala.model

import java.time.LocalDate
import java.time.LocalTime
import java.time.LocalDateTime

case class Appoint(
  date: LocalDate,
  time: LocalTime,
  eventId: Int,
  patientName: String,
  patientId: Int,
  memo: String
) {
  def isVacant: Boolean = patientName.isEmpty
}

case class AppEvent(
  id: Int,
  eventId: Int,
  createdAt: LocalDateTime,
  model: String,
  kind: String,
  data: String
)