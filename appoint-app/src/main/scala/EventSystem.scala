package dev.myclinic.scala.web.appoint

import dev.myclinic.scala.web.appoint.Events.ModelEvent
import org.scalajs.dom.raw.Element
import org.scalajs.dom.raw.Event

object EventSystem:

  class JsModelEvent(val modelEvent: ModelEvent) extends Event("model-event")

  def addListener(listener: ModelEvent => Unit, ele: Element): Unit =
    ele.addEventListener("model-event", (e: JsModelEvent) => listener(e.modelEvent))
    ele.classList.add("x-model-event-listener")

  def dispatch(modelEvent: ModelEvent, topElement: Element): Unit =
    val nodeList = topElement.querySelectorAll(".x-model-event-listener")
    for
      i <- 0 until nodeList.length
    do
      val node = nodeList.item(i)
      val event: JsModelEvent = new JsModelEvent(modelEvent)
      node.dispatchEvent(event)