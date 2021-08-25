package dev.fujiwara.domq

import org.scalajs.dom.raw.{Element, HTMLInputElement}
import dev.fujiwara.domq.ElementQ._
import dev.fujiwara.domq.Modifiers._

object Binding {
  case class TextBinding() {
    var ele: Element = null

    def bind(e: Element): Unit = {
      ele = e
    }

    def text: String = {
      require(ele != null)
      ele.innerText
    }

    def text_=(value: String): Unit = {
      require(ele != null)
      ele.innerText = value
    }
  }

  case class InputBinding() {
    var ele: HTMLInputElement = null

    def bind(e: HTMLInputElement): Unit = {
      ele = e
    }

    def value: String = {
      require(ele != null)
      ele.value
    }

    def value_=(v: String): Unit = {
      require(ele != null)
      ele.value = v
    }

    def setValid(valid: Boolean): Boolean = {
      if (valid) {
        ele(cls :- "is-invalid", cls := "is-valid")
      } else {
        ele(cls :- "is-valid", cls := "is-invalid")
      }
      valid
    }
  }

  def bindTo(target: Binding.TextBinding) = Modifier(e => {
    target.bind(e)
  })

  def bindTo(target: Binding.InputBinding) = Modifier(e => {
    target.bind(e.asInstanceOf[HTMLInputElement])
  })

}
