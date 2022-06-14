package dev.myclinic.scala.web.practiceapp.practice.disease

import dev.fujiwara.domq.all.{_, given}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.myclinicutil.DiseaseUtil
import java.time.LocalDate
import dev.fujiwara.kanjidate.KanjiDate

case class Add(patientId: Int, visitDates: List[LocalDate]):
  private var startDate: LocalDate =
    visitDates.headOption.getOrElse(LocalDate.now())
  val nameSpan = span
  val startDateSpan = span
  val startDateWorkarea = div
  val ele = div(
    cls := "practice-disease-add",
    div("名称：", nameSpan),
    div(startDateSpan(cls := "start-date"), onclick := (doStartDateClick _)),
    startDateWorkarea
  )
  updateStartDateUI()

  def doStartDateClick(): Unit =
    val select = dateSelect
    startDateWorkarea(clear, select.ele)

  def onDateSelect(d: LocalDate): Unit =
    startDate = d
    updateStartDateUI()

  def updateStartDateUI(): Unit =
    startDateSpan(innerText := formatDate(startDate))

  def formatDate(d: LocalDate): String =
    KanjiDate.dateToKanji(d)

  def dateSelect =
    val sel = Selection[LocalDate](visitDates, d => div(formatDate(d)))
    sel.addSelectEventHandler(d =>
      onDateSelect(d)
      sel.ele.remove()
    )
    sel
