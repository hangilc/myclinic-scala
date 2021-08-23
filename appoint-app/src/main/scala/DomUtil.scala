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

trait ElementModifier {
  def applyTo(target: Element): Unit
}

object Modifiers {

  private def create[A](f: Element => Unit): ElementModifier = {
    new ElementModifier {
      override def applyTo(e: Element): Unit = {
        f(e)
      }
    }
  }

  object cls {
    def :=(cls: String): ElementModifier = create[String] { e =>
      e.classList.add(cls)
    }
  }

  object textModifier {
    def :=(content: String): ElementModifier = create[String](e => {
      val t = document.createTextNode(content)
      e.appendChild(t)
    })
  }
}

object Implicits {

  implicit class ElementEx(val ele: Element) {

    def apply(modifiers: ElementModifier*): ElementEx = {
      modifiers.foreach(_.applyTo(ele))
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

  implicit def toElement(ex: ElementEx): Element = ex.ele

  implicit def toTextModifier(data: String): ElementModifier = {
    new ElementModifier {
      override def applyTo(e: Element): Unit = {
        val t = document.createTextNode(data)
        e.appendChild(t)
      }
    }
  }

  implicit def toChildModifier(e: Element): ElementModifier = {
    new ElementModifier {
      override def applyTo(target: Element): Unit = {
        target.appendChild(e)
      }
    }
  }

  implicit def toChildModifier(e: ElementEx): ElementModifier = {
    new ElementModifier {
      override def applyTo(target: Element): Unit = {
        target.appendChild(e.ele)
      }
    }
  }

  def div(modifiers: ElementModifier*): ElementEx = {
    val e = document.createElement("div")
    val ex = ElementEx(e)
    ex.apply(modifiers: _*)
  }


}
