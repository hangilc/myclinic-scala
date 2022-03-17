package dev.fujiwara.domq

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.HTMLElement

class Selection[S, T](mapper: S => T):
  private val onSelect = LocalEventPublisher[T]
  def addSelectEventHandler(handler: T => Unit): Unit =
    onSelect.subscribe(handler)
  private val onUnselect = LocalEventPublisher[Unit]
  def addUnselectEventHandler(handler: Unit => Unit): Unit =
    onUnselect.subscribe(handler)
  private val onSingleResult = LocalEventPublisher[T]
  def addSingleResultEventHandler(handler: T => Unit): Unit =
    onSingleResult.subscribe(handler)

  private var selectedValue: Option[T] = None
  var formatter: S => String = _.toString
  val ele = div(cls := "domq-selection")
  private var items: List[SelectionItem[T]] = List.empty

  def clear(): Unit = 
    ele(dev.fujiwara.domq.Modifiers.clear)
    items = List.empty
    selectedValue = None
  def add(s: S): Unit =
    val item = new SelectionItem(formatter(s), mapper(s))
    item.ele(onclick := (() => selectItem(item)))
    ele(item.ele)
    items = items :+ item
  def addAll(ss: List[S]): Unit = ss.foreach(add(_))
  def noMore(): Unit =
    if items.size == 1 then onSingleResult.publish(items(0).value)
  def set(ss: List[S]): Unit =
    clear()
    addAll(ss)
    noMore()
  def show(): Unit = ele(displayDefault)
  def hide(): Unit = ele(displayNone)
  def scrollToTop: Unit = ele.scrollTop = 0
  def selected: Option[T] = selectedValue
  def select(value: T, fireEvent: Boolean = true): Boolean =
    findItemByValue(value).fold(false)(item => {
      selectItem(item)
      true
    })
  def unselect(): Unit =
    selectedValue = None
    unmark()
    onUnselect.publish(())

  private def findItemByValue(value: T): Option[SelectionItem[T]] =
    items.find(_.value == value)
  private def selectItem(item: SelectionItem[T]): Unit =
    unmark()
    item.mark()
    selectedValue = Some(item.value)
    onSelect.publish(item.value)
  private def unmark(): Unit =
    items.foreach(_.unmark())

object Selection:
  def apply[T](): Selection[T, T] = new Selection[T, T](identity)
  def apply[T](
      items: List[(String, T)] = List.empty,
      onSelect: T => Unit = ((_: T) => ())
  ): Selection[(String, T), T] =
    val sel = new Selection[(String, T), T](_._2)
    sel.addSelectEventHandler(onSelect)
    sel.addAll(items)
    sel

private class SelectionItem[T](val label: String, val value: T):
  val ele: HTMLElement = div(cls := "domq-selection-item")(
    label
  )
  def mark(): Unit = ele(cls := "selected")
  def unmark(): Unit = ele(cls :- "selected")
