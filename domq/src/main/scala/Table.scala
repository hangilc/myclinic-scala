package dev.fujiwara.domq

import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.{HTMLElement}
import scala.collection.mutable.ListBuffer

class Table:
  val ele = div(display := "table", cls := "table")
  private var rowBuf = ListBuffer[HTMLElement]()
  def setColumns(specs: List[HTMLElement => Unit]): Unit =
    specs
      .map(spec => {
        val c = div(display := "table-column")
        spec(c)
        c
      })
      .foreach(ele(_))
  private def addRowElements(cells: List[HTMLElement => Unit]): HTMLElement =
    val row = div(display := "table-row")
    cells.foreach(m => {
      val e = div(display := "table-cell")
      m(e)
      row(e)
    })
    ele(row)
    row
  def addHeaderRow(cells: List[HTMLElement => Unit]): HTMLElement =
    addRowElements(
      cells.map(m => (e: HTMLElement) => { e(cls := "header"); m(e) })
    )
  def addRow(cells: List[HTMLElement => Unit]): HTMLElement =
    val row = addRowElements(cells)
    rowBuf += row
    row
  def clear(): Unit =
    rowBuf.foreach(_.remove())
    rowBuf.clear
