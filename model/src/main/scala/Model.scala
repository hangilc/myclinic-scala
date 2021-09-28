package dev.myclinic.scala.model

import java.time.LocalDate
import java.time.LocalTime
import java.time.LocalDateTime

case class AppointTime(
  appointTimeId: Int,
  eventId: Int,
  date: LocalDate,
  fromTime: LocalTime,
  untilTime: LocalTime,
  kind: String,
  capacity: Int
)

case class Appoint(
  appointId: Int,
  eventId: Int,
  appointTimeId: Int,
  patientName: String,
  patientId: Int,
  memo: String
)

case class AppEvent(
  id: Int,
  eventId: Int,
  createdAt: LocalDateTime,
  model: String,
  kind: String,
  data: String
)