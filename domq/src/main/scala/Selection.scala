package dev.fujiwara.domq

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.HTMLElement

class Selection[T]:
  var onSelect: T => Unit = _ => ()
  private var selectedValue: Option[T] = None
  var formatter: T => String = _.toString
  val ui = new Selection.UI
  val ele = ui.ele
  def clear(): Unit = ele(dev.fujiwara.domq.Modifiers.clear)
  def add(t: T): Unit = addItem(new SelectionItem(formatter(t), t))
  def add(label: String, t: T): Unit = addItem(new SelectionItem(label, t))
  def addAll(ts: List[T]): Unit = ts.foreach(add(_))
  def show(): Unit = ui.ele(displayDefault)
  def hide(): Unit = ui.ele(displayNone)
  def scrollToTop: Unit = ui.ele.scrollTop = 0
  def selected: Option[T] = selectedValue

  private def addItem(item: SelectionItem[T]): Unit =
    item.onSelect = value => {
      clearSelected()
      item.ele(cls := "selected")
      selectedValue = Some(value)
    }
    ele(item.ele)

  private def clearSelected(): Unit =
    ele.listChild().foreach(_(cls :- "selected"))

object Selection:
  def apply[T](): Selection[T] = new Selection[T]
  def apply[T](
      items: List[(String, T)] = List.empty,
      onSelect: T => Unit = ((_: T) => ())
  ): Selection[T] =
    val ui = new UI
    val sel = new Selection[T]
    sel.onSelect = onSelect
    items.foreach {
      case (label, value) => sel.add(label, value)
    }
    sel

  class UI:
    val ele = div(cls := "domq-selection")
    def hide: UI = 
      ele(displayNone)
      this
    def show: UI =
      ele(displayDefault)
      this

private class SelectionItem[T](label: String, value: T):
  var onSelect: T => Unit = _ => ()
  val ele: HTMLElement = div(cls := "domq-selection-item")(
    label,
    onclick := (() => onSelect(value))
  )
