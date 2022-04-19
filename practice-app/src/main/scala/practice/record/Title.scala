package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}
import java.time.LocalDateTime
import dev.fujiwara.kanjidate.KanjiDate

class Title(at: LocalDateTime):
  import Title as Helper
  val ele = div(cls := "practice-visit-title",
    innerText := Helper.formatVisitTime(at)
  )


object Title:
  def formatVisitTime(at: LocalDateTime): String =
    val p1 = KanjiDate.dateToKanji(
      at.toLocalDate,
      formatYoubi = info => s"（${info.youbi}）"
    )
    val p2 = KanjiDate.timeToKanji(at.toLocalTime)
    p1 + p2

