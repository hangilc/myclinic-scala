package dev.fujiwara.domq

import org.scalajs.dom.raw.HTMLElement
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions

class FloatingElement(content: HTMLElement):
  val ele: HTMLElement = div(css(style => {
    style.position = "absolute"
  }))(content)

  def left: String = ele.style.left
  def left_=(value: String): Unit = ele.style.left = value
  def top: String = ele.style.top
  def top_=(value: String): Unit = ele.style.top = value
  def right: String = ele.style.right
  def right_=(value: String): Unit = ele.style.right = value
  def bottom: String = ele.style.bottom
  def bottom_=(value: String): Unit = ele.style.bottom = value

  
