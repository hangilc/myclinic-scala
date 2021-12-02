package dev.fujiwara.domq

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{ContextMenu, Modal}
import scala.language.implicitConversions
import scala.collection.JavaConverters.*
import org.scalajs.dom.raw.{MouseEvent, HTMLElement}
import java.time.LocalDate
import scala.collection.mutable.ListBuffer

class DatePicker(initialDate: LocalDate, zIndex: Int = Modal.zIndexDefault):
  val menu = new ContextMenu(zIndex)
  val eDatesTab = div()
  menu.menu(
    div(cls := "domq-date-piacker-month-block")(
      "令和３年１２月"
    ),
    eDatesTab(cls := "domq-date-picker-dates-tab")
  )
  stuffDates(initialDate.getYear, initialDate.getMonthValue)

  def open(event: MouseEvent) = menu.open(event)

  private def stuffDates(year: Int, month: Int): Unit =
    eDatesTab.clear()
    eDatesTab.setChildren(makeDates(year, month))

  private def makeDates(year: Int, month: Int): List[HTMLElement] =
    val start = LocalDate.of(year, month, 1)
    val end = start.plusMonths(1)
    var d = start
    val buf = ListBuffer[LocalDate]()
    println(("dates", buf.toList))
    while d.isBefore(end) do
      buf.addOne(d)
      d = d.plusDays(1)
    buf.toList.map(d => {
      div(cls := "domq-date-picker-date-box")(d.getDayOfMonth.toString).ele
    })
