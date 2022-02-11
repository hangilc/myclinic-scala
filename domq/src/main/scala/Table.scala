package dev.fujiwara.domq

import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import scala.collection.mutable.ListBuffer

class Table:
  val ele = div(display := "table", cls := "table")
  def addColumns(cols: List[HTMLElement]): Unit =
    ele(children := cols)
  def addRow(row: HTMLElement): Unit = ele(row)
  def clear(): Unit = ele(dev.fujiwara.domq.all.clear)

object Table:
  def column: HTMLElement = div(display := "table-column")
  def row: HTMLElement = div(display := "table-row")
  def cell: HTMLElement = div(display := "table-cell")
  def headerCell: HTMLElement = cell(cls := "header")
  def createRow(cells: List[HTMLElement]): HTMLElement =
    row(children := cells)
