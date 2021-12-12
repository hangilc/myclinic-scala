package dev.myclinic.scala.web.appoint

import java.time.LocalDate
import dev.fujiwara.kanjidate.KanjiDate
import java.time.LocalTime
import dev.myclinic.scala.model.{AppointTime}

object Misc {

  def formatAppointDate(d: LocalDate): String =
    val youbi = KanjiDate.youbi(d)
    s"${d.getMonthValue()}月${d.getDayOfMonth()}日（$youbi）"

  def formatAppointTime(t: LocalTime): String =
    f"${t.getHour()}%02d:${t.getMinute()}%02d"

  def formatAppointDateTime(appointTime: AppointTime): String =
    formatAppointDate(appointTime.date) + formatAppointTime(
      appointTime.fromTime
    )

  def formatAppointTimeSpan(appointTime: AppointTime): String =
    val d = appointTime.date
    val t = appointTime.fromTime
    val youbi = KanjiDate.youbi(d)
    val m = d.getMonthValue()
    val day = d.getDayOfMonth()
    val hour = t.getHour()
    val minute = t.getMinute()
    val hour2 = appointTime.untilTime.getHour()
    val minute2 = appointTime.untilTime.getMinute()
    s"${m}月${day}日（$youbi）${hour}時${minute}分 - ${hour2}時${minute2}分"

}
