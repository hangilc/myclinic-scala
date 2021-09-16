package dev.myclinic.scala.util

import java.time.LocalDate

object KanjiDate {

  val youbiList: List[String] = List("日", "月", "火", "水", "木", "金", "土")

  def youbi(d: LocalDate): String =
    val i = d.getDayOfWeek().getValue()
    youbiList(i)
}