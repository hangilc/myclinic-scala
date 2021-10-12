package dev.fujiwara.domq

import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.raw.MouseEvent
import scala.language.implicitConversions
import org.scalajs.dom.raw.HTMLDocument
import org.scalajs.dom.raw.HTMLInputElement
import scala.concurrent.Future
import org.scalajs.dom.raw.Node

case class ElementQ(ele: HTMLElement):

  def apply(modifiers: Modifier*): ElementQ =
    modifiers.foreach(_.modifier(ele))
    this

  def onclick(handler: MouseEvent => _): HTMLElement =
    ele.addEventListener("click", handler)
    ele

  def onclick(handler: () => _): HTMLElement =
    onclick((_: MouseEvent) => handler())

  def clear(): Unit =
    ele.innerHTML = ""

  def withParent(f: Node => Unit): Unit =
    val parent = ele.parentNode
    if parent != null then f(parent)

  def replaceBy(newElement: HTMLElement): Unit =
    withParent(p => p.replaceChild(newElement, ele))

  def preInsert(newElement: HTMLElement): Unit =
    withParent(p => p.insertBefore(newElement, ele))

  def remove(): Unit =
    withParent(n => n.removeChild(ele))

object ElementQ {
  
  given Conversion[HTMLElement, ElementQ] = ElementQ(_)
  given htmlInputToElementQ: Conversion[HTMLInputElement, ElementQ] = ElementQ(_)

  given Conversion[ElementQ, HTMLElement] = _.ele
  given Conversion[ElementQ, HTMLInputElement] = _.ele.asInstanceOf[HTMLInputElement]
}