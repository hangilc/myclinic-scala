package dev.fujiwara.domq

import scala.scalajs.js
import scala.scalajs.js.annotation._
import org.scalajs.dom.Event
import org.scalajs.dom.EventInit
import org.scalajs.dom.HTMLElement

@js.native
@JSGlobal
class CustomEvent[T](typeArg: String, init: js.UndefOr[CustomEventInit[T]] = js.undefined)
    extends Event(typeArg, init) {

  def detail: T = js.native
}

object CustomEvent:
  def apply[T](typeArg: String, detail: T, bubbles: Boolean = false): CustomEvent[T] =
    new CustomEvent(typeArg, CustomEventInit(detail, bubbles))

  def dispatchTo[T](typeArg: String, detail: T, eles: List[HTMLElement]): Unit =
    val evt = CustomEvent(typeArg, detail)
    eles.foreach(_.dispatchEvent(evt))

class CustomEventInit[T](val detail: js.UndefOr[T] = js.undefined) extends EventInit 

object CustomEventInit:
  def apply[T](detailValue: T, bubbles: Boolean = false): CustomEventInit[T] = 
    val init = new CustomEventInit[T](detailValue)
    init.bubbles = bubbles
    init

def dispatchTo[T](targets: List[HTMLElement], eventType: String, detail: T): Unit =
  import dev.fujiwara.domq.{CustomEvent, CustomEventInit}
  val evt: CustomEvent[T] = CustomEvent(eventType, detail, false)
  targets
    .foreach(e => {
      e.dispatchEvent(evt)
    })
