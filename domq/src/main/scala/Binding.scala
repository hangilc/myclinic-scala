package dev.fujiwara.domq

import org.scalajs.dom.raw.{Element, HTMLInputElement}
import dev.fujiwara.domq.ElementQ.{given, *}
import dev.fujiwara.domq.Modifiers._
import scala.language.implicitConversions

object Binding:
  case class TextBinding():
    private var ele: Element = null

    def bind(e: Element): Unit =
      ele = e

    def text: String =
      require(ele != null)
      ele.innerText

    def text_=(value: String): Unit =
      require(ele != null)
      ele.innerText = value

  case class InputBinding():
    private var ele: HTMLInputElement = null

    def bind(e: HTMLInputElement): Unit =
      ele = e

    def value: String =
      require(ele != null)
      ele.value

    def value_=(v: String): Unit =
      require(ele != null)
      ele.value = v

    def setValid(valid: Boolean): Boolean =
      if valid then
        ele(cls :- "is-invalid", cls := "is-valid")
      else
        ele(cls :- "is-valid", cls := "is-invalid")
      valid

  case class ElementBinding():
    private var ele: Element = null

    def bind(e: Element): Unit =
      ele = e

    def element: Element =
      require(ele != null)
      ele

  def bindTo(target: Binding.TextBinding) = Modifier(e => {
    target.bind(e)
  })

  def bindTo(target: Binding.InputBinding) = Modifier(e => {
    target.bind(e.asInstanceOf[HTMLInputElement])
  })

  def bindTo(target: Binding.ElementBinding) = Modifier(e => {
    target.bind(e)
  })

