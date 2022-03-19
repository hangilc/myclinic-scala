package dev.fujiwara.domq

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.Modifiers
import scala.language.implicitConversions
import org.scalajs.dom.HTMLElement

class SelectionUI:
  val ele = div

class SelectionItem[T](val value: T):
  val ele = div
  def mark(): Unit = ele(cls := "selected")
  def unmark(): Unit = ele(cls :- "selected")

object SelectionItem:
  def apply[T](label: String, value: T): SelectionItem[T] =
    val item = new SelectionItem[T](value)
    item.ele(innerText := label)
    item

class Selection[T]:
  val ele = div
  private val selectEventPublisher = LocalEventPublisher[T]
  private var items: List[SelectionItem[T]] = List.empty
  private var markedValue: Option[T] = None

  def addSelectEventHandler(handler: T => Unit): Unit = 
    selectEventPublisher.subscribe(handler)
  private def publishSelect(t: T): Unit = selectEventPublisher.publish(t)

  def add[U](u: U, toLabel: U => String, toValue: U => T): Unit =
    val item = SelectionItem(toLabel(u), toValue(u))
    items = items :+ item
    item.ele(onclick := (() => onItemClick(item)))
    ele(item.ele)

  def addAll[U](us: List[U], toLabel: U => String, toValue: U => T): Unit =
    us.foreach(add(_, toLabel, toValue))

  def clear(): Unit =
    ele(Modifiers.clear)
    markedValue = None
    items = List.empty

  def mark(value: T): Unit =
    items.find(_.value == value).foreach(item => 
      unmark()
      markedValue = Some(item.value)
      item.mark()  
    )

  def select(value: T): Unit =
    items.find(_.value == value).foreach(selectItem(_))

  def marked: Option[T] = markedValue

  private def onItemClick(item: SelectionItem[T]): Unit =
    selectItem(item)

  private def selectItem(item: SelectionItem[T]): Unit =
    unmark()
    markedValue = Some(item.value)
    item.mark()
    publishSelect(item.value)

  def unmark(): Unit = 
    markedValue = None
    items.foreach(_.unmark())

object Selection:
  def apply[T] = new Selection[T]
  def apply[T](
      items: List[(String, T)] = List.empty,
      onSelect: T => Unit = ((_: T) => ())
  ): Selection[T] =
    val sel = Selection[T]
    sel.addAll(items, _._1, _._2)
    sel

