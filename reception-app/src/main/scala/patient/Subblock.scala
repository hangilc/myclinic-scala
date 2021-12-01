package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons}
import scala.language.implicitConversions
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}

class Subblock(title: String, content: HTMLElement, commands: HTMLElement):
  val ele = div(cls := "subblock")(
    div(cls := "subblock-title")(title),
    div(cls := "subblock-content")(content),
    div(cls := "subblock-commands")(commands)
  )