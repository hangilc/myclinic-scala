package dev.fujiwara.domq

import org.scalajs.dom.{HTMLElement, document, window}
import ElementQ.{*, given}
import Html.{*, given}
import Modifiers.{*, given}
import scala.language.implicitConversions

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

  // private def offsetParentsOf(e: HTMLElement, maxDepth: Int): List[HTMLElement] =
  //   def iter(
  //       c: HTMLElement,
  //       depth: Int,
  //       cur: List[HTMLElement]
  //   ): List[HTMLElement] =
  //     if depth <= 0 then cur
  //     else
  //       val p = c.offsetParent
  //       if p != null && c.isInstanceOf[HTMLElement] then
  //         val cp = p.asInstanceOf[HTMLElement]
  //         iter(cp, depth - 1, cur :+ cp)
  //       else cur
  //   iter(e, maxDepth, List.empty)

  def setLeftOf(e: HTMLElement, left: Double): Unit =
    e.style.left = s"${left}px"

  def setTopOf(e: HTMLElement, top: Double): Unit =
    e.style.top = s"${top}px"

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
