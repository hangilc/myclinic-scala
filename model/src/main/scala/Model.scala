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
):
  def isVacant: Boolean = patientName.isEmpty

  def sameDateTime(that: Appoint): Boolean =
    date == that.date && time == that.time

  def requireUpdate(newAppoint: Appoint): Boolean =
    sameDateTime(newAppoint) && eventId < newAppoint.eventId

case class AppEvent(
  id: Int,
  eventId: Int,
  createdAt: LocalDateTime,
  model: String,
  kind: String,
  data: String
)