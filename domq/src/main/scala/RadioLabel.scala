package dev.fujiwara.domq

import Html.{*, given}
import Modifiers.{*, given}
import ElementQ.{*, given}
import org.scalajs.dom.HTMLInputElement
import org.scalajs.dom.HTMLElement

case class RadioLabel[T](name: String, value: T, label: String, wrapper: HTMLElement = span):
  val id: String = GenSym.genSym
  val labelElement = Html.label(innerText := label, attr("for") := id)
  val radioElement = radio(Modifiers.name := name, Modifiers.id := id)
  val ele = wrapper(radioElement, labelElement)

  def checked: Boolean = radioElement.checked
  def check(): Unit = radioElement.checked = true

