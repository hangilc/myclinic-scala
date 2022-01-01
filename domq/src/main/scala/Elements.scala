package dev.fujiwara.domq

import org.scalajs.dom.{HTMLElement, HTMLSelectElement}
import scala.collection.mutable.ListBuffer
import org.scalajs.dom.HTMLOptionElement
import org.scalajs.dom.HTMLInputElement
import org.scalajs.dom.document
import math.Ordered.orderingToOrdered
import dev.fujiwara.domq.CustomEvent

trait Modifier[-E]:
  def modify(e: E): Unit

object Modifier:
  object cls:
    def :=[E <: HTMLElement](arg: String): Modifier[E] = new Modifier[E]:
      def modify(e: E): Unit = e.className = arg

  private lazy val textAreaWorkarea = document.createElement("textarea")

  def raw[E <: HTMLElement](text: String): Modifier[E] =
    new Modifier[E]:
      def modify(e: E): Unit =
        textAreaWorkarea.innerHTML = text
        val decoded = textAreaWorkarea.innerText
        e.appendChild(document.createTextNode(decoded))
