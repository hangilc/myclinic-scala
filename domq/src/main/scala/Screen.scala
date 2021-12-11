package dev.fujiwara.domq

import org.scalajs.dom.raw.HTMLElement

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions

object Screen:
  def create(zIndex: Int) = div(
    cls := "domq-screen",
    css(style => {
      style.zIndex = zIndex.toString
    })
  )
