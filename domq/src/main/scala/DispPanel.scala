package dev.fujiwara.domq

import ElementQ.{*, given}
import Html.{*, given}
import Modifiers.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.HTMLElement

case class DispPanel(form: Boolean = false):
  val ele = div(
    cls := "domq-disp-panel"
  )
  if form then ele(cls := "form")

  def add(key: String, value: String): Unit =
    add(span(key), span(value))

  def add(key: String, value: HTMLElement): Unit =
    add(span(key), value)

  def add(key: HTMLElement, value: HTMLElement): Unit =
    ele(
      key(cls := "domq-disp-panel-key"),
      value(cls := "domq-disp-panel-value")
    )

