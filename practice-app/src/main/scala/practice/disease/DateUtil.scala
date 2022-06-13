package dev.myclinic.scala.web.practiceapp.practice.disease

import dev.fujiwara.kanjidate.KanjiDate
import java.time.LocalDate

object DateUtil:
  def formatDate(d: LocalDate): String =
    val info = KanjiDate.DateInfo(d)
    s"${info.gengouAlphaChar}${info.nen}.${info.month}.${info.day}"
