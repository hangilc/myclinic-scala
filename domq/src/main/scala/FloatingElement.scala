package dev.fujiwara.domq

import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.Geometry
import scala.language.implicitConversions
import org.scalajs.dom.document

class FloatingElement(content: HTMLElement, onRemove: () => Unit = () => ()):
  val ele: HTMLElement = div(css(style => {
    style.position = "absolute"
    style.margin = "0"
    style.display = "hidden"
  }), cls := "domq-floating-element")(content)

  def insert(): Unit =
    document.body(ele)

  def show(): Unit =
    ele.style.display = ""

  def remove(): Unit = 
    ele.remove()
    onRemove()

  def isShown: Boolean = ele.parentElement != null

  def isWindowLeftOverflow: Boolean =
    val r = ele.getBoundingClientRect()
    r.left < 0

  def isWindowTopOverflow: Boolean =
    val r = ele.getBoundingClientRect()
    r.top < 0

  def isWindowRightOverflow: Boolean =
    val w = Geometry.viewportWidth
    val r = ele.getBoundingClientRect()
    r.left + r.width > w

  def isWindowBottomOverflow: Boolean =
    val h = Geometry.viewportHeight
    val r = ele.getBoundingClientRect()
    r.top + r.height > h

  
