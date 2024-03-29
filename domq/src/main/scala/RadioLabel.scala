package dev.fujiwara.domq

import Html.{*, given}
import Modifiers.{*, given}
import ElementQ.{*, given}
import org.scalajs.dom.HTMLInputElement
import org.scalajs.dom.HTMLElement
import org.scalajs.dom.Event
import scala.language.implicitConversions

case class RadioLabel[T](name: String, value: T, label: String, wrapper: HTMLElement = span):
  val id: String = GenSym.genSym
  val labelElement = Html.label(innerText := label, attr("for") := id)
  val radioElement = radio(Modifiers.name := name, Modifiers.id := id)
  val ele = wrapper(radioElement, labelElement)

  def checked: Boolean = radioElement.checked
  def check(): Unit = radioElement.checked = true
  def addOnInputListener(listener: RadioLabel[T] => Unit): Unit =
    radioElement(oninput := (_ => listener(this)))

