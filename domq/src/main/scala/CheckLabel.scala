package dev.fujiwara.domq

import Modifiers.{*, given}
import ElementQ.{*, given}
import org.scalajs.dom.HTMLElement

case class CheckLabel[T](value: T, labelString: String):
  val checkId = DomqUtil.genId
  val check = Html.checkbox(id := checkId)
  val label = Html.label(labelString, attr("for") := checkId)
  def selected: Option[T] =
    Option.when(check.checked)(value)
  def wrap(wrapper: HTMLElement): HTMLElement = wrapper(check, label)