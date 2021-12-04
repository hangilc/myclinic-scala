package dev.fujiwara.domq

import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.raw.MouseEvent
import scala.language.implicitConversions
import org.scalajs.dom.raw.CSSStyleDeclaration
import scala.scalajs.js
import org.scalajs.dom.raw.Event
import org.scalajs.dom.raw.HTMLInputElement
import org.scalajs.dom.raw.KeyboardEvent

case class Modifier(modifier: HTMLElement => Unit)

object Modifier:

  given toTextModifier: scala.Conversion[String, Modifier] = data =>
    Modifier(e => {
      val t = document.createTextNode(data)
      e.appendChild(t)
    })

  implicit def toChildModifier(e: HTMLElement): Modifier =
    Modifier(ele => {
      ele.appendChild(e)
    })

  implicit def toChildModifier(e: ElementQ): Modifier =
    Modifier(ele => {
      ele.appendChild(e.ele)
    })

  implicit def toListModifier(ms: List[Modifier]): Modifier =
    Modifier(target => {
      ms.foreach(_.modifier(target))
    })

  private val ta = document.createElement("textarea")

  def raw(text: String): Modifier = Modifier(target => {
    ta.innerHTML = text
    val decoded = ta.innerText
    target.appendChild(document.createTextNode(decoded))
  })

