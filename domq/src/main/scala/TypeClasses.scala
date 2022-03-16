package dev.fujiwara.domq

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}

import org.scalajs.dom.HTMLElement
import org.scalajs.dom.HTMLButtonElement
import org.scalajs.dom.HTMLAnchorElement
import org.scalajs.dom.HTMLFormElement

trait ElementProvider[T]:
  def getElement(t: T): HTMLElement

object ElementProvider:
  given ElementProvider[HTMLElement] = ele => ele

trait TriggerProvider[T]:
  def setTriggerHandler(t: T, handler: () => Unit): Unit

object TriggerProvider:
  given TriggerProvider[HTMLButtonElement] with
    def setTriggerHandler(b: HTMLButtonElement, handler: () => Unit): Unit =
      b(onclick := handler)
  given TriggerProvider[HTMLAnchorElement] with
    def setTriggerHandler(a: HTMLAnchorElement, handler: () => Unit): Unit =
      a(onclick := handler)
  given TriggerProvider[HTMLFormElement] with
    def setTriggerHandler(f: HTMLFormElement, handler: () => Unit): Unit =
      f(onsubmit := handler)

trait DataProvider[T, D]:
  def getData(t: T): D

trait DataAcceptor[T, D]:
  def setData(t: T, d: D): Unit

