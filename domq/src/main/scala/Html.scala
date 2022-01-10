package dev.fujiwara.domq

// import org.scalajs.dom.document
// import org.scalajs.dom.HTMLElement
// import org.scalajs.dom.HTMLImageElement
// import org.scalajs.dom.HTMLInputElement
// import org.scalajs.dom.HTMLOptionElement
// import org.scalajs.dom.HTMLSelectElement
// import org.scalajs.dom.HTMLAnchorElement
// import org.scalajs.dom.HTMLButtonElement
// import org.scalajs.dom.HTMLFormElement
// import org.scalajs.dom.HTMLLabelElement
// import org.scalajs.dom.HTMLUListElement
// import org.scalajs.dom.HTMLLIElement
// import org.scalajs.dom.HTMLSpanElement
// import org.scalajs.dom.HTMLTextAreaElement
import org.scalajs.dom.*
import dev.fujiwara.domq.ElementQ.*
import dev.fujiwara.domq.Modifiers.*

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
