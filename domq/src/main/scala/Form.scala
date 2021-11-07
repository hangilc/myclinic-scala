package dev.fujiwara.domq

import org.scalajs.dom.raw.HTMLElement
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Html
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.raw.HTMLInputElement

object Form:
  def rows(spec: (HTMLElement, HTMLElement)*): HTMLElement =
    val tab = div(css(style => {
      style.display = "table"
      style.borderCollapse = "separate"
      style.borderSpacing = "0.5rem 0.3rem"
      style.margin = "-0.3rem -0.5rem"
    }))
    spec.foreach { case (key, input) =>
      tab(
        div(css(style => style.display = "table-row"))(
          key(css(style => {
            style.display = "table-cell"
            style.textAlign = "right"
            style.wordBreak = "keep-all"
          }), cls := "form-key"),
          input(css(style => { 
            style.display = "table-cell"
            style.verticalAlign = "middle"
          }), cls := "form-value")
        )
      )
    }
    tab

  def inputGroup: HTMLElement =
    div(css(style => {
      style.display = "flex"
      style.width = "100%"
      style.overflow = "hidden"
      style.setProperty("align-items", "center")
      style.setProperty("align-content", "center")
    }))

  def input: HTMLInputElement =
    Html.inputText(attr("size") := "1", css(style => {
      style.setProperty("flex", "1 1 0")
    }))

  def fixedSizeInput(width: String): HTMLInputElement =
    Html.inputText(Modifiers.width := width, css(style => {
      style.setProperty("flex", "0 0 auto")
    }))
