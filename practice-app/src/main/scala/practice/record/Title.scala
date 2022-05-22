package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}
import java.time.LocalDateTime
import dev.fujiwara.kanjidate.KanjiDate
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.model.VisitEx
import dev.myclinic.scala.web.practiceapp.practice.PracticeBus

class Title(visit: VisitEx):
  import Title as Helper
  def at: LocalDateTime = visit.visitedAt
  val unsubscribers = List(
    PracticeBus.tempVisitIdChanged.subscribe(adaptToTempVisitId _)
  )
  val pullDown = PullDownLink(
    "操作",
    wrapperPostConstruct = (e => e(cls := "practice-visit-title-pulldown"))
  )
  pullDown.setBuilder(
    List(
      "この診察を削除" -> (() => ()),
      "暫定診察に設定" -> (setTempVisitId _),
      "暫定診察の解除" -> (() => { PracticeBus.tempVisitIdChanged.publish(None); () }),
      "診療明細" -> (() => ()),
      "負担割オーバーライド" -> (() => ()),
      "未収リストへ" -> (() => ())
    )
  )
  val ele = div(
    cls := "practice-visit-title",
    span(
      cls := "practice-visit-title-date",
      innerText := Helper.formatVisitTime(at)
    ),
    pullDown.link(cls := "practice-visit-title-manip")
  )
  adaptToTempVisitId(PracticeBus.currentTempVisitId)

  def setTempVisitId(): Unit =
    if PracticeBus.currentVisitId.isEmpty then
      PracticeBus.tempVisitIdChanged.publish(Some(visit.visitId))

  def adaptToTempVisitId(optTempVisitId: Option[Int]): Unit =
    optTempVisitId match {
      case None => ele(cls :- "temp-visit")
      case Some(visitId) =>
        if visitId == visit.visitId then ele(cls := "temp-visit")
        else ele(cls :- "temp-visit")
    }


object Title:
  def formatVisitTime(at: LocalDateTime): String =
    val p1 = KanjiDate.dateToKanji(
      at.toLocalDate,
      formatYoubi = info => s"（${info.youbi}）"
    )
    val p2 = KanjiDate.timeToKanji(at.toLocalTime)
    p1 + p2

  given Dispose[Title] = 
    Dispose.nop[Title] + (_.unsubscribers)