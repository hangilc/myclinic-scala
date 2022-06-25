package dev.fujiwara.dateinput.datepicker

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.kanjidate.KanjiDate.{Gengou}
import org.scalajs.dom.MouseEvent
import dev.fujiwara.kanjidate.KanjiDate
import dev.fujiwara.kanjidate.KanjiDate.{Era, Gengou, Seireki}
import java.time.LocalDate

case class YearDisp(var year: Int):
  private val changeYearPublisher = new LocalEventPublisher[Int]

  val yearSpan = span
  val ele = div(cls := "domq-display-inline-block domq-date-picker-year-disp domq-user-select-none",
    Icons.chevronLeft(cls := "domq-icon-chevron-left", onclick := (() => simulateChange(_ - 1))),
    yearSpan(cls := "domq-cursor-pointer", onclick := (doYearClick _)),
    Icons.chevronRight(cls := "domq-icon-chevron-right", onclick := (() => simulateChange(_ + 1)))
  )
  updateUI()

  def set(newYear: Int): Unit =
    year = newYear
    updateUI()

  def simulateChange(f: Int => Int): Unit =
    set(f(year))
    changeYearPublisher.publish(year)

  def simulateChange(newYear: Int): Unit =
    simulateChange(_ => newYear)

  def onChangeYear(handler: Int => Unit): Unit = changeYearPublisher.subscribe(handler)

  private def doYearClick(event: MouseEvent): Unit =
    val yearList = YearList(1926, LocalDate.now().getYear + 6)
    yearList.ele(cls := "domq-background-white")
    Absolute.position(yearList.ele)
    val close = Absolute.openWithScreen(yearList.ele, e => {
      val (x, y) = Absolute.clickPos(event)
      Absolute.setLeftOf(e, x + 8)
      Absolute.setBottomOf(e, y + 20)
      Absolute.ensureInViewOffsetting(e, 10)
    })
    yearList.selection.addSelectEventHandler(year => {
      close()
      changeYearPublisher.publish(year)
    })

  private def updateUI(): Unit =
    val (era, nen) = Gengou.dateToEra(LocalDate.of(year, 12, 31))
    val eraName = 
      era match {
        case g: Gengou => g.name
        case s: Seireki => ""
      }
    yearSpan(innerText := f"${eraName}${nen}%02d年")



