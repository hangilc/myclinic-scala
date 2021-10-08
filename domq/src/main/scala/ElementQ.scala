package dev.fujiwara.domq

import org.scalajs.dom.raw.Element
import org.scalajs.dom.raw.MouseEvent
import scala.language.implicitConversions
import org.scalajs.dom.raw.HTMLDocument
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.raw.HTMLInputElement
import scala.concurrent.Future

case class ElementQ(ele: Element):

  def apply(modifiers: Modifier*): ElementQ =
    modifiers.foreach(_.modifier(ele))
    this

  def onclick(handler: MouseEvent => _): Element =
    ele.addEventListener("click", handler)
    ele

  def onclick(handler: () => _): Element =
    onclick((_: MouseEvent) => handler())

  def clear(): Unit =
    ele.innerHTML = ""

  def replaceBy(newElement: Element): Unit =
    ele.parentNode.replaceChild(newElement, ele)

  def remove(): Unit =
    ele.parentNode.removeChild(ele)


object ElementQ {
  
  given Conversion[Element, ElementQ] = ElementQ(_)
  given Conversion[HTMLElement, ElementQ] = ElementQ(_)
  given htmlInputToElementQ: Conversion[HTMLInputElement, ElementQ] = ElementQ(_)

  given Conversion[ElementQ, Element] = _.ele
}