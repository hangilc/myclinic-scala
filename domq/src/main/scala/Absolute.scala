package dev.fujiwara.domq

import org.scalajs.dom.{HTMLElement, document, window}
import ElementQ.{*, given}
import Html.{*, given}
import Modifiers.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.MouseEvent

object Absolute:
  def position(e: HTMLElement): HTMLElement =
    e(css(_.position = "absolute"))

  def leftOf(e: HTMLElement): Double =
    e.getBoundingClientRect().left + window.scrollX

  def topOf(e: HTMLElement): Double =
    e.getBoundingClientRect().top + window.scrollY

  def rightOf(e: HTMLElement): Double =
    e.getBoundingClientRect().right + window.scrollX

  def bottomOf(e: HTMLElement): Double =
    e.getBoundingClientRect().bottom + window.scrollY

  def setLeftOf(e: HTMLElement, left: Double): Unit =
    e.style.left = s"${left}px"

  def setTopOf(e: HTMLElement, top: Double): Unit =
    e.style.top = s"${top}px"

  def setRightOf(e: HTMLElement, right: Double): Unit =
    val marginLeft = marginLeftOf(e)
    val marginRight = marginRightOf(e)
    val r = e.getBoundingClientRect()
    val w = r.width + marginLeft + marginRight
    e.style.left = s"${right - w}px"

  def setBottomOf(e: HTMLElement, bottom: Double): Unit =
    val marginTop = marginTopOf(e)
    val marginBottom = marginBottomOf(e)
    val r = e.getBoundingClientRect()
    val h = r.height + marginTop + marginBottom
    e.style.top = s"${bottom - h}px"

  def marginLeftOf(e: HTMLElement): Double =
    parseDouble(window.getComputedStyle(e).marginLeft)

  def marginTopOf(e: HTMLElement): Double =
    parseDouble(window.getComputedStyle(e).marginTop)

  def marginRightOf(e: HTMLElement): Double =
    parseDouble(window.getComputedStyle(e).marginRight)

  def marginBottomOf(e: HTMLElement): Double =
    parseDouble(window.getComputedStyle(e).marginBottom)

  private def parseDouble(s: String): Double =
    val pat = raw"^\d+(\.\d+)?".r
    pat.findFirstIn(s).map(_.toDouble).getOrElse(0)

  def viewportWidth: Double = document.documentElement.getBoundingClientRect().width
  
  def viewportHeight: Double = document.documentElement.getBoundingClientRect().height

  def viewportExt: (Double, Double) =
    val r = document.documentElement.getBoundingClientRect()
    (r.width, r.height)

  def viewportOffsetLeft: Double = window.scrollX

  def viewportOffsetTop: Double = window.scrollY

  def clickPos(event: MouseEvent): (Double, Double) =
    (event.clientX + viewportOffsetLeft, event.clientY + viewportOffsetTop)

  def ensureHorizInViewOffsetting(e: HTMLElement, extra: Double): Unit =
    val r = e.getBoundingClientRect()
    if r.right > (viewportWidth - extra) then
      setRightOf(e, viewportOffsetLeft + viewportWidth - extra)
    else if r.left < 0 then
      setLeftOf(e, window.scrollX + extra)
      
  def ensureVertInViewOffsetting(e: HTMLElement, extra: Double): Unit =
    val r = e.getBoundingClientRect()
    if r.bottom > (viewportHeight - extra) then
      val viewBottom = viewportOffsetTop + viewportHeight
      setBottomOf(e, viewBottom - extra)
    else if r.top < 0 then
      setTopOf(e, window.scrollY + extra)

  def ensureInViewOffsetting(e: HTMLElement, extra: Double): Unit =
    ensureHorizInViewOffsetting(e, extra)
    ensureVertInViewOffsetting(e, extra)

  def ensureHorizInViewFlipping(e: HTMLElement, pivot: Double): Unit =
    val r = e.getBoundingClientRect()
    if r.right > viewportWidth then
      setRightOf(e, pivot)
    else if r.left < 0 then
      setLeftOf(e, pivot)

  def ensureVertInViewFlipping(e: HTMLElement, pivot: Double): Unit =
    val r = e.getBoundingClientRect()
    if r.bottom > viewportHeight then
      setBottomOf(e, pivot)
    else if r.top < 0 then
      setTopOf(e, pivot)

  def ensureInViewFlipping(e: HTMLElement, xpivot: Double, ypivot: Double): Unit =
    ensureHorizInViewFlipping(e, xpivot)
    ensureVertInViewFlipping(e, ypivot)

  private var dragTarget: HTMLElement = null
  private var dragTargetOffsetX: Double = 0
  private var dragTargetOffsetY: Double = 0

  private val dragEventHandler: scala.scalajs.js.Function1[MouseEvent, Unit] = e =>
    if dragTarget != null then
      val (x, y) = clickPos(e)
      dragTarget.style.left = s"${x + dragTargetOffsetX}px"
      dragTarget.style.top = s"${y + dragTargetOffsetY}px"

  private val undragEventHandler: scala.scalajs.js.Function1[MouseEvent, Unit] = e =>
    if dragTarget != null then
      dragTarget = null
      document.body.removeEventListener("mousemove", dragEventHandler)
      document.body.removeEventListener("mouseup", undragEventHandler)


  def enableDrag(e: HTMLElement, dragArea: HTMLElement): Unit =
    dragArea.addEventListener("mousedown", (event: MouseEvent) => {
      val (x, y) = clickPos(event)
      val style = window.getComputedStyle(e)
      dragTarget = e
      dragTargetOffsetX = parseDouble(style.left) - x
      dragTargetOffsetY = parseDouble(style.top) - y
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




 
