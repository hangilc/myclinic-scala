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

class CustomEventConnect[T](eventType: String):
  def trigger(e: HTMLElement, detail: T, bubbles: Boolean = true): Unit =
    e.dispatchEvent(CustomEvent[T](eventType, detail, bubbles))

  def handle(e: HTMLElement, handler: T => Unit): Unit =
    e.addEventListener(eventType, (e: CustomEvent[T]) => handler(e.detail))

  def listen(e: HTMLElement, handler: js.Function1[CustomEvent[T], Unit]): Unit =
    e.addEventListener(eventType, handler)

  def unlisten(e: HTMLElement, handler: js.Function1[T, Unit]): Unit =
    e.removeEventListener(eventType, handler)

