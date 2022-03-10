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

    def qSelectorAllCount(
      querySelector: String
    ): Int =
      ele.querySelectorAll(querySelector).length

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

    def extractFollowingSiblings(): List[HTMLElement] =
      var sibs: List[HTMLElement] = Nil
      while ele.nextElementSibling != null do
        val e = ele.nextElementSibling.asInstanceOf[HTMLElement]
        sibs = e :: sibs
        e.remove()
      sibs.reverse

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
