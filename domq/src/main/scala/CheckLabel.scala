package dev.fujiwara.domq

import Modifiers.{*, given}
import ElementQ.{*, given}
import org.scalajs.dom.{HTMLElement, HTMLLabelElement}
import scala.language.implicitConversions

case class CheckLabel[T](value: T, labelStuffer: HTMLLabelElement => Unit):
  val checkId = GenSym.genSym
  val checkElement = Html.checkbox(id := checkId)
  val labelElement = Html.label(attr("for") := checkId)
  labelStuffer(labelElement)
  def selected: Option[T] =
    Option.when(checkElement.checked)(value)
  def wrap(wrapper: HTMLElement): HTMLElement = wrapper(checkElement, labelElement)
  def check: Unit = checkElement.checked = true
  def uncheck: Unit = checkElement.checked = false
  def addOnInputListener(listener: CheckLabel[T] => Unit): Unit =
    checkElement(oninput := (_ => listener(this)))

object CheckLabel:
  def apply[T](value: T, labelString: String): CheckLabel[T] =
    new CheckLabel[T](value, _(labelString))

  def apply[T](value: T, stuffer: HTMLLabelElement => Unit): CheckLabel[T] =
    new CheckLabel[T](value, stuffer)