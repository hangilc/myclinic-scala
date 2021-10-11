package dev.myclinic.scala.model

import java.time.LocalTime

object DateTimeOrder:
  given localTimeOrder: Ordering[LocalTime] with
    def compare(self: LocalTime, other: LocalTime): Int =
      if self.equals(other) then 0
      else if self.isBefore(other) then -1
      else 1
