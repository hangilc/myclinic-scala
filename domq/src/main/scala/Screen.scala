package dev.fujiwara.domq

import org.scalajs.dom.HTMLElement

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions

object Screen:
  def screen: HTMLElement = div(cls := "domq-screen")
