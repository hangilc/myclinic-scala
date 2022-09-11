package dev.myclinic.scala.web.practiceapp.practice.disease

import dev.fujiwara.domq.all.{_, given}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.myclinicutil.DiseaseUtil
import java.time.LocalDate
import dev.fujiwara.kanjidate.KanjiDate
import dev.myclinic.scala.webclient.{Api, global}
import org.scalajs.dom.HTMLElement
import scala.concurrent.Future
import cats.syntax.all.*
import scala.language.implicitConversions
import dev.fujiwara.domq.dateinput.DateInputDialog
import dev.fujiwara.domq.SelectionConfig

case class Add(
    patientId: Int,
    visitDates: List[LocalDate],
    examples: List[DiseaseExample]
):
  private var startDate: LocalDate =
    visitDates.headOption.getOrElse(LocalDate.now())
  private var cur: Add.Current = Add.Current()
  val nameSpan = span
  val startDateSpan = span
  val startDateWorkarea = div
  type SearchType = ByoumeiMaster | ShuushokugoMaster | DiseaseExample
  val selConfig: SelectionConfig = new SelectionConfig{
    override def invokeHandlerOnSingleResult: Boolean = true
  }
  val searchForm: SearchForm[SearchType] =
    SearchForm[SearchType](searchElement _, doSearch _)(using selConfig)
  searchForm.onSelect(doSelect _)
  enum SearchKind:
    case Byoumei, Shuushokugo
  val searchKind = RadioGroup[SearchKind](
    List(
      "病名" -> SearchKind.Byoumei,
      "修飾語" -> SearchKind.Shuushokugo
    )
  )
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
      button("入力", onclick := (doEnter _)),
      a("の疑い", onclick := (doSusp _)),
      a("修飾語削除", onclick := (doDelAdj _))
    ),
    searchKind.ele,
    searchForm.ui.form(a("例"), onclick := (doExamples _)),
    searchForm.ui.selection.ele
  )
  updateStartDateUI()
  doExamples()

  def doSusp(): Unit =
    for
      m <- Api.resolveShuushokugoMasterByName("の疑い", startDate)
    yield 
      m.foreach(adj => 
        cur = cur.addAdj(adj)
        updateNameUI()  
      )

  def doDelAdj(): Unit =
    cur = cur.copy(adjList = List.empty)
    updateNameUI()

  def doEnter(): Unit =
    cur.byoumei match {
      case None => ShowMessage.showError("病名が選択されていません。")
      case Some(bm) => 
        val data = DiseaseEnterData(
          patientId,
          bm.shoubyoumeicode,
          startDate,
          cur.adjList.map(_.shuushokugocode)
        )
        for
          diseaseId <- Api.enterDiseaseEx(data)
        yield 
          cur = Add.Current()
          updateNameUI()
    }

  def doSelect(d: SearchType): Unit =
    d match {
      case m: ByoumeiMaster =>
        cur = cur.copy(byoumei = Some(m))
        updateNameUI()
      case m: ShuushokugoMaster =>
        if !cur.adjList.contains(m) then
          cur = cur.copy(adjList = cur.adjList :+ m)
          updateNameUI()
      case e: DiseaseExample =>
        for
          bOpt <- e.byoumei.fold(Future.successful(None))(name =>
            Api.resolveByoumeiMasterByName(name, startDate)
          )
          adjList <- (e.preAdjList ++ e.postAdjList)
            .map(name => Api.resolveShuushokugoMasterByName(name, startDate).map(_.get))
            .sequence
        yield
          cur = cur.setByoumei(bOpt).addAdjList(adjList)
          updateNameUI()
    }

  def doExamples(): Unit =
    searchForm.ui.selection.clear()
    searchForm.ui.selection.addAll(examples, searchElement _)

  def searchElement(d: SearchType): String =
    d match {
      case m: ByoumeiMaster     => m.name
      case m: ShuushokugoMaster => m.name
      case e: DiseaseExample    => e.label
    }

  def doSearch(text: String): Future[List[SearchType]] =
    searchKind.selected match {
      case SearchKind.Byoumei     => Api.searchByoumeiMaster(text, startDate)
      case SearchKind.Shuushokugo => Api.searchShuushokugoMaster(text, startDate)
    }

  def doManualStartDate(): Unit =
    val dlog = DateInputDialog(startDate, title = "開始日入力")
    dlog.onEnter(onDateSelect _)

  def doStartDateClick(): Unit =
    val select = dateSelect
    startDateWorkarea(clear, select.ele)

  def onDateSelect(d: LocalDate): Unit =
    startDate = d
    updateStartDateUI()

  def updateStartDateUI(): Unit =
    startDateSpan(innerText := formatDate(startDate))

  def updateNameUI(): Unit =
    nameSpan(innerText := cur.label)

  def formatDate(d: LocalDate): String =
    KanjiDate.dateToKanji(d)

  def dateSelect =
    val sel = Selection[LocalDate](visitDates, d => div(formatDate(d)))
    sel.addSelectEventHandler(d =>
      onDateSelect(d)
      sel.ele.remove()
    )
    sel

object Add:
  case class Current(
      byoumei: Option[ByoumeiMaster] = None,
      adjList: List[ShuushokugoMaster] = List.empty
  ):
    def label: String =
      DiseaseUtil.diseaseNameOf(byoumei, adjList)
    def setByoumei(bOpt: Option[ByoumeiMaster]): Current =
      bOpt match {
        case None => this
        case Some(b) => this.copy(byoumei = Some(b))
      }

    def addAdj(m: ShuushokugoMaster): Current = 
      if adjList.contains(m) then this
      else this.copy(adjList = adjList :+ m)
    
    def addAdjList(ms: List[ShuushokugoMaster]) : Current =
      ms.foldLeft(this)((acc, m) => acc.addAdj(m))
