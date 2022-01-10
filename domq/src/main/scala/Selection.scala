package dev.fujiwara.domq

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Modifier}
import org.scalajs.dom.{HTMLElement}
import scala.language.implicitConversions

class Selection[T](
    val ui: Selection.UI,
    onSelect: T => Unit = ((_: T) => ())
):
  val ele = ui.ele
  def addAll(items: List[(String, T)]): Unit =
    ui.ele.addChildren((items.map { case (label, value) =>
      SelectionItem(label, value).ele
    }))

  def add(item: (String, T)): Unit =
    item match {
      case (label, value) => ui.ele(SelectionItem(label, value).ele)
    }

  def clear(): Unit =
    ui.ele.clear()

  def show(): Unit = ui.ele(displayDefault)
  def hide(): Unit = ui.ele(displayNone)

  def scrollToTop: Unit = ui.ele.scrollTop = 0

  private def clearSelected(): Unit =
    val nodes = ui.ele.querySelectorAll(".domq-selection-item.selected")
    for i <- 0 until nodes.length do
      nodes
        .item(i)
        .asInstanceOf[HTMLElement]
        .classList
        .remove("selected")

  class SelectionItem(label: String, value: T):
    val ele: HTMLElement = div(cls := "domq-selection-item")(
      label,
      onclick := (() => {
        clearSelected()
        addSelected()
        onSelect(value)
        ()
      })
    )

    def addSelected(): Unit =
      ele(cls := "selected")

object Selection:
  def apply[T](
      items: List[(String, T)] = List.empty,
      onSelect: T => Unit = ((_: T) => ())
  ): Selection[T] =
    val ui = new UI
    val sel = new Selection(ui, onSelect)
    sel.addAll(items)
    sel

  class UI:
    val ele = div(cls := "domq-selection")
    def hide: UI = 
      ele(displayNone)
      this
    def show: UI =
      ele(displayDefault)
      this

  def create[T](ui: UI, onSelect: T => Unit): Selection[T] =
    new Selection[T](ui, onSelect)
