package dev.fujiwara.domq

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
  def button = create[HTMLButtonElement]("button")(attr("type") := "button")
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
  def pre = create[HTMLPreElement]("pre")

  def inputText = input(attr("type") := "text")
  def checkbox = input(attr("type") := "checkbox")
  def radio = input(attr("type") := "radio")
  def img = create[HTMLImageElement]("img")
  def hr = create[HTMLHRElement]("hr")

  def text(data: String) = document.createTextNode(data)
