package dev.fujiwara.domq

import org.scalajs.dom.document
import org.scalajs.dom.HTMLElement
import org.scalajs.dom.HTMLImageElement
import org.scalajs.dom.HTMLInputElement
import org.scalajs.dom.HTMLOptionElement
import org.scalajs.dom.HTMLSelectElement
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions

object Html:

  case class Tag(tag: String):
    def apply(modifiers: Modifier*): ElementQ =
      val e = document.createElement(tag).asInstanceOf[HTMLElement]
      val ex = ElementQ(e)
      ex.apply(modifiers: _*)

  private def element(tag: String): HTMLElement =
    document.createElement(tag).asInstanceOf[HTMLElement]

  val div = Tag("div")
  val h1 = Tag("h1")
  val h2 = Tag("h2")
  val h3 = Tag("h3")
  val h4 = Tag("h4")
  val h5 = Tag("h5")
  val h6 = Tag("h6")
  def a: HTMLElement = 
    val e = element("a")
    e.setAttribute("href", "javascript:void(0);")
    e
  def button: HTMLElement = element("button")
  val p = Tag("p")
  def form: HTMLElement = {
    val e = element("form")
    e(onsubmit := (() => ()))
  }
  val input = Tag("input")
  val label = Tag("label")
  val ul = Tag("ul")
  val li = Tag("li")
  val span = Tag("span")
  def select: HTMLSelectElement = element("select").asInstanceOf[HTMLSelectElement]
  def option: HTMLOptionElement = element("option").asInstanceOf[HTMLOptionElement]
  def textarea: HTMLInputElement = element("textarea").asInstanceOf[HTMLInputElement]
  def inputText = input(attr("type") := "text")
  def checkbox: HTMLElement = 
    val e = element("input")
    e.setAttribute("type", "checkbox")
    e
  def radio(name: String, value: String): HTMLElement =
    val e = element("input")
    e.setAttribute("type", "radio")
    e.setAttribute("name", name)
    e.asInstanceOf[HTMLInputElement].value = value
    e
  def img: HTMLImageElement = element("img").asInstanceOf[HTMLImageElement]
