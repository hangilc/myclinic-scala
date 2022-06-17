package dev.myclinic.scala.web.practiceapp.practice.disease

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.webclient.{Api, global}
import scala.concurrent.Future
import java.time.LocalDate

case class Search(startDateRef: () => LocalDate, examples: List[DiseaseExample]):
  import Search.{SearchKind, SearchType}

  val searchKind = RadioGroup[SearchKind](
    List(
      "病名" -> SearchKind.Byoumei,
      "修飾語" -> SearchKind.Shuushokugo
    )
  )
  searchKind.check(SearchKind.Byoumei)
  val searchForm: SearchForm[SearchType] =
    SearchForm[SearchType](Search.searchElement _, doSearch _)

  val ele = div(
    searchKind.ele,
    searchForm.ui.form(a("例", onclick := (doExamples _))),
    searchForm.ui.selection.ele
  )

  def doSearch(text: String): Future[List[SearchType]] =
    Search.search(text, searchKind.selected, startDateRef())

  def doExamples(): Unit =
    searchForm.ui.selection.clear()
    searchForm.ui.selection.addAll(examples, Search.searchElement _)

object Search:
  enum SearchKind:
    case Byoumei, Shuushokugo

  type SearchType = ByoumeiMaster | ShuushokugoMaster | DiseaseExample

  def searchElement(d: SearchType): String =
    d match {
      case m: ByoumeiMaster     => m.name
      case m: ShuushokugoMaster => m.name
      case e: DiseaseExample    => e.label
    }

  def search(
      text: String,
      searchKind: SearchKind,
      startDate: LocalDate
  ): Future[List[SearchType]] =
    searchKind match {
      case SearchKind.Byoumei => Api.searchByoumeiMaster(text, startDate)
      case SearchKind.Shuushokugo =>
        Api.searchShuushokugoMaster(text, startDate)
    }
