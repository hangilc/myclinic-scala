package dev.myclinic.scala.web.appoint.sheet.editappoint

import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Colors}
import scala.language.implicitConversions
import org.scalajs.dom.raw.HTMLElement

class PatientIdPart(var patientId: Int):
  val keyPart = span("患者番号：")
  val valuePart = div()
  Disp(valuePart).populate()

  class Disp(wrapper: HTMLElement):
    def populate(): Unit =
      val editIcon = Icons.pencilAlt(color = "gray")
      val ele = div(
        span(label),
        editIcon(
          Icons.defaultStyle,
          ml := "0.5rem",
          displayNone,
          onclick := (() => Edit(wrapper).populate())
        )
      )
      wrapper.innerHTML = ""
      wrapper(ele)
      ele(onmouseenter := (() => {
        editIcon(displayDefault)
        ()
      }))
      ele(onmouseleave := (() => {
        editIcon(displayNone)
        ()
      }))
    def label: String =
      if patientId == 0 then "（設定なし）"
      else patientId.toString

  class Edit(wrapper: HTMLElement):
    val enterIcon = Icons.checkCircle(color = Colors.primary)
    val discardIcon = Icons.xCircle(color = Colors.danger)
    discardIcon(onclick := (() => Disp(wrapper).populate()))

    def populate(): Unit =
      wrapper.innerHTML = ""
      wrapper(
        inputText(value := initialValue, width := "4rem"),
        enterIcon(
          Icons.defaultStyle,
          ml := "0.5rem"
        ),
        discardIcon(Icons.defaultStyle)
      )

    def initialValue: String = if patientId == 0 then "" else patientId.toString
