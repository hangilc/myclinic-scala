package dev.fujiwara.domq

import scalajs.js
import scalajs.js.annotation.JSGlobal
import org.scalajs.dom.document
import org.scalajs.dom.raw.Node
import org.scalajs.dom.raw.NodeSelector
import org.scalajs.dom.raw.Element
import org.scalajs.dom.raw.HTMLElement
import scala.collection.mutable.ListBuffer
import org.scalajs.dom.raw.HTMLCollection

@js.native
@JSGlobal
abstract class DocumentFragment extends Node with NodeSelector {

  val childElementCount: Int = js.native
  val firstElementChild: Element = js.native
  val children: HTMLCollection = js.native
}

@js.native
@JSGlobal
abstract class HTMLTemplateElement extends HTMLElement {

  val content: DocumentFragment = js.native

}

object Template {

  def createElement(html: String): Element = {
    val tmpl =
      document.createElement("template").asInstanceOf[HTMLTemplateElement]
    tmpl.innerHTML = html.trim()
    tmpl.content.firstElementChild
  }

  def createElements(html: String): List[Element] = {
    val tmpl =
      document.createElement("template").asInstanceOf[HTMLTemplateElement]
    tmpl.innerHTML = html.trim()
    val children = tmpl.content.children
    val buf = ListBuffer[Element]()
    for(i <- 0 until children.length)
      buf += children.item(i)
    buf.toList
  }

}