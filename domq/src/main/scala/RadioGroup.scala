package dev.fujiwara.domq

import Html.{*, given}
import Modifiers.{*, given}
import ElementQ.{*, given}
import org.scalajs.dom.HTMLElement

case class RadioGroup[T](
    items: List[(String, T)],
    name: String = RadioGroup.createName,
    itemWrapper: () => HTMLElement = () => span
):
  val radioLabels: List[RadioLabel[T]] = items.map { case (label, value) =>
    RadioLabel(name, value, label)
  }
  val ele = div(radioLabels.map(_.ele))
  radioLabels.headOption.foreach(_.check())

  def selected: T = 
    radioLabels.find(_.checked).get.value

  def check(value: T): Unit =
    radioLabels.find(_.value == value).foreach(_.check())

object RadioGroup:
  def createName: String = GenSym.genSym
