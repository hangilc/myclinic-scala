package dev.fujiwara.dateinput.datepicker

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.kanjidate.KanjiDate.{Gengou}

case class YearDisp(var g: Gengou, var nen: Int):
  val yearSpan = span
  val ele = div(cls := "domq-display-inline-block domq-date-picker-year-disp",
    Icons.chevronLeft(cls := "domq-icon-chevron-left"),
    yearSpan,
    Icons.chevronRight(cls := "domq-icon-chevron-right")
  )
  updateUI()

  def year: Int = Gengou.gengouToYear(g, nen)

  def updateUI(): Unit =
    yearSpan(innerText := s"${g.name}${nen}年")



