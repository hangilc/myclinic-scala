package dev.fujiwara.domq

import org.scalajs.dom.raw.HTMLElement
import scala.collection.mutable.ListBuffer
import org.scalajs.dom.raw.Node

case class LocalModal(wrapper: HTMLElement, content: HTMLElement):
  val saved: List[Node] = removeChildren()
  wrapper.appendChild(content)

  def close(): Unit =
    wrapper.innerHTML = ""
    saved.foreach(n => wrapper.appendChild(n))

  def removeChildren(): List[Node] = 
    val buf = ListBuffer[Node]()
    while wrapper.children.length > 0 do
      val e = wrapper.firstChild
      wrapper.removeChild(e)
      buf += e
    buf.toList

object LocalModal:
  def open(wrapper: HTMLElement, content: HTMLElement): LocalModal =
    LocalModal(wrapper, content)


