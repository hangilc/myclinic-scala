package dev.fujiwara.domq

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.HTMLElement

class Selection[S, T](mapper: S => T):
  var onSelect: T => Unit = _ => ()
  private var selectedValue: Option[T] = None
  var formatter: S => String = _.toString
  val ele = div(cls := "domq-selection")
  private var items: List[SelectionItem[T]] = List.empty
  def clear(): Unit = 
    ele(dev.fujiwara.domq.Modifiers.clear)
    items = List.empty
  def add(s: S): Unit = addItem(new SelectionItem(formatter(s), mapper(s)))
  def addAll(ss: List[S]): Unit = ss.foreach(add(_))
  def set(ss: List[S]): Unit =
    clear()
    addAll(ss)
  def show(): Unit = ele(displayDefault)
  def hide(): Unit = ele(displayNone)
  def scrollToTop: Unit = ele.scrollTop = 0
  def selected: Option[T] = selectedValue
  def select(value: T): Boolean =
    items.find(_.value == value).fold(
      false
    )(sel => {
      selectItem(sel)
      true
    })
  def selectOpt(optValue: Option[T]): Boolean =
    optValue match {
      case Some(value) => select(value)
      case None => 
        clearSelected()
        false
    }
  private def selectItem(item: SelectionItem[T]): Unit =
    clearSelected()
    item.ele(cls := "selected")
    selectedValue = Some(item.value)

  private def addItem(item: SelectionItem[T]): Unit =
    item.onSelect = value => {
      selectItem(item)
      onSelect(value)
    }
    ele(item.ele)
    items = items :+ item

  def clearSelected(): Unit =
    items.foreach(item => item.ele(cls :- "selected"))

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

private class SelectionItem[T](val label: String, val value: T):
  var onSelect: T => Unit = _ => ()
  val ele: HTMLElement = div(cls := "domq-selection-item")(
    label,
    onclick := (() => onSelect(value))
  )
