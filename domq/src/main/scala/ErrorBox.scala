package dev.fujiwara.domq

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons}
import scala.language.implicitConversions

case class ErrorBox():
  val eMessage = div()
  val ele = div(css(style => {
    style.color = "red"
    style.padding = "1rem"
    style.display = "none"
  }))(
    eMessage,
    div(
      Icons.xCircle(size = "1.2rem", color = "red")(
        css(style => style.cssFloat = "right"),
        onclick := (hide _)
      )
    )
  )

  def show(msg: String): Unit =
    eMessage.innerText = msg
    ele(displayBlock)

  def hide(): Unit =
    eMessage.innerHTML = ""
    ele(displayNone)
