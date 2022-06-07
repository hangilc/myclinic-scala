package dev.myclinic.scala.web.practiceapp.practice.patientmanip

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.Html
import dev.myclinic.scala.model.Text
import dev.myclinic.scala.model.Visit
import scala.concurrent.Future
import dev.myclinic.scala.webclient.{Api, global}
import dev.fujiwara.kanjidate.KanjiDate
import java.time.LocalDateTime
import dev.myclinic.scala.util.StringUtil
import org.scalajs.dom.Node
import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.SearchFormPaging
import dev.fujiwara.domq.SearchFormPaging.given

case class SearchTextDialog(patientId: Int):
  import SearchTextDialog.{Item, NavUI}
  import dev.fujiwara.domq.SelectionModifier
  given SelectionModifier with
    override def itemCssClass: Option[String] = None
  val navUI = NavUI()
  val form = new SearchFormPaging[(Text, Visit, String)](
    text => Api.countSearchTextForPatient(text, patientId),
    (text, limit, offset) => {
      for
        textVisits <- Api
          .searchTextForPatient(text.trim, patientId, limit, offset)
      yield textVisits.map { case (t, v) =>
        (t, v, text)
      }
    },
    (text, visit, searchText) => Item(text, visit, searchText).ele,
    navUI
  )
  form.form(button("閉じる", onclick := (close _)))
  val dlog = new ModalDialog3()
  dlog.content(cls := "practice-search-text-for-patient")
  dlog.title(innerText := "文章検索")
  dlog.body(form.ele)

  def open(): Unit =
    dlog.open()
    form.initFocus()

  def close(): Unit =
    dlog.close()

object SearchTextDialog:
  case class Item(text: Text, visit: Visit, searchText: String):
    val ele = div(
      cls := "practice-search-text-for-patient-item",
      div(cls := "visited-at", formatVisitedAt(visit.visitedAt)),
      div(cls := "content", children := formatContent(text.content))
    )

    def formatContentIter(
        t: String,
        start: Int,
        iStart: List[Int],
        acc: List[Node]
    ): List[Node] =
      iStart match {
        case Nil => acc :+ formatOther(t.substring(start, t.size))
        case i :: tail =>
          formatContentIter(
            t,
            i + searchText.size,
            tail,
            acc ++ List(
              formatOther(t.substring(start, i)),
              formatMatch(t.substring(i, i + searchText.size))
            )
          )
      }

    def formatOther(t: String): Node =
      Html.text(t)

    def formatMatch(t: String): Node =
      span(cls := "match", t)

    def formatContent(content: String): List[Node] =
      val iStart: List[Int] = StringUtil.collectIndex(content, searchText)
      formatContentIter(content, 0, iStart, List.empty)

    def formatVisitedAt(at: LocalDateTime): String =
      KanjiDate.dateToKanji(
        at.toLocalDate,
        formatYoubi = info => s"（${info.youbi}）"
      ) + KanjiDate.timeToKanji(at.toLocalTime)

  case class NavUI( ) extends dev.fujiwara.domq.NavUI:
    val gotoFirstLink = None
    val gotoPrevLink = Some(a("<"))
    val gotoNextLink = Some(a(">"))
    val gotoLastLink = None
    val infoSpan = Some(span)
    val ele = div(gotoPrevLink.get, infoSpan.get, gotoNextLink.get)