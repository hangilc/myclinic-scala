package dev.fujiwara.domq

import org.scalajs.dom.{HTMLElement, HTMLSelectElement}
import scala.collection.mutable.ListBuffer
import org.scalajs.dom.HTMLOptionElement
import org.scalajs.dom.HTMLInputElement
import org.scalajs.dom.CSSStyleDeclaration
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.document
import math.Ordered.orderingToOrdered
import dev.fujiwara.domq.CustomEvent
import dev.fujiwara.domq.ElementQ.*
import scala.scalajs.js
import org.scalajs.dom.HTMLFormElement
import org.scalajs.dom.Event
import org.scalajs.dom.HTMLAnchorElement
import org.scalajs.dom.KeyboardEvent
import org.scalajs.dom.HTMLButtonElement
import org.scalajs.dom.SVGElement
import org.scalajs.dom.HTMLTextAreaElement
import org.scalajs.dom.Node

trait Modifier[-E]:
  def modify(e: E): Unit

object Modifier:
  def apply[E](m: E => Unit): Modifier[E] = new Modifier[E]:
    def modify(e: E): Unit = m(e)

object Modifiers:
  given Conversion[String, Modifier[HTMLElement]] with
    def apply(s: String): Modifier[HTMLElement] =
      Modifier[HTMLElement](e => {
        val t = document.createTextNode(s)
        e.appendChild(t)
      })
  given Conversion[Node, Modifier[HTMLElement]] with
    def apply(e: Node): Modifier[HTMLElement] =
      Modifier[Node](ele => ele.appendChild(e))
  given Conversion[SVGElement, Modifier[HTMLElement]] with
    def apply(e: SVGElement): Modifier[HTMLElement] =
      Modifier[HTMLElement](ele => ele.appendChild(e))
  given Conversion[Option[HTMLElement], Modifier[HTMLElement]] with
    def apply(opt: Option[HTMLElement]): Modifier[HTMLElement] =
      Modifier[HTMLElement](ele => 
        opt match {
          case Some(e) => ele.appendChild(e)
          case None => ()
        }  
      )
  given Conversion[List[HTMLElement], Modifier[HTMLElement]] with
    def apply(eles: List[HTMLElement]): Modifier[HTMLElement] =
      Modifier[HTMLElement](ele => eles.foreach(e => ele.appendChild(e)))

  abstract class Assign[E, A]:
    def :=(a: A): Modifier[E]
  object Assign:
    def apply[E, A](m: (E, A) => Unit) = new Assign[E, A]:
      def :=(a: A): Modifier[E] = Modifier[E](e => m(e, a))

  object cls:
    def :=(s: String): Modifier[HTMLElement] = (
        e =>
          if !s.isEmpty then
            s.trim.split("\\s+").foreach(c => e.classList.add(c))
    )
    def :=(opt: Option[String]): Modifier[HTMLElement] = (e => {
      opt match {
        case Some(s) => :=(s).modify(e)
        case None    => ()
      }
    })
    def :-(a: String): Modifier[HTMLElement] = (
        e =>
          if !a.isEmpty then
            a.trim.split("\\s+").foreach(c => e.classList.remove(c))
    )

  val clear: Modifier[HTMLElement] = (e => e.innerHTML = "")

  val children = Assign[HTMLElement, List[Node]]((e, list) => {
    list.foreach(e.appendChild(_))
  })

  val cb = Assign[HTMLElement, HTMLElement => Unit]((e, handler) => handler(e))

  class Attr(name: String):
    def :=(arg: String) = Modifier[HTMLElement](e => e.setAttribute(name, arg))
    val remove = Modifier[HTMLElement](e => e.removeAttribute(name))
  def attr(name: String) = new Attr(name)

  class AttrNS(namespace: String, name: String):
    def :=(arg: String) =
      Modifier[HTMLElement](e => e.setAttributeNS(namespace, name, arg))
  def attrNS(namespace: String, name: String) = AttrNS(namespace, name)

  object value:
    def :=(
        arg: String
    ): Modifier[HTMLInputElement | HTMLOptionElement | HTMLTextAreaElement] =
      Modifier[HTMLInputElement | HTMLOptionElement | HTMLTextAreaElement](
        (e: HTMLInputElement | HTMLOptionElement | HTMLTextAreaElement) =>
          e match {
            case i: HTMLInputElement  => i.value = arg
            case o: HTMLOptionElement => o.value = arg
            case t: HTMLTextAreaElement => t.value = arg
          }
      )

  val id = attr("id")

  def placeholder: Attr = attr("placeholder")

  object disabled:
    def :=(arg: Boolean): Modifier[HTMLInputElement | HTMLButtonElement] =
      Modifier[HTMLInputElement | HTMLButtonElement](
        (e: HTMLInputElement | HTMLButtonElement) =>
          e match {
            case i: HTMLInputElement  => i.disabled = arg
            case b: HTMLButtonElement => b.disabled = arg
          }
      )

  object enabled:
    def :=(arg: Boolean): Modifier[HTMLInputElement | HTMLButtonElement] =
      disabled := !arg

  val checked = Assign[HTMLInputElement, Boolean]((e, b) => e.checked = b)

  val name: Attr = attr("name")

  def css(f: CSSStyleDeclaration => Unit) =
    Modifier[HTMLElement](e => f(e.style))

  abstract class AssignCss:
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
  val justifyContent =
    AssignCss((style, value) => style.setProperty("justify-content", value))
  val alignItems =
    AssignCss((style, value) => style.setProperty("align-items", value))
  val maxHeight = AssignCss((s, v) => s.maxHeight = v)
  val minHeight = AssignCss((s, v) => s.minHeight = v)
  val maxWidth = AssignCss((s, v) => s.maxWidth = v)
  val minWidth = AssignCss((s, v) => s.minWidth = v)
  val width = AssignCss((s, v) => if !v.isEmpty then s.width = v)
  val height = AssignCss((s, v) => s.height = v)
  val overflowX = AssignCss((s, v) => s.overflowX = v)
  val overflowY = AssignCss((s, v) => s.overflowY = v)
  val overflowYAuto = overflowY := "auto"
  val cssFloat = AssignCss((s, v) => s.cssFloat = v)
  val floatRight = cssFloat := "right"
  val textAlign = AssignCss((s, v) => s.textAlign = v)
  val stroke = AssignCss((s, v) => s.stroke = v)
  val zIndex = Assign[HTMLElement, Int]((e, v) => e.style.zIndex = v.toString)
  val showHide = Assign[HTMLElement, Boolean]((e, b) =>
    if b then e(displayDefault) else e(displayNone)
  )
  val fontWeight = AssignCss((s, v) => s.fontWeight = v)

  val adjustForFlex = Modifier[HTMLElement](e => {
    e.setAttribute("size", "1")
    e.style.setProperty("flex-base", "0")
  })
  val flexGrow = AssignCss((s, v) => s.setProperty("flex-grow", v))
  val flexShrink = AssignCss((s, v) => s.setProperty("flex-shrink", v))
  val flex = AssignCss((s, v) => s.setProperty("flex", v))

  val innerText = Assign[HTMLElement, String]((e, t) => e.innerText = t)
  val innerHTML = Assign[HTMLElement, String]((e, t) => e.innerHTML = t)

  val href = Assign[HTMLAnchorElement, String]((e, a) => {
    val value = if a.isEmpty then "javascript:void(0)" else a
    e.setAttribute("href", value)
  })

  class EventListener[Ele <: HTMLElement, Ev](name: String):
    def :=(h: Ev => Unit) = Modifier[Ele](e => e.addEventListener(name, h))
    def :=(h: () => Unit) =
      Modifier[Ele](e => e.addEventListener(name, (_: Ev) => h()))
    def :=(h: js.Function1[Ev, Unit]) = Modifier[Ele](e =>
      if h == null then System.err.println(s"null handler for add ${name}")
      e.addEventListener(name, h)
    )
    def :-(h: js.Function1[Ev, Unit]) = Modifier[Ele](e =>
      if h == null then System.err.println(s"null handler for remove ${name}")
      e.removeEventListener(name, h)
    )

  val onsubmit = new EventListener[HTMLFormElement, Event]("submit"):
    override def :=(h: Event => Unit) = Modifier[HTMLFormElement](e =>
      e.addEventListener(
        "submit",
        (ev: Event) => {
          ev.preventDefault()
          ev.stopPropagation()
          h(ev)
        }
      )
    )

  val onclick = EventListener[HTMLElement, MouseEvent]("click")
  val onmousedown = EventListener[HTMLElement, MouseEvent]("mousedown")
  val onmouseup = EventListener[HTMLElement, MouseEvent]("mouseup")
  val onmousemove = EventListener[HTMLElement, MouseEvent]("mousemove")
  val onmouseenter = EventListener[HTMLElement, MouseEvent]("mouseenter")
  val onmouseleave = EventListener[HTMLElement, MouseEvent]("mouseleave")
  val oncontextmenu = EventListener[HTMLElement, MouseEvent]("contextmenu")
  val onchange = EventListener[HTMLElement, Event]("change")
  val oninput = EventListener[HTMLElement, Event]("input")
  val onkeyup = EventListener[HTMLElement, KeyboardEvent]("keyup")
  val onkeydown = EventListener[HTMLElement, KeyboardEvent]("keydown")
  def oncustomevent[T](name: String) =
    EventListener[HTMLElement, CustomEvent[T]](name)

  private lazy val textAreaWorkarea = document.createElement("textarea")

  def raw[E <: HTMLElement](text: String): Modifier[E] =
    new Modifier[E]:
      def modify(e: E): Unit =
        textAreaWorkarea.innerHTML = text
        val decoded = textAreaWorkarea.innerText
        e.appendChild(document.createTextNode(decoded))

  def hoverBackground(bg: String): Modifier[HTMLElement] =
    var save: String = ""
    Modifier(e => {
      ElementQ(e)(
        onmouseenter := (() => {
          save = e.style.background
          e.style.background = bg
        }),
        onmouseleave := (() => {
          e.style.background = save
        })
      )
    })
