package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form, ErrorBox, Modifier}
import scala.language.implicitConversions
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}
import dev.myclinic.scala.util.{KanjiDate, DateUtil}
import dev.myclinic.scala.model.{Sex, Patient}
import java.time.LocalDate

class PatientDisp(val patient: Patient):
  val ele: HTMLElement = Form.rows(
    span("氏名") -> span(patient.fullName()),
    span("よみ") -> span(patient.fullNameYomi()),
    span("性別") -> span(patient.sex.rep),
    span("生年月日") -> div(
      span(KanjiDate.dateToKanji(patient.birthday)),
      span(
        DateUtil.calcAge(patient.birthday, LocalDate.now()).toString + "才",
        ml := "1rem"
      )
    ),
    span("住所") -> span(patient.address),
    span("電話") -> span(patient.phone)
  )
