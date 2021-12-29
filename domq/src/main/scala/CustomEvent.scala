package dev.fujiwara.domq

import scala.scalajs.js
import scala.scalajs.js.annotation._
import org.scalajs.dom.raw.Event
import org.scalajs.dom.raw.EventInit
import org.scalajs.dom.raw.HTMLElement

@js.native
@JSGlobal
class CustomEvent[T](typeArg: String, init: js.UndefOr[CustomEventInit[T]] = js.undefined)
    extends Event(typeArg, init) {

  def detail: T = js.native
}

object CustomEvent:
  def apply[T](typeArg: String, detail: T, bubbles: Boolean = false): CustomEvent[T] =
    new CustomEvent(typeArg, CustomEventInit(detail, bubbles))

class CustomEventInit[T](val detail: js.UndefOr[T] = js.undefined) extends EventInit 

object CustomEventInit:
  def apply[T](detailValue: T, bubbles: Boolean = false): CustomEventInit[T] = 
    val init = new CustomEventInit[T](detailValue)
    init.bubbles = bubbles
    init

  def dispatch[T](targets: List[HTMLElement], eventType: String, detail: T): Unit =
    import dev.fujiwara.domq.{CustomEvent, CustomEventInit}
    val evt: CustomEvent[T] = CustomEvent(eventType, detail, false)
    targets
      .foreach(e => {
        e.dispatchEvent(evt)
      })
