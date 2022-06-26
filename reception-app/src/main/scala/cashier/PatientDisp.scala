package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.fujiwara.kanjidate.KanjiDate
import dev.fujiwara.kanjidate.DateUtil
import java.time.LocalDate

case class PatientDisp(patient: Patient):
  val ele = div(
    cls := "grid-disp reception-cashier-patient-search-result-disp",
    span("氏名"),
    span(patient.fullName()),
    span("よみ"),
    span(patient.fullNameYomi()),
    span("性別"),
    span(patient.sex.rep),
    span("生年月日"),
    div(
      span(KanjiDate.dateToKanji(patient.birthday)),
      span(
        DateUtil.calcAge(patient.birthday, LocalDate.now()).toString + "才",
        ml := "1rem"
      )
    ),
    span("住所"),
    span(patient.address),
    div("電話"),
    div(patient.phone)
  )
