package dev.fujiwara.domq

import scala.scalajs.js
import org.scalajs.dom.raw.{Element}
import scalajs.js.annotation.{JSGlobal, JSGlobalScope}
import scala.collection.mutable.ListBuffer

@js.native
@JSGlobalScope
object TippyGlobals extends js.Any:
  def tippy(target: Element, props: js.Object): Tippy = js.native

@js.native
trait Tippy extends js.Object:
  def show(): Unit = js.native
  def hide(): Unit = js.native
  def destroy(): Unit = js.native

class TippyBuilder(target: Element):
  val buf = ListBuffer[(String, js.Any)]()
  def build(): Tippy = 
    TippyGlobals.tippy(target, js.Dynamic.literal(buf.toList: _*))
  private def add(key: String, value: js.Any): TippyBuilder =
    buf.addOne((key, value))
    this
  def arrow(value: Boolean): TippyBuilder = add("arrow", value)
  def content(value: String | Element): TippyBuilder =
    add("content", value match {
      case s: String => s
      case e: Element => e
      case _ => js.undefined
    })
  def theme(value: String): TippyBuilder = add("theme", value)
  def trigger(value: String): TippyBuilder = add("trigger", value)    

