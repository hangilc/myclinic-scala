package dev.fujiwara.domq

import org.scalajs.dom.document
import org.scalajs.dom.HTMLElement
import org.scalajs.dom.HTMLImageElement
import org.scalajs.dom.HTMLInputElement
import org.scalajs.dom.HTMLOptionElement
import org.scalajs.dom.HTMLSelectElement
import dev.fujiwara.domq.ElementQ.*
import dev.fujiwara.domq.Modifiers.*
import org.scalajs.dom.HTMLAnchorElement
import org.scalajs.dom.HTMLButtonElement
import org.scalajs.dom.HTMLFormElement
import org.scalajs.dom.HTMLLabelElement
import org.scalajs.dom.HTMLUListElement
import org.scalajs.dom.HTMLLIElement
import org.scalajs.dom.HTMLSpanElement
import org.scalajs.dom.HTMLTextAreaElement

object Html:
  private def create[T](tag: String): T =
    document.createElement(tag).asInstanceOf[T]

  def div = create[HTMLElement]("div")
  def h1 = create[HTMLElement]("h1")
  def h2 = create[HTMLElement]("h2")
  def h3 = create[HTMLElement]("h3")
  def h4 = create[HTMLElement]("h4")
  def h5 = create[HTMLElement]("h5")
  def h6 = create[HTMLElement]("h6")
  def a = create[HTMLAnchorElement]("a")(href := "")
  def button = create[HTMLButtonElement]("button")
  def p = create[HTMLElement]("p")
  def form = create[HTMLFormElement]("form")(
    onsubmit := (_ => ())
  )
  def input = create[HTMLInputElement]("input")
  def label = create[HTMLLabelElement]("label")
  def ul = create[HTMLUListElement]("ul")
  def li = create[HTMLLIElement]("li")
  def span = create[HTMLSpanElement]("span")
  def select = create[HTMLSelectElement]("select")
  def option = create[HTMLOptionElement]("option")
  def textarea = create[HTMLTextAreaElement]("textarea")

  def inputText = input(attr("type") := "text")
  def checkbox = input(attr("type") := "checkbox")
  def radio(radioName: String, radioValue: String) = input(
    attr("type") := "radio",
    name := radioName,
    value := radioValue)
  def img = create[HTMLImageElement]("img")

// object HtmlOrig:

//   case class Tag(tag: String):
//     def apply(modifiers: Modifier*): ElementQ =
//       val e = document.createElement(tag).asInstanceOf[HTMLElement]
//       val ex = ElementQ(e)
//       ex.apply(modifiers: _*)

//   private def element(tag: String): HTMLElement =
//     document.createElement(tag).asInstanceOf[HTMLElement]

//   val div = Tag("div")
//   val h1 = Tag("h1")
//   val h2 = Tag("h2")
//   val h3 = Tag("h3")
//   val h4 = Tag("h4")
//   val h5 = Tag("h5")
//   val h6 = Tag("h6")
//   def a: HTMLElement = 
//     val e = element("a")
//     e.setAttribute("href", "javascript:void(0);")
//     e
//   def button: HTMLElement = element("button")
//   val p = Tag("p")
//   def form: HTMLElement = {
//     val e = element("form")
//     e(onsubmit := (() => ()))
//   }
//   val input = Tag("input")
//   val label = Tag("label")
//   val ul = Tag("ul")
//   val li = Tag("li")
//   val span = Tag("span")
//   def select: HTMLSelectElement = element("select").asInstanceOf[HTMLSelectElement]
//   def option: HTMLOptionElement = element("option").asInstanceOf[HTMLOptionElement]
//   def textarea: HTMLInputElement = element("textarea").asInstanceOf[HTMLInputElement]
//   def inputText = input(attr("type") := "text")
//   def checkbox: HTMLElement = 
//     val e = element("input")
//     e.setAttribute("type", "checkbox")
//     e
//   def radio(name: String, value: String): HTMLElement =
//     val e = element("input")
//     e.setAttribute("type", "radio")
//     e.setAttribute("name", name)
//     e.asInstanceOf[HTMLInputElement].value = value
//     e
//   def img: HTMLImageElement = element("img").asInstanceOf[HTMLImageElement]
