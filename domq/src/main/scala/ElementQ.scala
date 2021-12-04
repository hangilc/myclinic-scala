package dev.fujiwara.domq

import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.raw.MouseEvent
import scala.language.implicitConversions
import org.scalajs.dom.raw.HTMLDocument
import org.scalajs.dom.raw.HTMLInputElement
import scala.concurrent.Future
import org.scalajs.dom.raw.Node
import scala.collection.mutable.ListBuffer

case class ElementQ(ele: HTMLElement):

  def apply(modifiers: Modifier*): ElementQ =
    modifiers.foreach(_.modifier(ele))
    this

  def setChildren(elements: List[HTMLElement]): ElementQ =
    clear()
    addChildren(elements)

  def addChildren(elements: List[HTMLElement]): ElementQ =
    elements.foreach(e => ele.appendChild(e))
    this

  def onclick(handler: MouseEvent => _): HTMLElement =
    ele.addEventListener("click", handler)
    ele

  def onclick(handler: () => _): HTMLElement =
    onclick((_: MouseEvent) => handler())

  def clear(): Unit =
    ele.innerHTML = ""

  def withParent(f: Node => Unit): Unit =
    val parent = ele.parentNode
    if parent != null then f(parent)

  def getParent(): Option[HTMLElement] =
    val parent = ele.parentNode
    if parent != null then Some(parent.asInstanceOf[HTMLElement])
    else None

  def replaceBy(newElement: HTMLElement): Unit =
    withParent(p => p.replaceChild(newElement, ele))

  def preInsert(newElement: HTMLElement): Unit =
    withParent(p => p.insertBefore(newElement, ele))

  def remove(): Unit =
    withParent(n => n.removeChild(ele))

  def isEmpty: Boolean =
    ele.childElementCount == 0

  def prepend(e: HTMLElement): Unit =
    val first = ele.firstChild
    if first == null then ele.appendChild(e)
    else ele.insertBefore(e, first)

  def qSelector(querySelector: String): Option[HTMLElement] =
    val node = ele.querySelector(querySelector)
    if node == null then None else Some(node.asInstanceOf[HTMLElement])

  def qSelectorAll(querySelector: String): List[HTMLElement] =
    val nodes = ele.querySelectorAll(querySelector)
    val buf = ListBuffer[HTMLElement]()
    for i <- 0 until nodes.length do
      buf.addOne(nodes.item(i).asInstanceOf[HTMLElement])
    buf.toList

  def qSelectorAllFind(querySelector: String, f: HTMLElement => Boolean): Option[HTMLElement] =
    val nodes = ele.querySelectorAll(querySelector)
    var result: Option[HTMLElement] = None
    var i = 0
    while i < nodes.length do
      val e = nodes.item(i).asInstanceOf[HTMLElement]
      if f(e) then
        result = Some(e)
        i = nodes.length
      i += 1
    result

  def setSelectValue(value: String): Boolean =
    val opt = qSelectorAllFind("option", e => e.asInstanceOf[HTMLInputElement].value == value)
    opt.fold(false)(o => {
      o.setAttribute("selected", "selected")
      true
    })
    // val nodes = ele.querySelectorAll("option")
    // val n = nodes.length
    // var i = 0
    // var opt: Option[HTMLElement] = None
    // while (i < n) do
    //   val e = nodes.item(i).asInstanceOf[HTMLElement]
    //   if e.getAttribute("value") == value then
    //     e.setAttribute("selected", "selected")
    //     opt = Some(e)
    //     i = n
    //   else
    //     e.removeAttribute("selected")
    //     i += 1
    // opt

  def getSelectedOptionValues: List[String] =
    qSelectorAll("option:checked").map(_.asInstanceOf[HTMLInputElement].value)
    // val nodes = ele.querySelectorAll("option:checked")
    //  val n = nodes.length
    // var i = 0
    // val buf = ListBuffer[String]()
    // while (i < n) do
    //   val e = nodes.item(i).asInstanceOf[HTMLElement]
    //   buf += e.getAttribute("value")
    //   i += 1
    // buf.toList

  def getCheckedRadioValue: Option[String] = 
    val n = ele.querySelector("input[type=radio]:checked")
    if n == null then None
    else Some(n.asInstanceOf[HTMLInputElement].value)

  def check(bool: Boolean = true): Unit =
    if bool then  ele.setAttribute("checked", "checked")
    else ele.removeAttribute("checked")

  def isChecked: Boolean =
    ele match {
      case e: HTMLInputElement => e.checked
      case _ => false
    }

  def asInputElement: HTMLInputElement = ele.asInstanceOf[HTMLInputElement]

  def selector(query: String): Option[HTMLElement] =
    val result = ele.querySelector(query)
    if result == null then None else Some(result.asInstanceOf[HTMLElement]) 
   

object ElementQ {
  
  given Conversion[HTMLElement, ElementQ] = ElementQ(_)
  given Conversion[HTMLInputElement, ElementQ] = ElementQ(_)

  given Conversion[ElementQ, HTMLElement] = _.ele
  given Conversion[ElementQ, HTMLInputElement] = _.ele.asInstanceOf[HTMLInputElement]
  given Conversion[(ElementQ, ElementQ), (HTMLElement, HTMLElement)] = {
    case (a: ElementQ, b: ElementQ) => (a.ele, b.ele)
  }
  given Conversion[(ElementQ, HTMLElement), (HTMLElement, HTMLElement)] = {
    case (a: ElementQ, b: HTMLElement) => (a.ele, b)
  }
}