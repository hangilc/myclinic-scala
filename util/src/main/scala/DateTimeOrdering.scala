package dev.myclinic.scala.util

import java.time.{LocalDate, LocalTime}

object DateTimeOrdering:
  given Ordering[LocalDate] with
    def compare(a: LocalDate, b: LocalDate): Int =
      if a.isBefore(b) then -1
      else if a.equals(b) then 0
      else 1

  given Ordering[LocalTime] with
    def compare(a: LocalTime, b: LocalTime): Int =
      if a.isBefore(b) then -1
      else if a.equals(b) then 0
      else 1
