package dev.myclinic.scala.web.reception.records

import dev.myclinic.scala.model.*
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.kanjidate.KanjiDate
import dev.myclinic.scala.util.DateUtil
import java.time.{LocalDate, LocalDateTime}
import org.scalajs.dom.raw.{HTMLElement}
import dev.myclinic.scala.apputil.HokenUtil

class VisitBlock(visit: VisitEx):
  val eText: HTMLElement = div()
  val eHoken: HTMLElement = div()
  val eShinryou: HTMLElement = div()
  val eDrug: HTMLElement = div()
  val eCashier: HTMLElement = div()
  val ele: HTMLElement = div(cls := "visit-block")(
    div(formatVisitTime(visit.visitedAt)),
    div(cls := "two-columns")(
      div(cls := "left")(
        eText.setChildren(
          visit.texts.map(text => TextBlock(text).ele)
        )
      ),
      div(cls := "right")(
        eHoken(
          HokenUtil.hokenRep(visit)
        ),
        eShinryou.setChildren(
          visit.shinryouList.map(shinryou => div(shinryou.master.name))
        ),
        eDrug,
        eCashier
      )
    )
  )

  def formatVisitTime(at: LocalDateTime): String =
    val p1 = KanjiDate.dateToKanji(at.toLocalDate, formatYoubi = info => s"（${info.youbi}）")
    val p2 = KanjiDate.timeToKanji(at.toLocalTime)
    p1+p2
