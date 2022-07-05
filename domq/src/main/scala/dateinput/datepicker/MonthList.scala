package dev.fujiwara.domq.dateinput.datepicker

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.kanjidate.KanjiDate.Gengou
import dev.fujiwara.kanjidate.KanjiDate
import java.time.LocalDate

case class MonthList():
  val selection = Selection[Int](
    (1 to 12).toList,
    m => div(f"${m}%02d月", cls := "domq-word-break-keep-all")
  )
  val ele = selection.ele


