package dev.myclinic.scala.web.appoint

import org.scalajs.dom.raw.Element
import org.scalajs.dom.raw.Event

object Removing:

  val evtRemoving = "x-removing"
  val clsRemoving = evtRemoving
  val selRemoving = "." + clsRemoving

  def broadcastRemoving(topElement: Element): Unit =
    val nodeList = topElement.querySelectorAll(selRemoving)
    for i <- 0 until nodeList.length do
      val event = new Event(evtRemoving)
      val node = nodeList.item(i)
      node.dispatchEvent(event)
  
  def addRemovingListener(e: Element, handler: () => Unit): Unit =
    e.classList.add(clsRemoving)
    e.addEventListener(evtRemoving, (e: Event) => handler())
