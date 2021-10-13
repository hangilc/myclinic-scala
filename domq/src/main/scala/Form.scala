package dev.fujiwara.domq

import org.scalajs.dom.raw.HTMLElement
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions

object Form:
  def rows(spec: (HTMLElement, HTMLElement)*): HTMLElement =
    val tab = div(css(style => {
      style.display = "table"
      style.borderCollapse = "separate"
      style.borderSpacing = "0.5rem 0"
      style.margin = "0 -0.5rem"
    }))
    spec.foreach { case (key, input) =>
      tab(
        div(css(style => style.display = "table-row"))(
          key(css(style => style.display = "table-cell")),
          input(css(style => style.display = "table-cell"))
        )
      )
    }
    tab
