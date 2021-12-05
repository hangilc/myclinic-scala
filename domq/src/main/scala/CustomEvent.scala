package dev.fujiwara.domq

import scala.scalajs.js
import scala.scalajs.js.annotation._
import org.scalajs.dom.raw.Event
import org.scalajs.dom.raw.EventInit

@js.native
@JSGlobal
class CustomEvent[T](typeArg: String, init: js.UndefOr[CustomEventInit[T]] = js.undefined)
    extends Event(typeArg, init) {

  def detail: T = js.native
}

class CustomEventInit[T](val detail: js.UndefOr[T] = js.undefined) extends EventInit 

object CustomEventInit:
  def apply[T](detailValue: T, bubbles: Boolean = false): CustomEventInit[T] = 
    val init = new CustomEventInit[T](detailValue)
    init.bubbles = bubbles
    init
