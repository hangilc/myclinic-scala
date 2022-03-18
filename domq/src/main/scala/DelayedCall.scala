package dev.fujiwara.domq

import org.scalajs.dom.window

class DelayedCall(timeoutId: Int): 
  def cancel(): Unit =
    window.clearTimeout(timeoutId)

object DelayedCall:
  def callLater(seconds: Double, proc: () => Unit): DelayedCall =
    val timeoutId = window.setTimeout(proc, seconds * 1000)
    DelayedCall(timeoutId)