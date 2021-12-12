package dev.fujiwara.domq

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq
import scala.language.implicitConversions

class Selection[T](cb: T => Unit):
  val ele = div(cls := "domq-selection")

  def add(label: String, value: T): Unit =
    val e =
      div(label, cls := "domq-selection-item", onclick := (() => cb(value)))
    ele(e)

  def clear(): Unit = 
    ele.innerHTML = ""
    
