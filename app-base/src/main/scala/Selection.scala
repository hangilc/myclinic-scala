package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Modifier}
import org.scalajs.dom.raw.{HTMLElement}
import scala.language.implicitConversions

class Selection[T](
    items: List[(String, T)],
    onSelect: T => Unit = ((_: T) => ())
):
  val ele = div(cls := "appbase-selection")(
    (items.map { case (label, value) =>
      SelectionItem(label, value).ele
    }: List[Modifier]): _*
  )

  private def clearSelected(): Unit =
    val nodes = ele.querySelectorAll(".appbase-selection-item-selected")
    for i <- 0 until nodes.length do
      nodes.item(i).asInstanceOf[HTMLElement].classList.remove("appbase-selection-item-selected")

  class SelectionItem(label: String, value: T):
    val ele: HTMLElement = div(cls := "appbase-selection-item")(
      label,
      onclick := (() => {
        clearSelected()
        addSelected()
        onSelect(value)
        ()
      })
    )

    def addSelected(): Unit =
      ele(cls := "appbase-selection-item-selected")

    

