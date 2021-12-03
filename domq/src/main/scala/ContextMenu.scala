package dev.fujiwara.domq

import org.scalajs.dom.raw.HTMLElement
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.raw.MouseEvent
import org.scalajs.dom.{document, window}
import org.scalajs.dom.raw.ClientRect
import org.scalajs.dom.raw.Event

class ContextMenu(zIndex: Int):
  val menu: HTMLElement = makeEmptyMenu()
  val screen: HTMLElement = makeScreen()

  def makeEmptyMenu(): HTMLElement =
    div(css(style => {
      style.position = "absolute"
      style.background = "rgba(255, 255, 255, 1"
      style.border = "1px solid gray"
      style.padding = "10px"
      style.zIndex = zIndex.toString
      style.visibility = "hidden"
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
      style.zIndex = (zIndex - 1).toString
    }))

  def open(event: MouseEvent): Unit =
    document.body(
      screen(onclick := ((e: MouseEvent) => {
        e.preventDefault
        e.stopPropagation
        close()
      })),
      menu
    )
    val (x, y) = ContextMenu.calcPlacement(
      event.clientX,
      event.clientY,
      menu.getClientRects()(0),
      window.innerWidth,
      window.innerHeight
    )
    val xx = (x + window.scrollX).toInt
    val yy = (y + window.scrollY).toInt
    menu(css(style => {
      style.left = s"${xx}px"
      style.top = s"${yy}.px"
    }))
    menu(css(style => style.visibility = "visible"))

  def close(): Unit =
    menu.remove()
    screen.remove()

object ContextMenu:
  def apply(
      commands: List[(String, () => Unit)],
      zIndex: Int = 2002
  ): ContextMenu =
    val m = new ContextMenu(zIndex)
    def makeItem(label: String, f: () => Unit): HTMLElement =
      div(
        a(
          label,
          href := "",
          onclick := ((e: Event) => {
            e.preventDefault
            m.close()
            f()
          })
        )
      )
    val items = commands.map((name, f) => {
      Modifier(e => e.appendChild(makeItem(name, f)))
    })
    m.menu(items: _*)
    m

  def calcPlacement(
      refX: Double,
      refY: Double,
      menuRect: ClientRect,
      viewWidth: Double,
      viewHeight: Double
  ): (Double, Double) =
    (
      calcPlacementX(refX, menuRect, viewWidth),
      calcPlacementY(refY, menuRect, viewHeight)
    )

  def calcPlacementX(
      refX: Double,
      menuRect: ClientRect,
      viewWidth: Double
  ): Double =
    val right = refX + menuRect.width
    if right <= viewWidth then refX
    else viewWidth - menuRect.width

  def calcPlacementY(
      refY: Double,
      menuRect: ClientRect,
      viewHeight: Double
  ): Double =
    val bottom = refY + menuRect.height
    if bottom <= viewHeight then refY
    else refY - menuRect.height