object Modifiers:

  case class Creator[A](f: (HTMLElement, A) => Unit):
    def :=(arg: A) = Modifier(e => f(e, arg))

  case class ClsModifier():
    def :=(arg: String) = Modifier(e => {
      for c <- arg.split("\\s+") do e.classList.add(c)
    })

    def :-(arg: String) = Modifier(e => {
      for c <- arg.split("\\s+") do e.classList.remove(c)
    })

  val cls = ClsModifier()

  val cb = Creator[HTMLElement => Unit]((e, handler) => handler(e))

  case class Attr(name: String):
    def :=(arg: String): Modifier = Modifier(e => e.setAttribute(name, arg))
    def remove: Modifier = Modifier(e => e.removeAttribute(name))

  def attr(name: String): Attr = Attr(name)

  val value = attr("value")
  val id = attr("id")

  def attrNS(namespace: String, name: String) = Creator[String]((e, a) => {
    e.setAttributeNS(namespace, name, a)
  })

  def placeholder: Attr = attr("placeholder")

  def disabled: Creator[Boolean] = Creator[Boolean]((e, disable) => {
    if disable then e.setAttribute("disabled", "disabled")
    else e.removeAttribute("disabled")
  })

  def checked: Creator[Boolean] = Creator[Boolean]((e, check) => {
    if e.isInstanceOf[HTMLInputElement] then
      val eCheck = e.asInstanceOf[HTMLInputElement]
      eCheck.checked = check
  })

  val name: Attr = attr("name")

  def css(f: CSSStyleDeclaration => Unit): Modifier = Modifier(e => f(e.style))

  val mt = Creator[String]((e, value) => e.style.marginTop = value)
  val mb = Creator[String]((e, value) => e.style.marginBottom = value)
  val ml = Creator[String]((e, value) => e.style.marginLeft = value)
  val mr = Creator[String]((e, value) => e.style.marginRight = value)

  val leftGap: Modifier = ml := "0.5rem"
  val topGap: Modifier = mt := "0.5em"

  private def styleSetter(
      f: (CSSStyleDeclaration, String) => Unit
  ): Creator[String] =
    Creator[String]((e, a) => f(e.style, a))

  val padding = styleSetter((s, v) => s.padding = v)
  val margin = styleSetter((s, v) => s.margin = v)
  val border = styleSetter((s, v) => s.border = v)
  val color = styleSetter((s, v) => s.color = v)
  val background = styleSetter((s, v) => s.background = v)
  val cursor = styleSetter((s, v) => s.cursor = v)
  val display = styleSetter((style, value) => style.display = value)
  val displayNone = display := "none"
  val displayBlock = display := "block"
  val displayInlineBlock = display := "inline-block"
  val displayDefault = display := ""
  val justifyContent =
    styleSetter((style, value) => style.setProperty("justify-content", value))
  val alignItems =
    styleSetter((style, value) => style.setProperty("align-items", value))
  val maxHeight = styleSetter((s, v) => s.maxHeight = v)
  val minHeight = styleSetter((s, v) => s.minHeight = v)
  val width = styleSetter((s, v) => if !v.isEmpty then s.width = v)
  val height = styleSetter((s, v) => s.height = v)
  val overflowY = styleSetter((s, v) => s.overflowY = v)
  val overflowYAuto = overflowY := "auto"
  val cssFloat = styleSetter((s, v) => s.cssFloat = v)
  val floatRight = cssFloat := "right"
  val textAlign = styleSetter((s, v) => s.textAlign = v)

  val showHide = Creator[Boolean]((e, show: Boolean) => {
    val q = ElementQ(e)
    if show then q(displayDefault) else q(displayNone)
  })

  val adjustForFlex = Modifier(e => {
    e.setAttribute("size", "1")
    e.style.setProperty("flex-base", "0")
  })
  val flexGrow = styleSetter((s, v) => s.setProperty("flex-grow", v))
  val flexShrink = styleSetter((s, v) => s.setProperty("flex-shrink", v))
  val flex = styleSetter((s, v) => s.setProperty("flex", v))

  def hoverBackground(bg: String): Modifier =
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

  val innerText = Creator[String]((e, t) => e.innerText = t)

  val href = Creator[String]((e, a) => {
    val value = if a.isEmpty then "javascript:void(0)" else a
    e.setAttribute("href", value)
  })

  object onclick:
    def :=(f: MouseEvent => Unit) = Modifier(e => {
      e.addEventListener("click", f)
    })

    def :=(f: () => Unit) = Modifier(e => {
      e.addEventListener("click", (_: MouseEvent) => f())
    })

  object onsubmit:
    def :=(f: () => Unit) = Modifier(e => {
      e.addEventListener(
        "submit",
        (e: Event) => {
          e.preventDefault
          e.stopPropagation
          f()
        }
      )
    })

  object onmousedown:
    def :=(f: MouseEvent => Unit) = Modifier(e => {
      e.addEventListener("mousedown", f)
    })

  object onmouseup:
    def :=(f: MouseEvent => Unit) = Modifier(e => {
      e.addEventListener("mouseup", f)
    })

  object onmousemove:
    def :=(f: js.Function1[MouseEvent, Unit]) = Modifier(e => {
      if f == null then System.err.println("null handler for onmousemove")
      e.addEventListener("mousemove", f)
    })

    def :-(f: js.Function1[MouseEvent, Unit]) = Modifier(e => {
      if f == null then System.err.println("null handler for onmousemove")
      e.removeEventListener("mousemove", f)
    })

  object onmouseenter:
    def :=(f: MouseEvent => Unit) = Modifier(e => {
      if f == null then System.err.println("null handler for onmouseenter")
      e.addEventListener("mouseenter", f)
    })

    def :=(f: () => Unit) = Modifier(e => {
      e.addEventListener("mouseenter", (_: MouseEvent) => f())
    })

  object onmouseleave:
    def :=(f: js.Function1[MouseEvent, Unit]) = Modifier(e => {
      if f == null then System.err.println("null handler for onmouseleave")
      e.addEventListener("mouseleave", f)
    })

    def :-(f: js.Function1[MouseEvent, Unit]) = Modifier(e => {
      if f == null then System.err.println("null handler for onmouseleave")
      e.removeEventListener("mouseleave", f)
    })

    def :=(f: () => Unit) = Modifier(e => {
      e.addEventListener("mouseleave", (_: MouseEvent) => f())
    })

  val oncontextmenu =
    Creator[js.Function1[MouseEvent, Unit]]((ele, handler) => {
      if handler == null then
        System.err.println("null handler for oncontextmenu")
      ele.addEventListener("contextmenu", handler)
    })

  object onchange:
    def :=(handler: js.Function1[Event, Unit]) = Modifier(ele => {
      if handler == null then System.err.println("null handler for onchange")
      ele.addEventListener("change", handler)
    })
    def :=(handler: () => Unit) =
      Modifier(ele => ele.addEventListener("change", (_: Event) => handler()))

  object onkeyup:
    def :=(handler: KeyboardEvent => Unit): Modifier =
      Modifier(ele => ele.addEventListener("keyup", handler))
