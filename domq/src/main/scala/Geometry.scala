package dev.fujiwara.domq

import org.scalajs.dom.HTMLElement
import org.scalajs.dom.{document, window}
import org.scalajs.dom.ClientRect

object Geometry:
  def viewportWidth: Double = document.documentElement.clientWidth
  def viewportHeight: Double = document.documentElement.clientHeight

  def viewportWidthIncludingScrollBar: Double = window.innerWidth
  def viewportHeightIncludingScrollBar: Double = window.innerHeight

  case class Rect(left: Double, top: Double, width: Double, height: Double)

  def rectInViewport(ele: HTMLElement): Rect =
    val r = ele.getBoundingClientRect()
    Rect(r.left, r.top, r.width, r.height)

  def windowScrollX: Double =
    window.scrollX

  def windowScrollY: Double =
    window.scrollY

