package dev.fujiwara.domq

import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.{document, window}

opaque type DocCoord = Double

object DocCoord:
    def apply(value: Double): DocCoord = value

    extension (c: DocCoord)
        def value: Double = c

object Geometry:
    enum HPos:
        case Left, Right

    enum VPos:
        case Top, Bottom

    def getDocCoordX(ele: HTMLElement, hpos: HPos): DocCoord =
        val r = ele.getClientRects()(0)
        hpos match {
            case HPos.Left => DocCoord(r.left + window.scrollX)
            case HPos.Right => DocCoord(r.right + window.scrollX)
        }

    def getDocCoordY(ele: HTMLElement, vpos: VPos): DocCoord =
        val r = ele.getClientRects()(0)
        vpos match {
            case VPos.Top => DocCoord(r.top + window.scrollY)
            case VPos.Bottom => DocCoord(r.bottom + window.scrollY)
        }
