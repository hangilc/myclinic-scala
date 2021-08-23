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
