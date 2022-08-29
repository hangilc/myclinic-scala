package dev.fujiwara.domq

import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.Geometry
import dev.fujiwara.domq.Geometry.{DocCoord, DocRect, DocPoint}
import scala.language.implicitConversions
import org.scalajs.dom.document

class FloatingElement(content: HTMLElement, onHide: () => Unit = () => ()):
  val ele: HTMLElement = div(css(style => {
    style.position = "absolute"
  }), cls := "domq-floating-element")(content)

  def getRect: DocRect = Geometry.getRect(ele)
  def leftTop: DocPoint = getRect.leftTop
  def leftTop_=(p: DocPoint) =
    left = p.x
    top = p.y
  def left: DocCoord = getRect.left
  def left_=(value: DocCoord) = Geometry.setElementLeft(ele, value)
  def top: DocCoord = getRect.top
  def top_=(value: DocCoord) = Geometry.setElementTop(ele, value)
  def right: DocCoord = getRect.right
  def right_=(value: DocCoord) = Geometry.setElementRight(ele, value)
  def bottom: DocCoord = getRect.bottom
  def bottom_=(value: DocCoord) = Geometry.setElementBottom(ele, value)

  def show(): Unit = document.body(ele)
  def hide(): Unit = 
    ele.remove()
    onHide()
  def isShown: Boolean = ele.parentElement != null
  def toggle(): Unit = if isShown then hide() else show()
  
