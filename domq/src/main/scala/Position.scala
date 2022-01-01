package dev.fujiwara.domq

import org.scalajs.dom.{MouseEvent, HTMLElement}
import org.scalajs.dom.{document, window}
import java.util.Random

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

  lazy val rgen = Random()

  def placeAtWindowCenter(
      e: HTMLElement,
      dx: Double = 0,
      dy: Double = 0,
      random: Boolean = true,
      randomDimension: Double = 30
  ): Unit =
    def randomized: Double = (rgen.nextDouble() * 2.0 - 1.0) * randomDimension
    val (wWidth, wHeight) = windowExtension
    val (w, h) = getElementExtension(e)
    val (rx, ry) =
      if random then (randomized, randomized)
      else (0.0, 0.0)
    val x = windowScrollX + wWidth / 2.0 - w / 2.0 + dx + rx
    val y = windowScrollY + wHeight / 2.0 - h / 2.0 + dy + ry
    moveTopLeftTo(e, x, y)
