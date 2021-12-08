package dev.fujiwara.domq

import scala.scalajs.js
import scala.math.Ordered
import scala.math.Ordering.Implicits.infixOrderingOps
import org.scalajs.dom.raw.HTMLElement

object DomqUtil:
  def alert(msg: String): Unit = js.Dynamic.global.alert(msg)

  private var nextIdValue: Int = 1

  def genId(): String =
    val id: String = s"domq-gen-id-${nextIdValue}"
    nextIdValue += 1
    id

