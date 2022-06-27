package dev.fujiwara.domq

import Html.{*, given}
import Modifiers.{*, given}
import ElementQ.{*, given}
import org.scalajs.dom.HTMLElement
import scala.language.implicitConversions

case class RadioGroup[T](
    items: List[(String, T)],
    name: String = RadioGroup.createName,
    itemWrapper: () => HTMLElement = () => span,
    initValue: Option[T] = None
):
  val radioLabels: List[RadioLabel[T]] = items.map { case (label, value) =>
    RadioLabel(name, value, label)
  }
  val ele = div(radioLabels.map(_.ele))
  initValue match {
    case Some(v) => check(v)
    case None => radioLabels.headOption.foreach(_.check())
  }

  def selected: T = 
    radioLabels.find(_.checked).get.value

  def value: T = selected

  def check(value: T): Unit =
    radioLabels.find(_.value == value).foreach(_.check())

  def addOnInputListener(listener: RadioLabel[T] => Unit): Unit =
    radioLabels.foreach(_.addOnInputListener(listener))

object RadioGroup:
  def createName: String = GenSym.genSym
