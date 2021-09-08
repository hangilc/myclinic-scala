package dev.fujiwara.domq

import org.scalajs.dom.raw.Element
import org.scalajs.dom.raw.MouseEvent

case class ElementQ(ele: Element) {

  def apply(modifiers: Modifier*): ElementQ = {
    modifiers.foreach(_.modifier(ele))
    this
  }

  def onclick(handler: MouseEvent => Unit): Element = {
    ele.addEventListener("click", handler)
    ele
  }

  def onclick(handler: () => Unit): Element = {
    onclick((_: MouseEvent) => handler())
  }

  def clear(): Unit = {
    ele.innerHTML = ""
  }

}

object ElementQ {
  implicit def toElementQ(e: Element) = ElementQ(e)

  implicit def toElement(eq: ElementQ) = eq.ele
}