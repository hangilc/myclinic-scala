package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}
import java.time.LocalDateTime
import dev.fujiwara.kanjidate.KanjiDate
import org.scalajs.dom.HTMLElement

class Title(at: LocalDateTime):
  import Title as Helper
  val pullDown = PullDownLink("操作", 
    wrapperPostConstruct = (e => e(cls := "practice-visit-title-pulldown"))
  )
  pullDown.setBuilder(List(
    "この診察を削除" -> (() => ()),
    "暫定診察に設定" -> (() => ()),
    "暫定診察の解除" -> (() => ()),
    "診療明細" -> (() => ()),
    "負担割オーバーライド" -> (() => ()),
    "未収リストへ" -> (() => ())
  ))
  val ele = div(cls := "practice-visit-title",
    span(cls := "practice-visit-title-date", innerText := Helper.formatVisitTime(at)),
    pullDown.link(cls := "practice-visit-title-manip")
  )


object Title:
  def formatVisitTime(at: LocalDateTime): String =
    val p1 = KanjiDate.dateToKanji(
      at.toLocalDate,
      formatYoubi = info => s"（${info.youbi}）"
    )
    val p2 = KanjiDate.timeToKanji(at.toLocalTime)
    p1 + p2

