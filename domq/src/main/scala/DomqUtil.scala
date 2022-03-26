package dev.fujiwara.domq

import scala.scalajs.js
import scala.math.Ordered
import scala.math.Ordering.Implicits.infixOrderingOps
import org.scalajs.dom.HTMLElement
import scala.scalajs.js.typedarray.ArrayBuffer

object DomqUtil:
  def alert(msg: String): Unit = js.Dynamic.global.alert(msg)

  private var nextIdValue: Int = 1

  def genId(): String =
    val id: String = s"domq-gen-id-${nextIdValue}"
    nextIdValue += 1
    id

  def dataURLtoArrayBuffer(dataURL: String): ArrayBuffer =
    val bs = org.scalajs.dom.window.atob(dataURL.substring(dataURL.indexOf(",") + 1))
    val ab = new ArrayBuffer(bs.size)
    val view = new scala.scalajs.js.typedarray.DataView(ab)
    for i <- 0 until bs.size do 
      view.setUint8(i, bs.codePointAt(i).toShort)
    ab


