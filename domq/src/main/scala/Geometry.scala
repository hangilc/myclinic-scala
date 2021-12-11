package dev.fujiwara.domq

import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.{document, window}
import org.scalajs.dom.raw.ClientRect

object Geometry:
  opaque type DocCoord = Double

  extension (c: DocCoord)
    def -(d: DocCoord): Double = c - d
    def +(v: Double): DocCoord = c + v

  case class DocPoint(x: DocCoord, y: DocCoord):
    def shift(dx: Double, dy: Double): DocPoint =
      DocPoint(x + dx, y + dy)
    def shiftX(dx: Double): DocPoint = shift(dx, 0)
    def shiftY(dy: Double): DocPoint = shift(0, dy)

  case class DocRect(
      left: DocCoord,
      top: DocCoord,
      right: DocCoord,
      bottom: DocCoord
  ):
    def width = right - left
    def height = bottom - top
    def shift(dx: Double, dy: Double): DocRect =
      DocRect(left + dx, top + dy, right + dx, bottom + dy)
    def shiftX(dx: Double): DocRect = shift(dx, 0)
    def shiftY(dy: Double): DocRect = shift(0, dy)
    def leftTop: DocPoint = DocPoint(left, top)
    def rightTop: DocPoint = DocPoint(right, top)
    def leftBottom: DocPoint = DocPoint(left, bottom)
    def rightBottom: DocPoint = DocPoint(right, bottom)

  object DocRect:
    def fromElement(ele: HTMLElement): DocRect =
      val scrollX = window.scrollX
      val scrollY = window.scrollY
      val r = ele.getClientRects()(0)
      DocRect(
        r.left + scrollX,
        r.top + scrollY,
        r.right + scrollX,
        r.bottom + scrollY
      )

  enum HPos:
    case Left, Right

  enum VPos:
    case Top, Bottom

  def getRect(ele: HTMLElement): DocRect =
    DocRect.fromElement(ele)
