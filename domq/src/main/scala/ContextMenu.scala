package dev.fujiwara.domq

import org.scalajs.dom.raw.HTMLElement
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.raw.MouseEvent
import org.scalajs.dom.{document, window}

object ContextMenu:
  def prepareMenu(menu: HTMLElement): Unit =
    menu(css(style => {
        style.position = "absolute"
        style.background = "rgba(255, 255, 255, 1"
        style.border = "1px solid gray"
        style.padding = "10px"
        style.zIndex = "2002"
    }))

  def show(clickEvent: MouseEvent, menu: HTMLElement): Unit =
    val x = clickEvent.clientX + window.scrollX
    val y = clickEvent.clientY + window.scrollY
    menu(css(style => {
      style.left = s"${x}px"
      style.top = s"${y}.px"
    }))
    val screen = contextMenuScreen
    document.body(screen(onclick := ((e: MouseEvent) => {
      e.preventDefault
      e.stopPropagation
      menu.remove()
      screen.remove()
    })), menu)
    
  def contextMenuScreen: HTMLElement =
    div(css(style => {
      style.display = "block"
      style.position = "fixed"
      style.left = "0"
      style.top = "0"
      style.right = "0"
      style.bottom = "0"
      style.backgroundColor = "#5a6268"
      style.opacity = "0"
      style.overflowY = "auto"
      style.zIndex = "2001"
    }))
