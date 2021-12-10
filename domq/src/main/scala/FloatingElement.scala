package dev.fujiwara.domq

import org.scalajs.dom.raw.HTMLElement
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.document

class FloatingElement(content: HTMLElement):
  private val ele: HTMLElement = div(css(style => {
    style.position = "absolute"
  }))(content)

  def left: String = ele.style.left
  def left_=(value: String): Unit = ele.style.left = value
  def left_=(value: DocCoord): Unit = ele.style.left = s"${value.value}px"
  def top: String = ele.style.top
  def top_=(value: String): Unit = ele.style.top = value
  def top_=(value: DocCoord): Unit = ele.style.top = s"${value.value}px"
  def right: String = ele.style.right
  def right_=(value: String): Unit = ele.style.right = value
  def right_=(value: DocCoord): Unit = ele.style.right = s"${value.value}px"
  def bottom: String = ele.style.bottom
  def bottom_=(value: String): Unit = ele.style.bottom = value
  def bottom_=(value: DocCoord): Unit = ele.style.bottom = s"${value.value}px"

  def show(): Unit = document.body(ele)
  def hide(): Unit = ele.remove()
  def isShown: Boolean = ele.parentElement != null
  def toggle(): Unit = if isShown then hide() else show()
  
