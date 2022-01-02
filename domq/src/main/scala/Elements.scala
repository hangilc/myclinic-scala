package dev.fujiwara.domq

import org.scalajs.dom.{HTMLElement, HTMLSelectElement}
import scala.collection.mutable.ListBuffer
import org.scalajs.dom.HTMLOptionElement
import org.scalajs.dom.HTMLInputElement
import org.scalajs.dom.CSSStyleDeclaration
import org.scalajs.dom.document
import math.Ordered.orderingToOrdered
import dev.fujiwara.domq.CustomEvent

trait Modifier[-E]:
  def modify(e: E): Unit

object Modifier:
  def apply[E](m: E => Unit): Modifier[E] = new Modifier[E]:
    def modify(e: E): Unit = m(e)

  class Assign[E, A]:
    def :=(a: A): Modifier[E]
  object Assign:
    def apply[E, A](m: (E, A) => Unit) = new Assign[E, A]:
      def :=(a: A): Modifier[E] = Modifier[E](e => m(e, a))

  val cls = Assign[HTMLElement, String]((e, a) => e.className = a)
  val cb = Assign[HTMLElement, HTMLElement => Unit](
    (e, handler) => handler(e)
  )

  class Attr(name: String):
    def :=(arg: String) = Modifier[HTMLElement](e => e.setAttribute(name, arg))
    val remove = Modifier[HTMLElement](e => e.removeAttribute(name))
  def attr(name: String) = new Attr(name)

  class AttrNS(namespace: String, name: String):
    def :=(arg: String) = Modifier[HTMLElement](
      e => e.setAttributeNS(namespace, name, arg)
    )
  def attrNS(namespace: String, name: String) = AttrNS(namespace, name)

  val value = Assign[HTMLInputElement, String]((e, v) => e.value = v)

  val id = attr("id")

  def placeholder: Attr = attr("placeholder")

  val disabled = Assign[HTMLInputElement, Boolean](
    (e, b) => e.disabled = b
  )

  val checked = Assign[HTMLInputElement, Boolean](
    (e, b) => e.checked = b
  )

  val name: Attr = attr("name")

  def css(f: CSSStyleDeclaration => Unit) = Modifier[HTMLElement](
    e => f(e.style)
  )

  class AssignCss:
    def :=(arg: String): Modifier[HTMLElement]
  object AssignCss:
    def apply(f: (CSSStyleDeclaration, String) => Unit) = 
      new AssignCss:
        def :=(arg: String) = css(style => f(style, arg))

  val mt = AssignCss((style, value) => style.marginTop = value)
  val mb = AssignCss((style, value) => style.marginBottom = value)
  val ml = AssignCss((style, value) => style.marginLeft = value)
  val mr = AssignCss((style, value) => style.marginRight = value)
  val padding = AssignCss((s, v) => s.padding = v)
  val margin = AssignCss((s, v) => s.margin = v)
  val border = AssignCss((s, v) => s.border = v)
  val color = AssignCss((s, v) => s.color = v)
  val background = AssignCss((s, v) => s.background = v)
  val cursor = AssignCss((s, v) => s.cursor = v)
  val display = AssignCss((style, value) => style.display = value)
  val displayNone = display := "none"
  val displayBlock = display := "block"
  val displayInlineBlock = display := "inline-block"
  val displayDefault = display := ""

  val tst = {
    import dev.fujiwara.domq.ElementQ.*
    val e = document.createElement("svg").asInstanceOf[HTMLElement]
    e(mt := "1.2rem")

  }



  private lazy val textAreaWorkarea = document.createElement("textarea")

  def raw[E <: HTMLElement](text: String): Modifier[E] =
    new Modifier[E]:
      def modify(e: E): Unit =
        textAreaWorkarea.innerHTML = text
        val decoded = textAreaWorkarea.innerText
        e.appendChild(document.createTextNode(decoded))

