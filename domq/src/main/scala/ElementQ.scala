package dev.fujiwara.domq

import org.scalajs.dom.{HTMLElement, HTMLSelectElement}
import scala.collection.mutable.ListBuffer
import org.scalajs.dom.HTMLOptionElement
import org.scalajs.dom.HTMLInputElement
import math.Ordered.orderingToOrdered
import org.scalajs.dom.HTMLButtonElement
import org.scalajs.dom.Element

object ElementQ:
  extension [E <: Element](ele: E)
    def apply(ms: Modifier[E]*): E =
      ms.foreach(m => m.modify(ele))
      ele

  extension [E <: HTMLElement](ele: E)
    def clear(): E =
      ele.innerHTML = ""
      ele

    def addChildren(elements: List[HTMLElement]): E =
      elements.foreach(e => ele.appendChild(e))
      ele

    def setChildren(elements: List[HTMLElement]): E =
      clear()
      addChildren(elements)
      ele

    def setChildren(element: HTMLElement): E =
      setChildren(List(element))

    def getParent: Option[HTMLElement] =
      val parent = ele.parentElement
      if parent != null then Some(parent)
      else None

    def withParent(f: HTMLElement => Unit): Boolean =
      getParent match {
        case Some(parent) =>
          f(parent)
          true
        case None => false
      }

    def replaceBy(newElement: HTMLElement): Boolean =
      withParent(p => p.replaceChild(newElement, ele))

    def preInsert(newElement: HTMLElement): Boolean =
      withParent(p => p.insertBefore(newElement, ele))

    def remove(): Boolean =
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

    def qSelectorAllFind(
        querySelector: String,
        f: HTMLElement => Boolean
    ): Option[HTMLElement] =
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

    def getCheckedRadioValue(name: String): Option[String] =
      val n = ele.querySelector(s"input[type=radio][name=${name}]:checked")
      if n == null then None
      else Some(n.asInstanceOf[HTMLInputElement].value)

    def setRadioGroupValue(name: String, value: String): Boolean =
      qSelectorAllFind(
        s"input[type=radio][name=${name}]",
        e => {
          e.asInstanceOf[HTMLInputElement].value == value
        }
      ).map(e => {
        e.asInstanceOf[HTMLInputElement].checked = true
        true
      }).getOrElse(false)

    def insertInOrderDesc[T](
        e: HTMLElement,
        selector: String,
        extract: HTMLElement => T
    )(using Ordering[T]): Unit =
      val eles = qSelectorAll(selector)
      val o = extract(e)
      val fOpt = eles.find(ele => extract(ele) < o)
      fOpt match {
        case Some(f) => f.parentElement.insertBefore(e, f)
        case None    => ele.appendChild(e)
      }

    def show: Unit =
      ele.style.display = ""
    
    def hide: Unit =
      ele.style.display = "none"

    def show(flag: Boolean): Unit =
      if flag then show else hide

    def toggle(): Unit =
      if ele.style.display == "none" then ele.style.display = ""
      else ele.style.display = "none"

    def listenToCustomEvent[T](typeArg: String, handler: T => Unit): Unit =
      ele.addEventListener(typeArg, (e: CustomEvent[T]) => handler(e.detail))

  extension (s: HTMLSelectElement)
    def setValue(value: String): Boolean =
      s.qSelectorAllFind(
        "option",
        e => e.asInstanceOf[HTMLOptionElement].value == value
      ) match {
        case Some(e) =>
          e.asInstanceOf[HTMLOptionElement].selected = true
          true
        case None => false
      }

    def getValue: String = s.value

    def getValueOption: Option[String] =
      val value = s.value
      if s.value.isEmpty then None
      else Some(value)

  extension (i: HTMLInputElement)
    def enable(flag: Boolean): Unit =
      i.disabled = !flag

    def enable(): Unit = enable(true)
    def disable(): Unit = enable(false)

  extension (i: HTMLButtonElement)
    def enable(flag: Boolean): Unit =
      i.disabled = !flag

    def enable(): Unit = enable(true)
    def disable(): Unit = enable(false)


// import org.scalajs.dom.HTMLElement
// import org.scalajs.dom.MouseEvent
// import scala.language.implicitConversions
// import org.scalajs.dom.{
//   HTMLDocument,
//   HTMLInputElement,
//   Node,
//   HTMLSelectElement,
//   HTMLOptionElement
// }
// import scala.concurrent.Future
// import scala.collection.mutable.ListBuffer
// import scala.scalajs.js
// import math.Ordering.Implicits.infixOrderingOps

// case class ElementQ(ele: HTMLElement):

//   def apply(modifiers: Modifier*): ElementQ =
//     modifiers.foreach(_.modifier(ele))
//     this

//   def setChildren(elements: List[HTMLElement]): ElementQ =
//     clear()
//     addChildren(elements)

//   def addChildren(elements: List[HTMLElement]): ElementQ =
//     elements.foreach(e => ele.appendChild(e))
//     this

//   def onclick(handler: MouseEvent => _): HTMLElement =
//     ele.addEventListener("click", handler)
//     ele

//   def onclick(handler: () => _): HTMLElement =
//     onclick((_: MouseEvent) => handler())

//   def clear(): Unit =
//     ele.innerHTML = ""

