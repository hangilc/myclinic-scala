package dev.myclinic.scala.web.practiceapp.practice.patientmanip

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.Text
import dev.myclinic.scala.model.Visit
import scala.concurrent.Future
import dev.myclinic.scala.webclient.{Api, global}
import dev.fujiwara.kanjidate.KanjiDate
import java.time.LocalDateTime
import dev.myclinic.scala.util.StringUtil

case class SearchTextDialog(patientId: Int):
  import SearchTextDialog.Item
  import dev.fujiwara.domq.SelectionModifier
  given SelectionModifier with
    override def itemCssClass: Option[String] = None
  val form = new SearchForm[(Text, Visit, String)](
    result => Item.apply.tupled(result).ele,
    text =>
      if text.trim.isEmpty then Future.successful(List.empty)
      else 
        for
          textVisits <- Api.searchTextForPatient(text.trim, patientId)
        yield textVisits.map {
          case (t, v) => (t, v, text)
        }
  )
  form.ui.form(button("閉じる", onclick := (close _)))
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
      div(cls := "content", innerHTML := mark(text.content))
    )

    def formatConetnt(content: String): String =
      val is = StringUtil.collectIndex(content, searchText)
      

    def mark(content: String): String =
      content
        .replaceAll(searchText, s"<span class='match'>$searchText</span>")
        .replaceAll("\n", "<br />")

    def formatVisitedAt(at: LocalDateTime): String =
      KanjiDate.dateToKanji(
        at.toLocalDate,
        formatYoubi = info => s"（${info.youbi}）"
      ) + KanjiDate.timeToKanji(at.toLocalTime)

  


