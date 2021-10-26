package dev.fujiwara.domq

import org.scalajs.dom.raw.HTMLElement
import scala.collection.mutable.ListBuffer
import org.scalajs.dom.raw.Node

class LocalModal(wrapper: HTMLElement, content: HTMLElement):
  var saved: List[Node] = List.empty

  def open(): Unit = 
    saved = removeChildren()
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



