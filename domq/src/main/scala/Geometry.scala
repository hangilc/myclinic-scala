package dev.fujiwara.domq

import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.{document, window}
import org.scalajs.dom.raw.ClientRect

object Geometry:
  opaque type DocCoord = Double

  extension (c: DocCoord)
    def -(d: DocCoord): Double = c - d
    def +(v: Double): DocCoord = c + v

  case class DocPoint(x: DocCoord, y: DocCoord)

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
    def shiftY(dy: Double): DocRect: shift(0, dy)

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

  def getDocCoordX(ele: HTMLElement, hpos: HPos): DocCoord =
    val r = ele.getClientRects()(0)
    getCornerX(r, hpos)

  def getDocCoordY(ele: HTMLElement, vpos: VPos): DocCoord =
    val r = ele.getClientRects()(0)
    getCornerY(r, vpos)

  def getCornerX(r: ClientRect, hpos: HPos): DocCoord =
    hpos match {
      case HPos.Left  => r.left + window.scrollX
      case HPos.Right => r.right + window.scrollX
    }

  def getCornerY(r: ClientRect, vpos: VPos): DocCoord =
    vpos match {
      case VPos.Top    => r.top + window.scrollY
      case VPos.Bottom => r.bottom + window.scrollY
    }

  def getCorner(ele: HTMLElement, hpos: HPos, vpos: VPos): DocPoint =
    val r = ele.getClientRects()(0)
