package dev.myclinic.scala.util

import java.time.{LocalDate, LocalTime}

object KanjiDate {

  val youbiList: List[String] = List("日", "月", "火", "水", "木", "金", "土")

  def youbi(d: LocalDate): String =
    val i = d.getDayOfWeek().getValue() % 7
    youbiList(i)
  
  def dateToKanji(d: LocalDate, includeYoubi: Boolean = false): String =
    val year = d.getYear
    val month = d.getMonthValue
    val day = d.getDayOfMonth
    val dow = if includeYoubi then 
      {
        s"（${youbi(d)}）"
      }  
      else ""

    s"${year}年${month}月${day}日${dow}"

  def timeToKanji(t: LocalTime): String =
    val hour = t.getHour
    val minute = t.getMinute
    s"${hour}時${minute}分"
}