package dev.fujiwara.domq

import dev.fujiwara.domq.all.{*, given}
import org.scalajs.dom.HTMLInputElement

case class RadioLabel[T](name: String, value: T, label: String):
  val labelElement = span(Modifiers.innerText := label, Modifiers.name := "")
  val radioElement: HTMLInputElement = radio

