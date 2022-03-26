package dev.fujiwara.domq

import org.scalajs.dom.Blob
import org.scalajs.dom.HTMLImageElement
import org.scalajs.dom.URL
import org.scalajs.dom.Event
import scala.scalajs.js
import org.scalajs.dom.BlobPropertyBag
import scala.scalajs.js.typedarray.ArrayBuffer

object DataImage:
  def apply(data: ArrayBuffer, mimeType: String): HTMLImageElement =
    val blob = 
      new Blob(
        js.Array(data),
        new BlobPropertyBag {
          override val `type`: js.UndefOr[String] = mimeType
        }
      )
    DataImage(blob)

  def apply(blob: Blob): HTMLImageElement =
    val oURL = URL.createObjectURL(blob)
    val image = org.scalajs.dom.document
      .createElement("img")
      .asInstanceOf[HTMLImageElement]
    image.onload = (e: Event) => {
      URL.revokeObjectURL(oURL)
    }
    image.src = oURL
    image

  def apply(dataURL: String): HTMLImageElement = 
    val image = org.scalajs.dom.document
      .createElement("img")
      .asInstanceOf[HTMLImageElement]
    image.onload = (e: Event) => {
      URL.revokeObjectURL(dataURL)
    }
    image.src = dataURL
    image

