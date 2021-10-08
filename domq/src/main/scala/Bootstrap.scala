package dev.fujiwara.domq

import dev.fujiwara.domq.Modifiers.Creator
import scalajs.js
import scalajs.js.annotation.JSGlobal
import org.scalajs.dom.raw.Element

object Bootstrap:
  val ml = Creator[Int]((e, a) => {
    e.classList.add(s"ms-$a")
  })

  val mr = Creator[Int]((e, a) => {
    e.classList.add(s"me-$a")
  })

  val mt = Creator[Int]((e, a) => {
    e.classList.add(s"mt-$a")
  })

  val mb = Creator[Int]((e, a) => {
    e.classList.add(s"mb-$a")
  })

  @js.native
  @JSGlobal("bootstrap.Modal")
  class Modal(val ele: Element) extends js.Object:
    def toggle(): Unit = js.native
    def show(): Unit = js.native
    def hide(): Unit = js.native
    def dispose(): Unit = js.native


