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

  // def getRect: DocRect = Geometry.getRect(ele)
  // def leftTop: DocPoint = getRect.leftTop
  // def leftTop_=(p: DocPoint) =
  //   left = p.x
  //   top = p.y
  // def left: DocCoord = getRect.left
  // def left_=(value: DocCoord) = Geometry.setElementLeft(ele, value)
  // def top: DocCoord = getRect.top
  // def top_=(value: DocCoord) = Geometry.setElementTop(ele, value)
  // def right: DocCoord = getRect.right
  // def right_=(value: DocCoord) = Geometry.setElementRight(ele, value)
  // def bottom: DocCoord = getRect.bottom
  // def bottom_=(value: DocCoord) = Geometry.setElementBottom(ele, value)

  def insert(): Unit =
    document.body(ele)

  def show(): Unit =
    ele.style.display = ""

  def remove(): Unit = 
    ele.remove()
    onRemove()

  def isShown: Boolean = ele.parentElement != null
  
