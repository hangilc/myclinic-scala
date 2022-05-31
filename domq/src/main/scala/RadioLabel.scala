package dev.fujiwara.domq

import Html.{*, given}
import Modifiers.{*, given}
import ElementQ.{*, given}
import org.scalajs.dom.HTMLInputElement

case class RadioLabel[T](name: String, value: T, label: String):
  val labelElement = span(innerText := label)
  val radioElement = radio(Modifiers.name := name)

  def checked: Boolean = radioElement.checked

