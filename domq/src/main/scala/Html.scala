package dev.fujiwara.domq

import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLElement

object Html {

  case class Tag(tag: String):
    def apply(modifiers: Modifier*): ElementQ =
      val e = document.createElement(tag).asInstanceOf[HTMLElement]
      val ex = ElementQ(e)
      ex.apply(modifiers: _*)

  def tag(tag: String)(modifiers: Modifier*): ElementQ =
    val e = document.createElement(tag).asInstanceOf[HTMLElement]
    val ex = ElementQ(e)
    ex.apply(modifiers: _*)

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
  val ul = Tag("ul")
  val li = Tag("li")

}