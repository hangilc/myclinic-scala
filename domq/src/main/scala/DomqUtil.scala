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

  def insertInOrderDesc[T](
      e: HTMLElement,
      eles: List[HTMLElement],
      extract: HTMLElement => T
  )(using Ordering[T]): Unit =
    val o = extract(e)
    val fOpt = eles.find(ele => extract(ele) < o)
    fOpt match {
      case Some(f) => f.parentElement.insertBefore(e, f)
      case None => eles.headOption.foreach(_.parentElement.appendChild(e))
    }
