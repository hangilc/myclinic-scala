package dev.fujiwara.domq

import scala.scalajs.js
import scala.scalajs.js.annotation._
import org.scalajs.dom.raw.Event
import org.scalajs.dom.raw.EventInit

@js.native
@JSGlobal
class CustomEvent[T](typeArg: String, init: js.UndefOr[CustomEventInit[T]])
    extends Event(typeArg, init) {

  def detail: T = js.native
}

class CustomEventInit[T] extends EventInit {
  var detail: js.UndefOr[T] = js.undefined
}

object CustomEventInit:
  def apply[T](detailValue: T): CustomEventInit[T] = 
    val init = new CustomEventInit[T]()
    init.detail = detailValue
    init
