package dev.fujiwara.domq.rev

import org.scalajs.dom.{HTMLElement, HTMLSelectElement}
import scala.collection.mutable.ListBuffer
import org.scalajs.dom.HTMLOptionElement
import org.scalajs.dom.HTMLInputElement
import math.Ordered.orderingToOrdered
import dev.fujiwara.domq.CustomEvent

trait ElementModifier[+E]:
  def modify[E2 >: E <: HTMLElement](e: E2): Unit

object ElementModifier:
  object cls:
    def :=[E](arg: String): ElementModifier[E] = new ElementModifier[E]:
      def modify[E2 >: E <: HTMLElement](e: E2): Unit = e.className = arg

