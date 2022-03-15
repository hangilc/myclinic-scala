package dev.fujiwara.domq

import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}

class FlipFlop(flipElement: HTMLElement, flopElement: HTMLElement):
  private val flipDisplay: String = defaultDisplay(flipElement.style.display)
  private val flopDisplay: String = defaultDisplay(flopElement.style.display)
  println(("flip-flop", flipDisplay, flopDisplay))
  val ele = div(
    flipElement(flipDisplay),
    flopElement(displayNone)
  )
  private var flag = 1
  def isFlip: Boolean = flag > 0
  def flip(): Unit =
    flipElement(display := flipDisplay)
    flopElement(displayNone)
    flag = 1
  def flop(): Unit =
    flipElement(displayNone)
    flopElement(display := flopDisplay)
    flag = -1
  def flipFlop(): Unit =
    if isFlip then flop() else flip()

  def defaultDisplay(display: String): String =
    if display == "none" then ""
    else display
