package dev.fujiwara.domq

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{ContextMenu, Modal}
import dev.myclinic.scala.util.KanjiDate.Gengou
import scala.language.implicitConversions
import scala.collection.JavaConverters.*
import org.scalajs.dom.raw.{MouseEvent, HTMLElement}
import java.time.LocalDate
import scala.collection.mutable.ListBuffer

class DatePicker(
    initialDate: LocalDate,
    gengouList: List[Gengou] = Gengou.list,
    zIndex: Int = Modal.zIndexDefault
):
  val menu = new ContextMenu(zIndex)
  val eGengouSelect = select()
  val eNenSelect = select()
  val eDatesTab = div()
  menu.menu(
    div(cls := "domq-date-picker-month-block")(
      eGengouSelect.setChildren(
        gengouList.map(g => option(g.name, value := g.name).ele)
          ++ List(option("西暦", value := "西暦").ele)
      ),
      eNenSelect,
      span("年", cls := "label"),
      "１２月"
    ),
    eDatesTab(cls := "domq-date-picker-dates-tab")
  )
  stuffDates(initialDate.getYear, initialDate.getMonthValue)
  adaptNenInput()


  def open(event: MouseEvent) = menu.open(event)

  private case class Seireki()
 
  private def adaptNenInput(): Unit =
    selectedGengou match {
      case g: Gengou => setupNenSelect(g)
      case _: Seireki => ()
    }


  private def selectedGengou: Gengou | Seireki =
    val valueOpt: Option[String] =
      eGengouSelect.getSelectedOptionValues.headOption
    valueOpt.fold(new Seireki())(s =>
      Gengou.findByName(s).getOrElse(new Seireki())
    )

  private def setupNenSelect(g: Gengou): Unit =
    eNenSelect.setChildren(
      g.listNen.toList.map(n => option(n.toString, value := n.toString))
    )

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
