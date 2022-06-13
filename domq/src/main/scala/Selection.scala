package dev.fujiwara.domq

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.HTMLElement

case class SelectionItem[T](ele: HTMLElement, value: T)(using modifier: SelectionConfig):
  def mark(): Unit = ele(cls := "selected")
  def unmark(): Unit = ele(cls :- "selected")
  def isMarked: Boolean = ele.classList.contains("selected")

  modifier.itemCssClass.foreach(itemClass => ele(cls := itemClass))

trait SelectionConfig:
  def itemCssClass: Option[String] = Some(SelectionConfig.defaultItemCssClass)
  def selectionCssClass: Option[String] = Some(SelectionConfig.defaultSelectionCssClass)

object SelectionConfig:
  val defaultItemCssClass: String = "domq-selection-item"
  val defaultSelectionCssClass: String = "domq-selection"

class Selection[T](using config: SelectionConfig):
  val ele = div(cls := config.selectionCssClass)
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

  def addAll(values: List[T], toLabel: T => String): Unit =
    values.foreach(v => add(div(innerText := toLabel(v)), v))

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

  def scrollToTop(): Unit = ele.scrollToTop()

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
  def apply[T]()(using SelectionConfig): Selection[T] = new Selection[T]
  def apply[T](
      items: List[(HTMLElement, T)] = List.empty,
      onSelect: T => Unit = ((_: T) => ())
  )(using SelectionConfig): Selection[T] =
    val sel = Selection[T]()
    sel.addAll(items)
    sel.addSelectEventHandler(onSelect)
    sel
  def apply[T](values: List[T], render: T => HTMLElement, onSelect: T => Unit): Selection[T] =
    val sel = Selection[T]()
    sel.addAll(values.map(v => (render(v), v)))
    sel.addSelectEventHandler(onSelect)
    sel

  given SelectionConfig = new SelectionConfig{}

