package dev.myclinic.scala.web.practiceapp.practice.disease

import dev.fujiwara.domq.all.{_, given}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.myclinicutil.DiseaseUtil
import java.time.LocalDate
import dev.fujiwara.kanjidate.KanjiDate
import dev.myclinic.scala.web.appbase.{DateUtil as AppDateUtil}
import dev.myclinic.scala.webclient.{Api, global}
import org.scalajs.dom.HTMLElement
import scala.concurrent.Future

case class Add(patientId: Int, visitDates: List[LocalDate]):
  private var startDate: LocalDate =
    visitDates.headOption.getOrElse(LocalDate.now())
  val nameSpan = span
  val startDateSpan = span
  val startDateWorkarea = div
  type SearchType = ByoumeiMaster | ShuushokugoMaster
  val searchForm: SearchForm[SearchType] =
    SearchForm.withToElement[SearchType](searchElement _, doSearch _)
  enum SearchKind:
    case Byoumei, Shuushokugo
  val searchKind = RadioGroup[SearchKind](List(
    "病名" -> SearchKind.Byoumei,
    "修飾語" -> SearchKind.Shuushokugo
  ))
  searchKind.check(SearchKind.Byoumei)
  val ele = div(
    cls := "practice-disease-add",
    div("名称：", nameSpan),
    div(
      startDateSpan(cls := "start-date", onclick := (doStartDateClick _)),
      a("変更", onclick := (doManualStartDate _))
    ),
    startDateWorkarea,
    div(
      button("入力"),
      a("の疑い"),
      a("修飾語削除")
    ),
    searchKind.ele,
    searchForm.ui.form(a("例")),
    searchForm.ui.selection.ele
  )
  updateStartDateUI()

  def searchElement(d: SearchType): HTMLElement =
    d match {
      case m: ByoumeiMaster     => div(m.name)
      case m: ShuushokugoMaster => div(m.name)
    }

  def doSearch(text: String): Future[List[SearchType]] =
    searchKind.selected match {
      case SearchKind.Byoumei => Api.searchByoumeiMaster(text, startDate)
      case SearchKind.Shuushokugo => Api.searchShuushokugoMaster(text)
    }

  def doManualStartDate(): Unit =
    AppDateUtil.getDateByDialog("開始日入力", _.foreach(onDateSelect _))

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
