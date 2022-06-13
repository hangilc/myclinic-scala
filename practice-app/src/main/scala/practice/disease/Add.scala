package dev.myclinic.scala.web.practiceapp.practice.disease

import dev.fujiwara.domq.all.{_, given}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.myclinicutil.DiseaseUtil
import java.time.LocalDate
import dev.fujiwara.kanjidate.KanjiDate

case class Add(patientId: Int, visitDates: List[LocalDate]):
  var startDate: LocalDate = visitDates.headOption.getOrElse(LocalDate.now())
  val dateSelect = Selection[LocalDate](visitDates, d => div(formatDate(d)), onDateSelect _)
  val nameSpan = span
  val startDateSpan = span
  val ele = div(
    cls := "practice-disease-add",
    div("名称：", nameSpan),
    div(startDateSpan(formatDate(startDate))),
    dateSelect.ele
  )

  def onDateSelect(d: LocalDate): Unit =
    ()

  def formatDate(d: LocalDate): String =
    KanjiDate.dateToKanji(d)