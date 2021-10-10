package dev.fujiwara.domq

import org.scalajs.dom.raw.{Element, HTMLElement}
import org.scalajs.dom.document
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import scala.language.implicitConversions

class Modal(title: String, f: Modal.CloseFunction => HTMLElement):
  val dialog = div(Modal.modalContent)

  def open(): Unit =
    dialog(
      div(css(style => style.width = "*"),
        span(Modal.modalTitle)(title),
        Modal.xCircle(color = "gray")(
          css(style => {
            style.cssFloat = "right"
            style.verticalAlign = "middle"
            style.marginLeft = "2rem"
          }),
          onclick := (onCloseClick _)
        )
      ),
      f(() => close())
    )
    document.body(Modal.modalBackdropInstance, dialog)

  def close(): Unit =
    dialog.remove()
    Modal.modalBackdropInstance.remove()

  def onCloseClick(): Unit =
    close()

object Modal:
  type CloseFunction = (() => Unit)

  val modalBackdrop = Modifier(e => {
    val style = e.style
    style.display = "block"
    style.position = "fixed"
    style.left = "0"
    style.top = "0"
    style.right = "0"
    style.bottom = "0"
    style.backgroundColor = "#5a6268"
    style.opacity = "0.4"
    style.overflowY = "auto"
    style.zIndex = "2001"
  })

  val modalBackdropInstance: HTMLElement = div(modalBackdrop)

  val modalTitle = Modifier(e => {
    val style = e.style
    style.verticalAlign = "middle"
    style.fontSize = "1.2rem"
    style.fontWeight = "bold"
  })

  val modalContent = Modifier(e => {
    val style = e.style
    style.position = "fixed"
    style.top = "20px"
    style.left = "50vw"
    style.transform = "translateX(-50%)"
    style.backgroundColor = "white"
    style.padding = "0.5rem 1.5rem"
    style.opacity = "1.0"
    style.zIndex = "2002"
    style.overflow = "auto"
    style.borderRadius = "0.5rem"
  })

  val modalBody = Modifier(e => {
    val style = e.style
    style.padding = "0.5em 0"
  })

  val modalCommands = Modifier(e => {
    val style = e.style
    e.style.textAlign = "end"
    e(cls := "modal-commands")
  })

  // Based from Heroicons
  // src: https://github.com/tailwindlabs/heroicons
  // <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
  // <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
  // </svg>
  def xCircle(size: String = "1.5rem", color: String = "black"): HTMLElement = {
    val ns = "http://www.w3.org/2000/svg"
    val svg = document.createElementNS(ns, "svg").asInstanceOf[HTMLElement]
    val path = document.createElementNS(ns, "path").asInstanceOf[HTMLElement]
    svg(
      css(style => { style.height = size; style.width = size }),
      attrNS(ns, "viewBox") := "0 0 24 24",
      attr("fill") := "none",
      attr("viewBox") := "0 0 24 24",
      attr("stroke") := color,
      attr("width") := "34",
      attr("height") := "34"
    )(
      path(
        attr("stroke-linecap") := "round",
        attr("stroke-linejoin") := "round",
        attr("stroke-width") := "2",
        attr(
          "d"
        ) := "M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"
      )
    )
    svg
  }
