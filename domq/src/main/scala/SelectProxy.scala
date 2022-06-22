package dev.fujiwara.domq

import ElementQ.{*, given}
import Html.{*, given}
import Modifiers.{*, given}
import org.scalajs.dom.HTMLOptionElement
import scala.language.implicitConversions

case class SelectProxy[T](
    items: List[T],
    optionStuffer: (HTMLOptionElement, T) => Unit =
      (opt: HTMLOptionElement, t: T) => opt(t.toString)
):
  private val changedEventPublisher = LocalEventPublisher[T]
  private var opts: List[OptionProxy[T]] =
    items.map(item => OptionProxy(item).stuff(optionStuffer))

  val ele = select(
    items.map(item => {
      val opt = option
      optionStuffer(opt, item)
      opt
    }),
    onchange := (doOnChange _)
  )

  def addOnChangeEventHandler(handler: T => Unit): Unit =
    changedEventPublisher.subscribe(handler)

  def selected: T = 
    opts(ele.selectedIndex).value

  def simulateSelect(t: T): Boolean =
    opts.indexWhere(opt => opt.value == t) match {
      case i if i >= 0 => 
        ele.selectedIndex = i
        true
      case _ => false
    }

  def contains(t: T): Boolean =
    opts.find(_.value == t).isDefined

  def prepend(t: T): Unit =
    opts = OptionProxy(t).stuff(optionStuffer) :: opts

  private def doOnChange(): Unit =
    changedEventPublisher.publish(selected)
