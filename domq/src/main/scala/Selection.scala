package dev.fujiwara.domq

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.HTMLElement

class Selection[S, T](mapper: S => T):
  var onSelect: T => Unit = _ => ()
  private var selectedValue: Option[T] = None
  var formatter: S => String = _.toString
  val ele = div(cls := "domq-selection")
  def clear(): Unit = ele(dev.fujiwara.domq.Modifiers.clear)
  def add(s: S): Unit = addItem(new SelectionItem(formatter(s), mapper(s)))
  def addAll(ss: List[S]): Unit = ss.foreach(add(_))
  def show(): Unit = ele(displayDefault)
  def hide(): Unit = ele(displayNone)
  def scrollToTop: Unit = ele.scrollTop = 0
  def selected: Option[T] = selectedValue

  private def addItem(item: SelectionItem[T]): Unit =
    item.onSelect = value => {
      clearSelected()
      item.ele(cls := "selected")
      selectedValue = Some(value)
      onSelect(value)
    }
    ele(item.ele)

  private def clearSelected(): Unit =
    ele.listChild().foreach(_(cls :- "selected"))

object Selection:
  def apply[T](): Selection[T, T] = new Selection[T, T](identity)
  def apply[T](
      items: List[(String, T)] = List.empty,
      onSelect: T => Unit = ((_: T) => ())
  ): Selection[(String, T), T] =
    val sel = new Selection[(String, T), T](_._2)
    sel.onSelect = onSelect
    sel.addAll(items)
    sel

private class SelectionItem[T](label: String, value: T):
  var onSelect: T => Unit = _ => ()
  val ele: HTMLElement = div(cls := "domq-selection-item")(
    label,
    onclick := (() => {
      onSelect(value)
    })
  )
