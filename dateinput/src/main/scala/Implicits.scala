package dev.fujiwara.dateinput

import java.time.LocalDate

trait InitNoneConverter:
  def convert: Option[LocalDate]

object InitNoneConverter:
  given InitNoneConverter with
    def convert: Option[LocalDate] = Some(LocalDate.now())

