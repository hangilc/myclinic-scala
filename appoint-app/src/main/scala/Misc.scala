package dev.myclinic.scala.web.appoint

import java.time.LocalDate
import dev.myclinic.scala.util.KanjiDate
import java.time.LocalTime
import dev.myclinic.scala.model.Appoint

object Misc {

  def formatAppointDate(d: LocalDate): String = {
    val youbi = KanjiDate.youbi(d)
    s"${d.getMonthValue()}月${d.getDayOfMonth()}日（$youbi）"
  }

  def formatAppointTime(t: LocalTime): String = {
    f"${t.getHour()}%02d:${t.getMinute()}%02d"
  }

  def formatAppointDateTime(a: Appoint): String = {
    val d = formatAppointDate(a.date)
    val t = formatAppointTime(a.time)
    d + t
  }

}