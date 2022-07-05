package dev.fujiwara.domq.dateinput

import java.time.LocalDate

trait InitNoneConverter:
  def convert: Option[LocalDate]

object InitNoneConverter:
  val defaultInitNoneFun: () => Option[LocalDate] = 
    () => Some(LocalDate.now())

  given defaultInitNoneConverter: InitNoneConverter with
    def convert: Option[LocalDate] = defaultInitNoneFun()

