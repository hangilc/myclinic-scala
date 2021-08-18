package dev.myclinic.scala.model

import java.time.LocalDate
import java.time.LocalTime

case class Appoint(
  date: LocalDate,
  time: LocalTime,
  patientName: String,
  patientId: Option[Int],
  memo: String
)