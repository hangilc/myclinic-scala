package dev.fujiwara.dateinput.datepicker

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.kanjidate.KanjiDate.{Gengou}

case class YearDisp(var g: Gengou, var nen: Int):
  private val changeYearPublisher = new LocalEventPublisher[Int]

  val yearSpan = span
  val ele = div(cls := "domq-display-inline-block domq-date-picker-year-disp",
    Icons.chevronLeft(cls := "domq-icon-chevron-left", onclick := (() => doChangeYear(-1))),
    yearSpan,
    Icons.chevronRight(cls := "domq-icon-chevron-right", onclick := (() => doChangeYear(1)))
  )
  updateUI()

  def year: Int = Gengou.gengouToYear(g, nen)

  def set(newGengou: Gengou, newNen: Int): Unit =
    g = newGengou
    nen = newNen
    updateUI()

  def onChangeYear(handler: Int => Unit): Unit = changeYearPublisher.subscribe(handler)

  private def doChangeYear(n: Int): Unit =
    changeYearPublisher.publish(year + n)

  private def updateUI(): Unit =
    yearSpan(innerText := f"${g.name}${nen}%02d年")



