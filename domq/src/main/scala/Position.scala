package dev.fujiwara.domq

import org.scalajs.dom.raw.{MouseEvent, HTMLElement}
import org.scalajs.dom.{document, window}

object Position:
  def getClickPosition(event: MouseEvent): (Double, Double) =
    (
      event.clientX + window.scrollX,
      event.clientY + window.scrollY
    )

  def windowExtension: (Double, Double) =
    (
      window.innerWidth,
      window.innerHeight
    )

  def windowWidthWithoutScrollbar: Double =
    document.documentElement.clientWidth

  def windowHeightWithoutScrollbar: Double =
    document.documentElement.clientHeight - window.scrollY

  def windowScrollX: Double = window.scrollX
  def windowScrollY: Double = window.scrollY

  def moveTopLeftTo(e: HTMLElement, left: Double, top: Double): Unit =
    e.style.left = s"${left}px"
    e.style.top = s"${top}px"

  def getElementTopLeft(e: HTMLElement): (Double, Double) =
    val r = e.getClientRects()(0)
    (r.left + window.scrollX, r.top + window.scrollY)

  def getElementExtension(e: HTMLElement): (Double, Double) =
    val r = e.getClientRects()(0)
    (r.width, r.height)

