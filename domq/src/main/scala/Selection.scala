package dev.fujiwara.domq

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.Modifiers
import scala.language.implicitConversions
import org.scalajs.dom.HTMLElement

case class SelectionItem[T](ele: HTMLElement, value: T):
  def mark(): Unit = ele(cls := "selected")
  def unmark(): Unit = ele(cls :- "selected")
  def isMarked: Boolean = ele.classList.contains("selected")

class Selection[T]:
  val ele = div(cls := "domq-selection")
  private val selectEventPublisher = LocalEventPublisher[T]
  private val unselectEventPublisher = LocalEventPublisher[Unit]
  private var items: List[SelectionItem[T]] = List.empty
  private var markedValue: Option[T] = None

  def addSelectEventHandler(handler: T => Unit): Unit = 
    selectEventPublisher.subscribe(handler)
  private def publishSelect(t: T): Unit = selectEventPublisher.publish(t)

  def addUnselectEventHandler(handler: () => Unit): Unit =
    unselectEventPublisher.subscribe(_ => handler())
  private def publishUnselect(): Unit = unselectEventPublisher.publish(())

  def add(itemElement: HTMLElement, value: T): Unit =
    val item = SelectionItem(itemElement, value)
    items = items :+ item
    item.ele(onclick := (() => onItemClick(item)))
    ele(item.ele)

  def addAll(args: List[(HTMLElement, T)]): Unit =
    args.foreach {
      case (e, v) => add(e, v)
    }

  def addAll(values: List[T], toElement: T => HTMLElement): Unit =
    values.foreach(v => add(toElement(v), v))

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

  def unselect(): Unit =
    unmark()
    markedValue = None
    publishUnselect()

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
  def apply[T]() = new Selection[T]
  def apply[T](
      items: List[(HTMLElement, T)] = List.empty,
      onSelect: T => Unit = ((_: T) => ())
  ): Selection[T] =
    val sel = Selection[T]()
    sel.addAll(items)
    sel.addSelectEventHandler(onSelect)
    sel

