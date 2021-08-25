package dev.myclinic.scala.web

import org.scalajs.dom.document
import org.scalajs.dom.raw.DocumentFragment
import org.scalajs.dom.raw.Element
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.raw.HTMLInputElement
import org.scalajs.dom.raw.MouseEvent

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

  private var genIdCounter = 0

  def genId(): String = {
    genIdCounter += 1
    s"genId-$genIdCounter"
  }

}

case class ElementModifier(modifier: Element => Unit)

object Modifiers {
  import Implicits._

  case class Creator[A](f: (Element, A) => Unit) {
    def :=(arg: A): ElementModifier = ElementModifier(e => f(e, arg))
  }

  case class ClsCreator() {
    def :=(arg: String) = ElementModifier(e => {
      for (c <- arg.split("\\s+"))
        e.classList.add(c)
    })

    def :-(arg: String) = ElementModifier(e => {
      for (c <- arg.split("\\s+"))
        e.classList.remove(c)
    })
  }

  val cls = ClsCreator()

  val cb = Creator[Element => Unit]((e, handler) => handler(e))

  def attr(name: String) = Creator[String]((e, a) => {
    e.setAttribute(name, a)
  })

  val style = attr("style")

  val href = Creator[String]((e, a) => {
    val value = if (a.isEmpty) "javascript:void(0)" else a
    e.setAttribute("href", value)
  })

  val ml = Creator[Int]((e, a) => {
    e.classList.add(s"ms-$a")
  })

  val mr = Creator[Int]((e, a) => {
    e.classList.add(s"me-$a")
  })

  val mt = Creator[Int]((e, a) => {
    e.classList.add(s"mt-$a")
  })

  val mb = Creator[Int]((e, a) => {
    e.classList.add(s"mb-$a")
  })

  def bindTo(target: Binding.TextBinding) = ElementModifier(e => {
    target.bind(e)
  })

  def bindTo(target: Binding.InputBinding) = ElementModifier(e => {
    target.bind(e.asInstanceOf[HTMLInputElement])
  })

  val onclick = Creator[() => Unit]((e, a) => {
    e.onclick(a)
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

  case class Tag(tag: String) {
    def apply(modifiers: ElementModifier*): ElementEx = {
      val e = document.createElement(tag)
      val ex = ElementEx(e)
      ex.apply(modifiers: _*)
    }
  }

  def tag(tag: String)(modifiers: ElementModifier*): ElementEx = {
    val e = document.createElement(tag)
    val ex = ElementEx(e)
    ex.apply(modifiers: _*)
  }

  val div = Tag("div")
  val h1 = Tag("h1")
  val h2 = Tag("h2")
  val h3 = Tag("h3")
  val h4 = Tag("h4")
  val h5 = Tag("h5")
  val h6 = Tag("h6")
  val a = Tag("a")
  val button = Tag("button")
  val p = Tag("p")
  val form = Tag("form")
  val input = Tag("input")
  val label = Tag("label")

}

object Bs {
  import Modifiers._

  def btn(kind: String) = List(
    attr("type") := "button",
    cls := s"btn $kind"
  )

  @js.native
  @JSGlobal("bootstrap.Modal")
  class Modal(val ele: Element) extends js.Object {
    def toggle(): Unit = js.native
    def show(): Unit = js.native
    def hide(): Unit = js.native
    def dispose(): Unit = js.native
  }

}

object Binding {
  import Modifiers._
  import Implicits._

  case class TextBinding() {
    var ele: Element = null

    def bind(e: Element): Unit = {
      ele = e
    }

    def text: String = {
      require(ele != null)
      ele.innerText
    }

    def text_=(value: String): Unit = {
      require(ele != null)
      ele.innerText = value
    }
  }

  case class InputBinding() {
    var ele: HTMLInputElement = null

    def bind(e: HTMLInputElement): Unit = {
      ele = e
    }

    def value: String = {
      require(ele != null)
      ele.value
    }

    def value_=(v: String): Unit = {
      require(ele != null)
      ele.value = v
    }

    def setValid(valid: Boolean): Boolean = {
      if( valid ){
        ele(cls :- "is-invalid", cls := "is-valid")
      } else {
        ele(cls :- "is-valid", cls := "is-invalid")
      }
      valid
    }
  }

}

