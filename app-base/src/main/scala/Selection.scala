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
      makeItem(label, value)
    }: List[Modifier]): _*
  )

class SelectionItem[T](label: String, value: T):
  val ele: HTMLElement = div(cls := "appbase-selection-item")(
    label,
    onclick := (() => {
      clearSelected()
      e(cls := "appbase-selection-item-selected")
      ()
    })
  )
  e

  private def clearSelected(): Unit =
    ()
