package dev.fujiwara.domq

import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.{document, window}
//import org.scalajs.dom.ClientRect
import org.scalajs.dom.DOMRect
import org.scalajs.dom.Event

class ContextMenu:
  val zIndexScreen = ZIndexManager.alloc()
  val zIndexMenu = ZIndexManager.alloc()
  val menu: HTMLElement = makeEmptyMenu()
  val screen: HTMLElement = makeScreen()

  def makeEmptyMenu(): HTMLElement =
    div(css(style => {
      style.position = "absolute"
      style.background = "rgba(255, 255, 255, 1)"
      style.border = "1px solid gray"
      style.padding = "10px"
      style.zIndex = zIndexMenu.toString
      style.visibility = "hidden"
    }), cls := "domq-context-menu")

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
      style.zIndex = zIndexScreen.toString
    }))

  def calcPlacement(
      clickX: Double,
      clickY: Double,
      rect: DOMRect,
      //rect: ClientRect,
      windowWidth: Double,
      windowHeight: Double
  ): (Double, Double) = ContextMenu.calcPlacement(
    clickX,
    clickY,
    rect,
    windowWidth,
    windowHeight
  )

  def open(event: MouseEvent, className: Option[String] = None): Unit =
    document.body(
      screen(onclick := ((e: MouseEvent) => {
        e.preventDefault()
        e.stopPropagation()
        close()
      })),
      menu(cls := className)
    )
    val (x, y) = calcPlacement(
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
    ZIndexManager.release(zIndexMenu)
    ZIndexManager.release(zIndexScreen)

object ContextMenu:
  def apply(
      commands: List[(String, () => Unit)]
  ): ContextMenu =
    val m = new ContextMenu
    def makeItem(label: String, f: () => Unit): HTMLElement =
      div(
        a(
          label,
          href := "",
          onclick := ((e: Event) => {
            e.preventDefault()
            m.close()
            f()
          })
        )
      )
    val items = commands.map((name, f) => {
      Modifier[HTMLElement](e => e.appendChild(makeItem(name, f)))
    })
    m.menu(items: _*)
    m

  def calcPlacement(
      refX: Double,
      refY: Double,
      menuRect: DOMRect,
      //menuRect: ClientRect,
      viewWidth: Double,
      viewHeight: Double
  ): (Double, Double) =
    (
      calcPlacementX(refX, menuRect, viewWidth),
      calcPlacementY(refY, menuRect, viewHeight)
    )

  def calcPlacementX(
      refX: Double,
      menuRect: DOMRect,
      // menuRect: ClientRect,
      viewWidth: Double
  ): Double =
    val right = refX + menuRect.width
    if right <= viewWidth then refX
    else viewWidth - menuRect.width

  def calcPlacementY(
      refY: Double,
      menuRect: DOMRect,
      // menuRect: ClientRect,
      viewHeight: Double
  ): Double =
    val bottom = refY + menuRect.height
    if bottom <= viewHeight then refY
    else refY - menuRect.height
