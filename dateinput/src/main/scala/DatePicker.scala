package dev.fujiwara.dateinput

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{ContextMenu, Modal, Modifier}
import dev.fujiwara.kanjidate.KanjiDate.{Gengou, Wareki}
import scala.language.implicitConversions
import scala.scalajs.js
import org.scalajs.dom.raw.{MouseEvent, HTMLElement, Event}
import java.time.LocalDate
import scala.collection.mutable.ListBuffer
import org.scalajs.dom.raw.KeyboardEvent
import org.scalajs.dom.raw.HTMLInputElement

class DatePicker(
    cb: LocalDate => Unit = _ => (),
    gengouList: List[Gengou] = Gengou.list,
    zIndex: Int = Modal.zIndexDefault
):
  val menu = new ContextMenu(zIndex)
  val eGengouSelect = select()
  val eNenSelect = select()
  val eMonthSelect = select()
  val eDatesTab = div()
  menu.menu(
    div(cls := "domq-date-picker-month-block")(
      eGengouSelect(
        cls := "domq-gengou-select",
        onchange := (onGengouChange _)
      ).setChildren(
        gengouList.map(g => option(g.name, value := g.name).ele)
      ),
      eNenSelect(cls := "domq-nen-select", onchange := (onNenOrMonthChange _)),
      a("年", cls := "domq-nen-label", onclick := (advanceYear _)),
      eMonthSelect(
        cls := "domq-month-select",
        onclick := (onNenOrMonthChange _)
      ).setChildren(
        (1 to 12).toList.map(i => option(i.toString, value := i.toString))
      ),
      a("月", cls := "domq-month-label", onclick := (advanceMonth _))
    ),
    eDatesTab(cls := "domq-date-picker-dates-tab")
  )

  def open(event: MouseEvent, year: Int, month: Int) =
    set(year, month)
    menu.open(event)

  private def init(): Unit =
    val g = eGengouSelect

  def set(year: Int, month: Int): Unit =
    val d = LocalDate.of(year, month, 1)
    Wareki.fromDate(d) match {
      case Some(w) =>
        if currentGengou != w.gengou then
          eGengouSelect.setSelectValue(w.gengou.name)
          setupNenSelect(w.gengou)
        ensureNen(w.nen)
        println(("set", w))
        eNenSelect.setSelectValue(w.nen.toString)
        eMonthSelect.setSelectValue(month.toString)
        stuffDates(year, month)
      case None => System.err.println(s"Cannot get Gengou of ${year}-${month}")
    }

  private def ensureNen(nen: Int): Unit =
    val last: Int = eNenSelect
      .qSelectorAll("option")
      .lastOption
      .map(_.asInstanceOf[HTMLInputElement].value.toInt)
      .getOrElse(0)
    if last < nen then
      val opts: List[Modifier] = ((last + 1) to nen).toList.map(mkNenOption(_))
      eNenSelect(opts)

  private def advanceYear(event: MouseEvent): Unit =
    var n = 1
    if event.ctrlKey then n = 5
    if event.shiftKey then n = -n
    advance(_.plusYears(n))

  private def advanceMonth(event: MouseEvent): Unit =
    var n = 1
    if event.ctrlKey then n = 5
    if event.shiftKey then n = -n
    advance(_.plusMonths(n))

  private def advance(f: LocalDate => LocalDate): Unit =
    val d = f(LocalDate.of(currentYear, currentMonth, 1))
    set(d.getYear, d.getMonthValue)

  private def currentGengou: Gengou =
    Gengou.findByName(eGengouSelect.getSelectValue).get

  private def currentNen: Int =
    eNenSelect.getSelectValue.toInt

  private def currentYear: Int =
    Gengou.gengouToYear(currentGengou, currentNen)

  private def currentMonth: Int =
    eMonthSelect.getSelectValue.toInt

  private def onGengouChange(): Unit =
    val g = currentGengou
    val n = currentNen
    setupNenSelect(g)
    ensureNen(n)
    eNenSelect.setSelectValue(n.toString)
    val y = Gengou.gengouToYear(g, currentNen)
    stuffDates(y, currentMonth)

  private def onNenOrMonthChange(): Unit =
    val g = currentGengou
    val n = currentNen
    val y = Gengou.gengouToYear(g, n)
    stuffDates(y, currentMonth)

  private def setupNenSelect(g: Gengou): Unit =
    eNenSelect.setChildren(
      g.listNen.toList.map(n => mkNenOption(n))
    )

  private def mkNenOption(nen: Int): HTMLElement =
    val v = nen.toString
    option(v, value := v)

  private def stuffDates(year: Int, month: Int): Unit =
    eDatesTab.clear()
    val d1 = LocalDate.of(year, month, 1)
    val pad = d1.getDayOfWeek.getValue % 7
    for _ <- 0 until pad do eDatesTab(div())
    eDatesTab.addChildren(makeDates(year, month))

  private def makeDates(year: Int, month: Int): List[HTMLElement] =
    val start = LocalDate.of(year, month, 1)
    val end = start.plusMonths(1)
    var d = start
    val buf = ListBuffer[LocalDate]()
    while d.isBefore(end) do
      buf.addOne(d)
      d = d.plusDays(1)
    buf.toList.map(d => {
      div(d.getDayOfMonth.toString)(
        cls := "domq-date-picker-date-box",
        onclick := (() => onDayClick(d)),
        attr("data-date") := d.getDayOfMonth.toString
      ).ele
    })

  private def onDayClick(d: LocalDate): Unit =
    menu.close()
    cb(d)
