package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq
import scala.language.implicitConversions

class DateInput:
  val ele = div(display := "inline-flex", alignItems := "center", cls := "date-input")(
    select(cls := "gengou")(
      option("令和"),
      option("平成")
    ),
    inputText(cls := "nen"),
    span("年", cls := "label"),
    inputText(cls := "month"),
    span("月", cls := "label"),
    inputText(cls := "day"),
    span("日", cls := "label")
  )
