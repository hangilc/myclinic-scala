package dev.fujiwara.domq

import org.scalajs.dom.document
import org.scalajs.dom.raw.Element
import org.scalajs.dom.raw.MouseEvent
import scala.language.implicitConversions

case class Modifier(modifier: Element => Unit)

object Modifier {

  given toTextModifier: scala.Conversion[String, Modifier] = data =>
    Modifier(e => {
      val t = document.createTextNode(data)
      e.appendChild(t)
    })

  implicit def toChildModifier(e: Element): Modifier =
    Modifier(ele => {
      ele.appendChild(e)
    })

  implicit def toChildModifier(e: ElementQ): Modifier =
    Modifier(ele => {
      ele.appendChild(e.ele)
    })

  implicit def toListModifier(ms: List[Modifier]): Modifier =
    Modifier(target => {
      ms.foreach(_.modifier(target))
    })

  private val ta = document.createElement("textarea")

  def raw(text: String): Modifier = Modifier(target => {
    ta.innerHTML = text
    val decoded = ta.innerText
    target.appendChild(document.createTextNode(decoded))
  })

}

object Modifiers {

  case class Creator[A](f: (Element, A) => Unit) {
    def :=(arg: A) = Modifier(e => f(e, arg))
  }

  case class ClsModifier(){
    def :=(arg: String) = Modifier(e => {
      for (c <- arg.split("\\s+"))
        e.classList.add(c)
    })

    def :-(arg: String) = Modifier(e => {
      for (c <- arg.split("\\s+"))
        e.classList.remove(c)
    })
  }

  val cls = ClsModifier()

  val cb = Creator[Element => Unit]((e, handler) => handler(e))

  def attr(name: String) = Creator[String]((e, a) => {
    e.setAttribute(name, a)
  })

  val style = attr("style")

  val href = Creator[String]((e, a) => {
    val value = if (a.isEmpty) "javascript:void(0)" else a
    e.setAttribute("href", value)
  })

  object onclick {
    def :=(f: MouseEvent => Unit) = Modifier(e => {
      e.addEventListener("click", f)
    })

    def :=(f: () => Unit) = Modifier(e => {
      e.addEventListener("click", (_: MouseEvent) => f())
    })
  }
}
