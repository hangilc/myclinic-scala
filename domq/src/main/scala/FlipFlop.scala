package dev.fujiwara.domq

import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.TypeClasses.{*, given}

class FlipFlop[Flip, Flop](flipComponent: Flip, flopComponent: Flop)(using 
  flipComp: ElementProvider[Flip],
  flipActivate: EventAcceptor[Flip, Unit, "activate"],
  flopComp: ElementProvider[Flop],
  flopActivate: EventAcceptor[Flop, Unit, "activate"]
):
  private def flipElement = flipComp.getElement(flipComponent)
  private def flopElement = flopComp.getElement(flopComponent)
  private val flipDisplay: String = defaultDisplay(flipElement.style.display)
  private val flopDisplay: String = defaultDisplay(flopElement.style.display)

  private var current: HTMLElement = flipElement
  def ele = current

  def isFlip: Boolean = current == flipElement

  private def changeTo(newElem: HTMLElement, newDisplay: String, oldElem: HTMLElement): Unit =
    newElem.remove()
    newElem(display := newDisplay)
    current.replaceBy(newElem)
    oldElem(displayNone)
    newElem(oldElem)
    current = newElem


  def flip(): Unit =
    if !isFlip then
      changeTo(flipElement, flipDisplay, flopElement)
      flipActivate.accept(flipComponent, ())

  def flop(): Unit =
    if isFlip then
      changeTo(flopElement, flopDisplay, flipElement)
      flopActivate.accept(flopComponent, ())

  def flipFlop(): Unit =
    if isFlip then flop() else flip()

  def defaultDisplay(display: String): String =
    if display == "none" then ""
    else display