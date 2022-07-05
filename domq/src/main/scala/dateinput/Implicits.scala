package dev.fujiwara.domq.dateinput

import java.time.LocalDate

object Implicits:
  case class Suggest(f: () => Option[LocalDate]):
    def value: Option[LocalDate] = f()

  given defaultSuggest: Suggest = Suggest(() => Some(LocalDate.now()))


