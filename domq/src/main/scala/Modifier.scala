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

trait Modifier[-E]:
  def modify(e: E): Unit

object Modifier:
  def apply[E](m: E => Unit): Modifier[E] = new Modifier[E]:
    def modify(e: E): Unit = m(e)

object Modifiers:
  given Conversion[String, Modifier[HTMLElement]] with
    def apply(s: String): Modifier[HTMLElement] =
      Modifier[HTMLElement](
        e => {
          val t = document.createTextNode(s)
          e.appendChild(t)
        }
      )
  given Conversion[HTMLElement, Modifier[HTMLElement]] with
    def apply(e: HTMLElement): Modifier[HTMLElement] =
      Modifier[HTMLElement](ele => ele.appendChild(e))
  given Conversion[SVGElement, Modifier[HTMLElement]] with
    def apply(e: SVGElement): Modifier[HTMLElement] =
      Modifier[HTMLElement](ele => ele.appendChild(e))

  
  abstract class Assign[E, A]:
    def :=(a: A): Modifier[E]
  object Assign:
    def apply[E, A](m: (E, A) => Unit) = new Assign[E, A]:
      def :=(a: A): Modifier[E] = Modifier[E](e => m(e, a))

  object cls:
    def :=(a: String): Modifier[HTMLElement] = (e => 
      a.trim.split("\\s+").foreach(c => e.classList.add(c))
    )
    def :-(a: String): Modifier[HTMLElement] = (e => 
      a.trim.split("\\s+").foreach(c => e.classList.remove(c))
    )

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

  //def value = Assign[HTMLInputElement, String]((e, v) => e.value = v)
  object value:
    def :=(arg: String): Modifier[HTMLInputElement | HTMLOptionElement] =
      Modifier[HTMLInputElement | HTMLOptionElement]((e: HTMLInputElement | HTMLOptionElement) =>
        e match {
          case i: HTMLInputElement => i.value = arg
          case o: HTMLOptionElement => o.value = arg
        })

  val id = attr("id")

  def placeholder: Attr = attr("placeholder")

  object disabled:
    def :=(arg: Boolean): Modifier[HTMLInputElement | HTMLButtonElement] =
      Modifier[HTMLInputElement | HTMLButtonElement]((e: HTMLInputElement | HTMLButtonElement) =>
        e match {
          case i: HTMLInputElement => i.disabled = arg
          case b: HTMLButtonElement => b.disabled = arg
        }
      )

  val checked = Assign[HTMLInputElement, Boolean](
    (e, b) => e.checked = b
  )

  val name: Attr = attr("name")

  def css(f: CSSStyleDeclaration => Unit) = Modifier[HTMLElement](
    e => f(e.style)
  )

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
  val width = AssignCss((s, v) => if !v.isEmpty then s.width = v)
  val height = AssignCss((s, v) => s.height = v)
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

  val adjustForFlex = Modifier[HTMLElement](e => {
    e.setAttribute("size", "1")
    e.style.setProperty("flex-base", "0")
  })
  val flexGrow = AssignCss((s, v) => s.setProperty("flex-grow", v))
  val flexShrink = AssignCss((s, v) => s.setProperty("flex-shrink", v))
  val flex = AssignCss((s, v) => s.setProperty("flex", v))

  val innerText = Assign[HTMLElement, String]((e, t) => e.innerText = t)

  val href = Assign[HTMLAnchorElement, String]((e, a) => {
    val value = if a.isEmpty then "javascript:void(0)" else a
    e.setAttribute("href", value)
  })

  class EventListener[Ele <: HTMLElement, Ev](name: String):
    def :=(h: Ev => Unit) = Modifier[Ele](e => e.addEventListener(name, h))
    def :=(h: () => Unit) = Modifier[Ele](e => e.addEventListener(name, (_: Ev) => h()))
    def :=(h: js.Function1[Ev, Unit]) = Modifier[Ele]( e =>
      if h == null then System.err.println(s"null handler for add ${name}")
      e.addEventListener(name, h)
    )
    def :-(h: js.Function1[Ev, Unit]) = Modifier[Ele]( e =>
      if h == null then System.err.println(s"null handler for remove ${name}")
      e.removeEventListener(name, h)
    )

  val onsubmit = new EventListener[HTMLFormElement, Event]("submit"):
    override def :=(h: Event => Unit)= Modifier[HTMLFormElement](
      e => e.addEventListener("submit",
        (ev: Event) => {
          ev.preventDefault
          ev.stopPropagation
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
  def oncustomevent[T](name: String) = EventListener[HTMLElement, CustomEvent[T]](name)

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



// import org.scalajs.dom.document
// import org.scalajs.dom.HTMLElement
// import org.scalajs.dom.MouseEvent
// import scala.language.implicitConversions
// import org.scalajs.dom.CSSStyleDeclaration
// import scala.scalajs.js
// import org.scalajs.dom.Event
// import org.scalajs.dom.HTMLInputElement
// import org.scalajs.dom.KeyboardEvent

// trait Modifier[+E]:
//   def modify[E2 >: E <: HTMLElement](e: E2): Unit

// object ModifierOrig:

//   given toTextModifier: scala.Conversion[String, Modifier] = data =>
//     Modifier(e => {
//       val t = document.createTextNode(data)
//       e.appendChild(t)
//     })

//   implicit def toChildModifier(e: HTMLElement): Modifier =
//     Modifier(ele => {
//       ele.appendChild(e)
//     })

//   implicit def toChildModifier(e: ElementQ): Modifier =
//     Modifier(ele => {
//       ele.appendChild(e.ele)
//     })

//   implicit def toListModifier(ms: List[Modifier]): Modifier =
//     Modifier(target => {
//       ms.foreach(_.modifier(target))
//     })

//   private val ta = document.createElement("textarea")

//   def raw(text: String): Modifier = Modifier(target => {
//     ta.innerHTML = text
//     val decoded = ta.innerText
//     target.appendChild(document.createTextNode(decoded))
//   })

// object Modifiers:

//   case class Creator[A](f: (HTMLElement, A) => Unit):
//     def :=(arg: A) = Modifier(e => f(e, arg))

//   case class ClsModifier():
//     def :=(arg: String) = Modifier(e => {
//       for c <- arg.split("\\s+") do e.classList.add(c)
//     })

//     def :-(arg: String) = Modifier(e => {
//       for c <- arg.split("\\s+") do e.classList.remove(c)
//     })

//   val cls = ClsModifier()

//   val cb = Creator[HTMLElement => Unit]((e, handler) => handler(e))

//   case class Attr(name: String):
//     def :=(arg: String): Modifier = Modifier(e => e.setAttribute(name, arg))
//     def remove: Modifier = Modifier(e => e.removeAttribute(name))

//   def attr(name: String): Attr = Attr(name)

//   object value:
//     def :=(arg: String) = Modifier(e => e.asInstanceOf[HTMLInputElement].value = arg)

//   val id = attr("id")

//   def attrNS(namespace: String, name: String) = Creator[String]((e, a) => {
//     e.setAttributeNS(namespace, name, a)
//   })

//   def placeholder: Attr = attr("placeholder")

//   def disabled: Creator[Boolean] = Creator[Boolean]((e, disable) => {
//     if disable then e.setAttribute("disabled", "disabled")
//     else e.removeAttribute("disabled")
//   })

//   def checked: Creator[Boolean] = Creator[Boolean]((e, check) => {
//     if e.isInstanceOf[HTMLInputElement] then
//       val eCheck = e.asInstanceOf[HTMLInputElement]
//       eCheck.checked = check
//   })

//   val name: Attr = attr("name")

//   def css(f: CSSStyleDeclaration => Unit): Modifier = Modifier(e => f(e.style))

//   val mt = Creator[String]((e, value) => e.style.marginTop = value)
//   val mb = Creator[String]((e, value) => e.style.marginBottom = value)
//   val ml = Creator[String]((e, value) => e.style.marginLeft = value)
//   val mr = Creator[String]((e, value) => e.style.marginRight = value)

//   val leftGap: Modifier = ml := "0.5rem"
//   val topGap: Modifier = mt := "0.5em"

//   private def styleSetter(
//       f: (CSSStyleDeclaration, String) => Unit
//   ): Creator[String] =
//     Creator[String]((e, a) => f(e.style, a))

//   val padding = styleSetter((s, v) => s.padding = v)
//   val margin = styleSetter((s, v) => s.margin = v)
//   val border = styleSetter((s, v) => s.border = v)
//   val color = styleSetter((s, v) => s.color = v)
//   val background = styleSetter((s, v) => s.background = v)
//   val cursor = styleSetter((s, v) => s.cursor = v)
//   val display = styleSetter((style, value) => style.display = value)
//   val displayNone = display := "none"
//   val displayBlock = display := "block"
//   val displayInlineBlock = display := "inline-block"
//   val displayDefault = display := ""
//   val justifyContent =
//     styleSetter((style, value) => style.setProperty("justify-content", value))
//   val alignItems =
//     styleSetter((style, value) => style.setProperty("align-items", value))
//   val maxHeight = styleSetter((s, v) => s.maxHeight = v)
//   val minHeight = styleSetter((s, v) => s.minHeight = v)
//   val width = styleSetter((s, v) => if !v.isEmpty then s.width = v)
//   val height = styleSetter((s, v) => s.height = v)
//   val overflowY = styleSetter((s, v) => s.overflowY = v)
//   val overflowYAuto = overflowY := "auto"
//   val cssFloat = styleSetter((s, v) => s.cssFloat = v)
//   val floatRight = cssFloat := "right"
//   val textAlign = styleSetter((s, v) => s.textAlign = v)
//   val stroke = styleSetter((s, v) => s.stroke = v)
//   val zIndex = Creator[Int]((e, v) => e.style.zIndex = v.toString)

//   val showHide = Creator[Boolean]((e, show: Boolean) => {
//     val q = ElementQ(e)
//     if show then q(displayDefault) else q(displayNone)
//   })

//   val adjustForFlex = Modifier(e => {
//     e.setAttribute("size", "1")
//     e.style.setProperty("flex-base", "0")
//   })
//   val flexGrow = styleSetter((s, v) => s.setProperty("flex-grow", v))
//   val flexShrink = styleSetter((s, v) => s.setProperty("flex-shrink", v))
//   val flex = styleSetter((s, v) => s.setProperty("flex", v))

//   def hoverBackground(bg: String): Modifier =
//     var save: String = ""
//     Modifier(e => {
//       ElementQ(e)(
//         onmouseenter := (() => {
//           save = e.style.background
//           e.style.background = bg
//         }),
//         onmouseleave := (() => {
//           e.style.background = save
//         })
//       )
//     })

//   val innerText = Creator[String]((e, t) => e.innerText = t)

//   val href = Creator[String]((e, a) => {
//     val value = if a.isEmpty then "javascript:void(0)" else a
//     e.setAttribute("href", value)
//   })

//   object onclick:
//     def :=(f: MouseEvent => Any) = Modifier(e => {
//       e.addEventListener("click", f)
//     })

//     def :=(f: () => Any) = Modifier(e => {
//       e.addEventListener("click", (_: MouseEvent) => f())
//     })

//   object onsubmit:
//     def :=(f: () => Unit) = Modifier(e => {
//       e.addEventListener(
//         "submit",
//         (e: Event) => {
//           e.preventDefault
//           e.stopPropagation
//           f()
//         }
//       )
//     })

//   object onmousedown:
//     def :=(f: MouseEvent => Unit) = Modifier(e => {
//       e.addEventListener("mousedown", f)
//     })

//   object onmouseup:
//     def :=(f: MouseEvent => Unit) = Modifier(e => {
//       e.addEventListener("mouseup", f)
//     })

//   object onmousemove:
//     def :=(f: js.Function1[MouseEvent, Unit]) = Modifier(e => {
//       if f == null then System.err.println("null handler for onmousemove")
//       e.addEventListener("mousemove", f)
//     })

//     def :-(f: js.Function1[MouseEvent, Unit]) = Modifier(e => {
//       if f == null then System.err.println("null handler for onmousemove")
//       e.removeEventListener("mousemove", f)
//     })

//   object onmouseenter:
//     def :=(f: MouseEvent => Unit) = Modifier(e => {
//       if f == null then System.err.println("null handler for onmouseenter")
//       e.addEventListener("mouseenter", f)
//     })

//     def :=(f: () => Unit) = Modifier(e => {
//       e.addEventListener("mouseenter", (_: MouseEvent) => f())
//     })

//   object onmouseleave:
//     def :=(f: js.Function1[MouseEvent, Unit]) = Modifier(e => {
//       if f == null then System.err.println("null handler for onmouseleave")
//       e.addEventListener("mouseleave", f)
//     })

//     def :-(f: js.Function1[MouseEvent, Unit]) = Modifier(e => {
//       if f == null then System.err.println("null handler for onmouseleave")
//       e.removeEventListener("mouseleave", f)
//     })

//     def :=(f: () => Unit) = Modifier(e => {
//       e.addEventListener("mouseleave", (_: MouseEvent) => f())
//     })

//   val oncontextmenu =
//     Creator[js.Function1[MouseEvent, Unit]]((ele, handler) => {
//       if handler == null then
//         System.err.println("null handler for oncontextmenu")
//       ele.addEventListener("contextmenu", handler)
//     })

//   object onchange:
//     def :=(handler: js.Function1[Event, Unit]) = Modifier(ele => {
//       if handler == null then System.err.println("null handler for onchange")
//       ele.addEventListener("change", handler)
//     })
//     def :=(handler: () => Unit) =
//       Modifier(ele => ele.addEventListener("change", (_: Event) => handler()))

//   object onkeyup:
//     def :=(handler: KeyboardEvent => Unit): Modifier =
//       Modifier(ele => ele.addEventListener("keyup", handler))

//   class CustomEventModifier[T](eventType: String):
//     def :=(f: CustomEvent[T] => Any) = Modifier(e => e.addEventListener(eventType, f))

//   def oncustomevent[T](eventType: String) = new CustomEventModifier[T](eventType)