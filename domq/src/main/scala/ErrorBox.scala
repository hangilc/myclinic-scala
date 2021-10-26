package dev.fujiwara.domq

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions

case class ErrorBox():
  val ele = div(css(style => {
    style.color = "red"
    style.padding = "1rem"
    style.display = "none"
  }))

  def show(msg: String): Unit =
    ele.innerHTML = ""
    ele(msg)
    ele(displayBlock)
  
  def hide(): Unit =
    ele.innerHTML = ""
    ele(displayNone)