package dev.fujiwara.domq

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Modifier}
import org.scalajs.dom.raw.{HTMLElement}
import scala.language.implicitConversions

class Selection[T](
    onSelect: T => Unit = ((_: T) => ())
):
  val ele = div(cls := "appbase-selection")

  def addAll(items: List[(String, T)]): Unit =
    ele.addChildren((items.map { case (label, value) =>
      SelectionItem(label, value).ele
    }))

  def add(item: (String, T)): Unit =
    item match {
      case (label, value) => ele(SelectionItem(label, value).ele)
    }

  def clear(): Unit =
    ele.clear()

  private def clearSelected(): Unit =
    val nodes = ele.querySelectorAll(".domq-selection-item.selected")
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
    val sel = new Selection(onSelect)
    sel.addAll(items)
    sel
