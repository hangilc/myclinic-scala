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
)(using layout: RadioGroup.Layout[T]):
  val radioLabels: List[RadioLabel[T]] = items.map { case (label, value) =>
    RadioLabel(name, value, label, wrapper = itemWrapper())
  }

  def getRadioLabel(value: T): RadioLabel[T] =
    radioLabels.find(_.value == value).get

  val ele: HTMLElement = layout.exec(this)

  initValue match {
    case Some(v) => check(v)
    case None    => radioLabels.headOption.foreach(_.check())
  }

  def selected: T =
    radioLabels.find(_.checked).get.value

  def value: T = selected

  def values: List[T] = items.map(_._2)

  def check(value: T): Unit =
    radioLabels.find(_.value == value).foreach(_.check())

  def addOnInputListener(listener: RadioLabel[T] => Unit): Unit =
    radioLabels.foreach(_.addOnInputListener(listener))

  def getLabel(value: T): String =
    items.find(_._2 == value).map(_._1).getOrElse("")

object RadioGroup:
  def createName: String = GenSym.genSym

  case class Layout[T](f: RadioGroup[T] => HTMLElement):
    def exec(radioGroup: RadioGroup[T]): HTMLElement = f(radioGroup)

  given defaultLayout[T]: Layout[T] =
    Layout(g => div(g.radioLabels.map(_.ele)))

  def createItemsFromValues[T](
      values: List[T],
      format: T => String = (t: T) => t.toString
  ): List[(String, T)] =
    values.map(t => (format(t), t))
