package dev.fujiwara.domq

import Modifiers.{*, given}
import ElementQ.{*, given}
import org.scalajs.dom.HTMLElement

case class CheckLabel[T](value: T, labelString: String):
  val checkId = DomqUtil.genId
  val checkElement = Html.checkbox(id := checkId)
  val labelElement = Html.label(labelString, attr("for") := checkId)
  def selected: Option[T] =
    Option.when(checkElement.checked)(value)
  def wrap(wrapper: HTMLElement): HTMLElement = wrapper(checkElement, labelElement)
  def check: Unit = checkElement.checked = true
  def uncheck: Unit = checkElement.checked = false