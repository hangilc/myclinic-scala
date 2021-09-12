package dev.myclinic.scala.modeljson

import java.time.LocalDate
import java.time.LocalTime

object DateTime {
  def convert(d: LocalDate): String = d.toString()
}