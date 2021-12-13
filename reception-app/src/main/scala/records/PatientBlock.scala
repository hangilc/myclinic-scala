package dev.myclinic.scala.web.reception.records

import dev.myclinic.scala.model.{Patient}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.kanjidate.KanjiDate
import dev.myclinic.scala.util.DateUtil
import java.time.LocalDate
import org.scalajs.dom.raw.{HTMLElement}

class PatientBlock(patient: Patient):
  val eDetail: HTMLElement = div(displayNone)
  val ele = div(cls := "patient-block")(
    span(s"[${patient.patientId}]")(cls := "patient-id"),
    span(patient.fullName("")),
    span("(" + patient.fullNameYomi("") + ")"),
    span(KanjiDate.dateToKanji(patient.birthday) + "生"),
    span("(" + DateUtil.calcAge(patient.birthday, LocalDate.now()).toString + "才)"),
    span(patient.sex.rep + "性"),
    a("詳細", onclick := (onDetail _)),
    eDetail(
      div(s"住所：${patient.address}"),
      div(s"電話：${patient.phone}") 
    )
  )

  def onDetail(): Unit =
    eDetail.toggle()
    