package dev.myclinic.scala.web.practiceapp.practice.patientmanip

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.Text
import dev.myclinic.scala.model.Visit
import scala.concurrent.Future
import dev.myclinic.scala.webclient.{Api, global}
import dev.fujiwara.kanjidate.KanjiDate
import java.time.LocalDateTime

case class SearchTextDialog(patientId: Int):
  import SearchTextDialog.Item
  import dev.fujiwara.domq.SelectionModifier
  given SelectionModifier with
    override def itemCssClass: Option[String] = None
  val form = new SearchForm[(Text, Visit)](
    result => Item.apply.tupled(result).ele,
    text =>
      if text.trim.isEmpty then Future.successful(List.empty)
      else Api.searchTextForPatient(text.trim, patientId)
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
  case class Item(text: Text, visit: Visit):
    val ele = div(
      cls := "practice-search-text-for-patient-item",
      div(cls := "visited-at", formatVisitedAt(visit.visitedAt)),
      div(cls := "content", innerText := text.content)
    )

    def formatVisitedAt(at: LocalDateTime): String =
      KanjiDate.dateToKanji(
        at.toLocalDate,
        formatYoubi = info => s"（${info.youbi}）"
      ) + KanjiDate.timeToKanji(at.toLocalTime)
