package dev.fujiwara.domq

import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.document
import org.scalajs.dom.raw.{HTMLElement}

case class FloatWindow(title: String, content: HTMLElement, width: String = "200px"):
  val eTitle = div()
  val ele = div(css(style => {
    style.width = width
    style.position = "absolute"
    style.border = "1px solid gray"
    style.padding = "4px"
    style.backgroundColor = "white"
    style.borderRadius = "4px"
  }))(
    eTitle(css(style => {
      style.fontWeight = "bold"
      style.backgroundColor = "#eee"
      style.padding = "2px"
      style.marginBottom = "4px"
    }))(title),
    content
  )

  def show(): Unit =
    val body: HTMLElement = document.body
    body(ele)
  
