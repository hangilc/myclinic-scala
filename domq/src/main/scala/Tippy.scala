package dev.fujiwara.domq

import scala.scalajs.js
import org.scalajs.dom.raw.{Element}
import scalajs.js.annotation.{JSGlobal, JSGlobalScope}

@js.native
@JSGlobal
class Tippy extends js.Any:
  def show(): Unit = js.native
  def hide(): Unit = js.native
  def destroy(): Unit = js.native

@js.native
@JSGlobalScope
object TippyGlobals extends js.Any:
  def tippy(target: Element, custom: js.Dynamic): Tippy = js.native

object Tippy:
  def apply(target: Element, text: String): Tippy =
    val custom = js.Dynamic.literal("content" -> text)
    TippyGlobals.tippy(target, custom)