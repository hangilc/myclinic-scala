package dev.fujiwara.domq

import ElementQ.{*, given}
import Html.{*, given}
import Modifiers.{*, given}
import org.scalajs.dom.HTMLOptionElement

case class OptionProxy[T](value: T):
  val ele = option

  def stuff(stuffer: (HTMLOptionElement, T) => Unit): OptionProxy[T] =
    stuffer(ele, value)
    this