//   def withParent(f: Node => Unit): Unit =
//     val parent = ele.parentNode
//     if parent != null then f(parent)

//   def getParent(): Option[HTMLElement] =
//     val parent = ele.parentNode
//     if parent != null then Some(parent.asInstanceOf[HTMLElement])
//     else None

//   def preInsert(newElement: HTMLElement): Unit =
//     withParent(p => p.insertBefore(newElement, ele))

//   def replaceBy(newElement: HTMLElement): Unit =
//     withParent(p => p.replaceChild(newElement, ele))

//   def remove(): Unit =
//     withParent(n => n.removeChild(ele))

//   def isEmpty: Boolean =
//     ele.childElementCount == 0

//   def prepend(e: HTMLElement): Unit =
//     val first = ele.firstChild
//     if first == null then ele.appendChild(e)
//     else ele.insertBefore(e, first)

//   def qSelector(querySelector: String): Option[HTMLElement] =
//     val node = ele.querySelector(querySelector)
//     if node == null then None else Some(node.asInstanceOf[HTMLElement])

//   def qSelectorAll(querySelector: String): List[HTMLElement] =
//     val nodes = ele.querySelectorAll(querySelector)
//     val buf = ListBuffer[HTMLElement]()
//     for i <- 0 until nodes.length do
//       buf.addOne(nodes.item(i).asInstanceOf[HTMLElement])
//     buf.toList

//   def qSelectorAllFind(
//       querySelector: String,
//       f: HTMLElement => Boolean
//   ): Option[HTMLElement] =
//     val nodes = ele.querySelectorAll(querySelector)
//     var result: Option[HTMLElement] = None
//     var i = 0
//     while i < nodes.length do
//       val e = nodes.item(i).asInstanceOf[HTMLElement]
//       if f(e) then
//         result = Some(e)
//         i = nodes.length
//       i += 1
//     result

//   def setSelectValue(value: String): Boolean =
//     qSelectorAllFind("option", e => e.asInstanceOf[HTMLOptionElement].value == value) match {
//       case Some(e) => 
//         e.asInstanceOf[HTMLOptionElement].selected = true
//         true
//       case None => false
//     }

//   def getOptionalSelectValue(): Option[String] =
//     val value = ele.asInstanceOf[HTMLSelectElement].value
//     if value == null || js.isUndefined(value) || value.isEmpty then None
//     else Some(value)

//   def getSelectValue(): String =
//     getOptionalSelectValue().get

//   def getCheckedRadioValue: Option[String] =
//     val n = ele.querySelector("input[type=radio]:checked")
//     if n == null then None
//     else Some(n.asInstanceOf[HTMLInputElement].value)

//   def setRadioGroupValue(value: String): Boolean =
//     qSelectorAllFind(
//       "input[type=radio]",
//       e => {
//         e.asInstanceOf[HTMLInputElement].value == value
//       }
//     )
//       .map(e => {
//         e.asInstanceOf[HTMLInputElement].checked = true
//         true
//       })
//       .getOrElse(false)

//   def check(bool: Boolean = true): Unit =
//     if bool then ele.setAttribute("checked", "checked")
//     else ele.removeAttribute("checked")

//   def isChecked: Boolean =
//     ele match {
//       case e: HTMLInputElement => e.checked
//       case _                   => false
//     }

//   def asInputElement: HTMLInputElement = ele.asInstanceOf[HTMLInputElement]

//   def selector(query: String): Option[HTMLElement] =
//     val result = ele.querySelector(query)
//     if result == null then None else Some(result.asInstanceOf[HTMLElement])

//   def insertInOrderDesc[T](
//       e: HTMLElement,
//       selector: String,
//       extract: HTMLElement => T
//   )(using Ordering[T]): Unit =
//     val eles = qSelectorAll(selector)
//     val o = extract(e)
//     val fOpt = eles.find(ele => extract(ele) < o)
//     fOpt match {
//       case Some(f) => f.parentElement.insertBefore(e, f)
//       case None    => ele.appendChild(e)
//     }

//   def toggle(): Unit =
//     if ele.style.display == "none" then ele.style.display = ""
//     else ele.style.display = "none"

//   def enable(yes: Boolean): Unit =
//     if yes then ele.removeAttribute("disabled")
//     else ele.setAttribute("disabled", "disabled")

//   def enable(): Unit = enable(true)
//   def disable(): Unit = enable(false)

//   def listenToCustomEvent[T](typeArg: String, handler: T => Unit): Unit =
//     ele.addEventListener(typeArg, (e: CustomEvent[T]) => handler(e.detail))

// object ElementQ {

//   given Conversion[HTMLElement, ElementQ] = ElementQ(_)
//   given Conversion[HTMLInputElement, ElementQ] = ElementQ(_)

//   given Conversion[ElementQ, HTMLElement] = _.ele
//   given Conversion[ElementQ, HTMLInputElement] =
//     _.ele.asInstanceOf[HTMLInputElement]
//   given Conversion[(ElementQ, ElementQ), (HTMLElement, HTMLElement)] = {
//     case (a: ElementQ, b: ElementQ) => (a.ele, b.ele)
//   }
//   given Conversion[(ElementQ, HTMLElement), (HTMLElement, HTMLElement)] = {
//     case (a: ElementQ, b: HTMLElement) => (a.ele, b)
//   }
// }
