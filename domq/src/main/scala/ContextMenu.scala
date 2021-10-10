package dev.fujiwara.domq

import org.scalajs.dom.raw.HTMLElement
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.raw.MouseEvent
import org.scalajs.dom.{document, window}

class ContextMenu:
  val menu: HTMLElement = makeEmptyMenu()
  val screen: HTMLElement = makeScreen()

  def makeEmptyMenu(): HTMLElement = 
    div(css(style => {
        style.position = "absolute"
        style.background = "rgba(255, 255, 255, 1"
        style.border = "1px solid gray"
        style.padding = "10px"
        style.zIndex = "2002"
    }))

  def makeScreen(): HTMLElement =
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

  def show(event: MouseEvent): Unit = 
    val x = event.clientX + window.scrollX
    val y = event.clientY + window.scrollY
    menu(css(style => {
      style.left = s"${x}px"
      style.top = s"${y}.px"
    }))
    document.body(screen(onclick := ((e: MouseEvent) => {
      e.preventDefault
      e.stopPropagation
      remove()
    })), menu)

  def remove(): Unit = 
    menu.remove()
    screen.remove()


object ContextMenu:
  def apply(commands: (String, () => Unit)*): ContextMenu =
    val m = new ContextMenu()
    def makeItem(label: String, f: () => Unit): HTMLElement =
      div(
        a(label, href := "", onclick := (() => {
          m.remove()
          f()
        }))
      )
    val items = commands.map((name, f) => {
      Modifier(e => e.appendChild(makeItem(name, f)))
    })
    m.menu(items: _*)
    m
