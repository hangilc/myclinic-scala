package dev.fujiwara.domq

import org.scalajs.dom.{HTMLElement, document, window}
import ElementQ.{*, given}
import Html.{*, given}
import Modifiers.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.MouseEvent

object Absolute:
  def positionAbsolute(e: HTMLElement, width: Int, height: Int): HTMLElement =
    e(css(style =>
      style.position = "absolute"
      style.width = s"${width}px"
      style.height = s"${height}px"
    ))
    e

  def leftOf(e: HTMLElement): Double =
    e.getBoundingClientRect().left + Viewport.offsetLeft

  def topOf(e: HTMLElement): Double =
    e.getBoundingClientRect().top + Viewport.offsetTop

  def rightOf(e: HTMLElement): Double =
    leftOf(e) + e.scrollWidth

  def bottomOf(e: HTMLElement): Double =
    topOf(e) + e.scrollHeight

  def setLeftOf(e: HTMLElement, left: Double): Unit =
    e.style.left = s"${left - Viewport.offsetLeft}px"

  def setTopOf(e: HTMLElement, top: Double): Unit =
    e.style.top = s"${top - Viewport.offsetTop}px"

  def setRightOf(e: HTMLElement, right: Double): Unit =
    val left = right - outerWidthOf(e) - marginLeftOf(e) - marginRightOf(e)
    e.style.left = s"${left}px"

  def setBottomOf(e: HTMLElement, bottom: Double): Unit =
    val top = bottom - outerHeightOf(e) - marginTopOf(e) - marginBottomOf(e)
    e.style.top = s"${top}px"

  def outerWidthOf(e: HTMLElement): Int =
    e.getBoundingClientRect().width.toInt

  def outerHeightOf(e: HTMLElement): Int =
    e.getBoundingClientRect().height.toInt

  def marginLeftOf(e: HTMLElement): Int =
    parseInt(window.getComputedStyle(e).marginLeft)

  def marginTopOf(e: HTMLElement): Int =
    parseInt(window.getComputedStyle(e).marginTop)

  def marginRightOf(e: HTMLElement): Int =
    parseInt(window.getComputedStyle(e).marginRight)

  def marginBottomOf(e: HTMLElement): Int =
    parseInt(window.getComputedStyle(e).marginBottom)

  private def parseInt(s: String): Int =
    val pat = raw"^\d+".r
    pat.findFirstIn(s).map(_.toInt).getOrElse(0)

  def ensureHorizInViewOffsetting(e: HTMLElement, extra: Int): Unit =
    val r = e.getBoundingClientRect()
    if r.right > (Viewport.width - extra) then
      val viewRight = Viewport.offsetLeft + Viewport.width
      setRightOf(e, viewRight - extra)
    else if r.left < 0 then
      setLeftOf(e, extra)
      
  def ensureVertInViewOffsetting(e: HTMLElement, extra: Int): Unit =
    val r = e.getBoundingClientRect()
    if r.bottom > (Viewport.height - extra) then
      val viewBottom = Viewport.offsetTop + Viewport.height
      setBottomOf(e, viewBottom - extra)
    else if r.top < 0 then
      setTopOf(e, extra)

  def ensureInViewOffsetting(e: HTMLElement, extra: Int): Unit =
    ensureHorizInViewOffsetting(e, extra)
    ensureVertInViewOffsetting(e, extra)

  def ensureHorizInViewFlipping(e: HTMLElement, pivot: Double): Unit =
    val r = e.getBoundingClientRect()
    if r.right > Viewport.width then
      setRightOf(e, pivot)
    else if r.left < 0 then
      setLeftOf(e, pivot)

  def ensureVertInViewFlipping(e: HTMLElement, pivot: Double): Unit =
    val r = e.getBoundingClientRect()
    if r.bottom > Viewport.height then
      setBottomOf(e, pivot)
    else if r.top < 0 then
      setTopOf(e, pivot)

  def ensureInViewFlipping(e: HTMLElement, xpivot: Double, ypivot: Double): Unit =
    ensureHorizInViewFlipping(e, xpivot)
    ensureVertInViewFlipping(e, ypivot)

  private var dragTarget: HTMLElement = null
  private var dragTargetOffsetX: Int = 0
  private var dragTargetOffsetY: Int = 0

  private val dragEventHandler: scala.scalajs.js.Function1[MouseEvent, Unit] = e =>
    if dragTarget != null then
      dragTarget.style.left = s"${e.clientX + dragTargetOffsetX}px"
      dragTarget.style.top = s"${e.clientY + dragTargetOffsetY}px"

  private val undragEventHandler: scala.scalajs.js.Function1[MouseEvent, Unit] = e =>
    if dragTarget != null then
      dragTarget = null
      document.body.removeEventListener("mousemove", dragEventHandler)
      document.body.removeEventListener("mouseup", undragEventHandler)


  def enableDrag(e: HTMLElement, dragArea: HTMLElement): Unit =
    dragArea.addEventListener("mousedown", (event: MouseEvent) => {
      val style = window.getComputedStyle(e)
      dragTarget = e
      dragTargetOffsetX = parseInt(style.left) - event.clientX.toInt 
      dragTargetOffsetY = parseInt(style.top) - event.clientY.toInt 
      document.body.addEventListener("mousemove", dragEventHandler)
      document.body.addEventListener("mouseup", undragEventHandler)
    })

  def openWithScreen(e: HTMLElement, locator: HTMLElement => Unit): () => Unit =
    val zIndexScreen = ZIndexManager.alloc()
    val zIndexElement = ZIndexManager.alloc()
    val screen = div(cls := "domq-screen", zIndex := zIndexScreen)

    def close(): Unit = 
      e.remove()
      screen.remove()
      ZIndexManager.release(zIndexElement)
      ZIndexManager.release(zIndexScreen)

    e(css(_.visibility = "hidden"), zIndex := zIndexElement)
    document.body(screen(onclick := (close _)))
    document.body(e)
    locator(e)
    e(css(_.visibility = "visible"))
    (close _)




 
