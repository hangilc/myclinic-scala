package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.all.*
import org.scalajs.dom.document

object EventDispatcher:
  def dispatchToElement[T](selector: String, eventType: String, event: T): Unit =
    val evt: CustomEvent[T] = CustomEvent(eventType, event, false)
    document.body
      .qSelectorAll(selector)
      .foreach(e => {
        e.dispatchEvent(evt)
      })
