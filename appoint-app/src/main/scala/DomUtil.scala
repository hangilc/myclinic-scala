package dev.myclinic.scala.web

import org.scalajs.dom.raw.{
  Element,
  HTMLElement,
  DocumentFragment,
  HTMLCollection,
  MouseEvent
}
import org.scalajs.dom.{document}
import scalajs.js
import scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal
abstract class DocumentFragmentEx extends DocumentFragment {

  val childElementCount: Int = js.native
  val firstElementChild: Element = js.native

}

@js.native
@JSGlobal
abstract class HTMLTemplateElement extends HTMLElement {

  val content: DocumentFragmentEx = js.native

}

object Tmpl {

  def createElement(html: String): Element = {
    val tmpl =
      document.createElement("template").asInstanceOf[HTMLTemplateElement]
    tmpl.innerHTML = html.trim()
    tmpl.content.firstElementChild
  }

  def appendElements(target: Element, html: String): Unit = {
    val tmpl =
      document.createElement("template").asInstanceOf[HTMLTemplateElement]
    tmpl.innerHTML = html.trim()
    val content = tmpl.content
    while (content.childElementCount > 0) {
      target.appendChild(content.firstElementChild)
    }
  }

}

object DomUtil {

  def traverse(ele: Element, cb: Element => Unit): Unit = {
    cb(ele)
    val children = ele.children
    for (i <- 0 until children.length) {
      traverse(children.item(i), cb)
    }
  }

  def traversex(ele: Element, cb: (String, Element) => Unit): Unit = {
    traverse(
      ele,
      e => {
        var x: Option[String] = None
        val classList = e.classList
        for (i <- 0 until classList.length) {
          val cls = classList.item(i)
          if (cls.startsWith("x-")) {
            x = Some(cls.substring(2))
            classList.remove(cls)
          }
        }
        x match {
          case Some(xcls) => cb(xcls, e)
          case _          =>
        }
      }
    )
  }

  def onClick(ele: Element, handler: () => Unit): Unit = {
    ele.addEventListener("click", { (_: MouseEvent) => handler() })
  }

}

case class ElementModifier(modifier: Element => Unit)

object Modifiers {

  case class Creator[A](f: (Element, A) => Unit) {
    def :=(arg: A): ElementModifier = ElementModifier(e => f(e, arg))
  }

  val cls = Creator[String]((e, a) => {
    for (c <- a.split("\\s+"))
      e.classList.add(c)
  })

  val cb = Creator[Element => Unit]((e, handler) => handler(e))

  def attr(name: String) = Creator[String]((e, a) => {
    e.setAttribute(name, a)
  })

  val style = attr("style")

  val href = Creator[String]((e, a) => {
    val value = if (a.isEmpty) "javascript:void(0)" else a
    e.setAttribute("href", value)
  })

}

class ElementEx(val ele: Element) {

  def apply(modifiers: ElementModifier*): ElementEx = {
    modifiers.foreach(_.modifier(ele))
    this
  }

  def onclick(handler: MouseEvent => Unit): Element = {
    ele.addEventListener("click", handler)
    ele
  }

  def onclick(handler: () => Unit): Element = {
    onclick((_: MouseEvent) => handler())
  }

}

object ElementEx {
  def apply(e: Element): ElementEx = new ElementEx(e)
}

object Implicits {

  implicit def toElement(ex: ElementEx): Element = ex.ele

  implicit def toElementEx(e: Element): ElementEx = new ElementEx(e)

  implicit def toTextModifier(data: String): ElementModifier =
    ElementModifier(e => {
      val t = document.createTextNode(data)
      e.appendChild(t)
    })

  implicit def toChildModifier(e: Element): ElementModifier =
    ElementModifier(ele => {
      ele.appendChild(e)
    })

  implicit def toChildModifier(e: ElementEx): ElementModifier =
    ElementModifier(ele => {
      ele.appendChild(e.ele)
    })

  implicit def toListModifier(ms: List[ElementModifier]): ElementModifier =
    ElementModifier(target => {
      ms.foreach(_.modifier(target))
    })

  private val ta = document.createElement("textarea")

  def raw(text: String): ElementModifier = ElementModifier(target => {
    ta.innerHTML = text
    val decoded = ta.innerText
    target.appendChild(document.createTextNode(decoded))
  })

}

object html {

  def tag(tag: String)(modifiers: ElementModifier*): ElementEx = {
    val e = document.createElement(tag)
    val ex = ElementEx(e)
    ex.apply(modifiers: _*)
  }

  def div(modifiers: ElementModifier*): ElementEx = {
    tag("div")(modifiers: _*)
  }

  def h1(modifiers: ElementModifier*): ElementEx = {
    tag("h1")(modifiers: _*)
  }

  def h2(modifiers: ElementModifier*): ElementEx = {
    tag("h2")(modifiers: _*)
  }

  def h3(modifiers: ElementModifier*): ElementEx = {
    tag("h3")(modifiers: _*)
  }

  def h4(modifiers: ElementModifier*): ElementEx = {
    tag("h4")(modifiers: _*)
  }

  def h5(modifiers: ElementModifier*): ElementEx = {
    tag("h5")(modifiers: _*)
  }

  def h6(modifiers: ElementModifier*): ElementEx = {
    tag("h6")(modifiers: _*)
  }

  def a(modifiers: ElementModifier*): ElementEx = {
    tag("a")(modifiers: _*)
  }

  def button(modifiers: ElementModifier*): ElementEx = {
    tag("button")(modifiers: _*)
  }

}
