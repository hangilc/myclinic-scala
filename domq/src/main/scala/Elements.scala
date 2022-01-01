package dev.fujiwara.domq

import org.scalajs.dom.{HTMLElement, HTMLSelectElement}
import scala.collection.mutable.ListBuffer
import org.scalajs.dom.HTMLOptionElement
import org.scalajs.dom.HTMLInputElement
import org.scalajs.dom.document
import math.Ordered.orderingToOrdered
import dev.fujiwara.domq.CustomEvent

trait Modifier[+E]:
  def modify[E2 >: E <: HTMLElement](e: E2): Unit

object Modifier:
  private def assignModifier[E] 
  object cls:
    def :=[E](arg: String): Modifier[E] = new Modifier[E]:
      def modify[E2 >: E <: HTMLElement](e: E2): Unit = e.className = arg

  private lazy val textAreaWorkarea = document.createElement("textarea")

  def raw[E](text: String): Modifier[E] =
    new Modifier[E]:
      def modify[E2 >: E <: HTMLElement](e: E2): Unit =
        textAreaWorkarea.innerHTML = text
        val decoded = textAreaWorkarea.innerText
        e.appendChild(document.createTextNode(decoded))
