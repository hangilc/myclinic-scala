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
import dev.myclinic.scala.apputil.DrugUtil
import dev.fujiwara.dateinput.ZenkakuUtil
import dev.myclinic.scala.{util => ju}
import dev.myclinic.scala.util.NumberUtil

class VisitBlock(visit: VisitEx):
  val eText: HTMLElement = div()
  val eHoken: HTMLElement = div()
  val eShinryou: HTMLElement = div()
  val eDrug: HTMLElement = div()
  val eCashier: HTMLElement = div()
  val ele: HTMLElement = div(cls := "visit-block")(
    div(cls := "visit-title")(formatVisitTime(visit.visitedAt)),
    div(cls := "visit-record")(
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
        eDrug.setChildren(drugElements(visit.drugs)),
        eCashier(chargeElement(visit.chargeOption))
      )
    )
  )

  def chargeElement(chargeOption: Option[Charge]): HTMLElement =
    chargeOption match {
      case None => div("（未請求）")
      case Some(charge) => 
        val txt = NumberUtil.withComma(charge.charge)
        div(s"請求額：${txt}円")
    }

  def drugElements(drugs: List[DrugEx]): List[HTMLElement] =
    if drugs.isEmpty then List.empty
    else
      val reps: List[String] = visit.drugs.map(drug => DrugUtil.drugRep(drug))
      val ords: List[Int] = (1 to reps.size).toList
      div("Ｒｐ）").ele ::
        ords.zip(reps).map { case (i, s) =>
          val ii = ZenkakuUtil.convertToZenkakuDigits(i.toString)
          div(s"${ii}）${s}").ele
        }

  def formatVisitTime(at: LocalDateTime): String =
    val p1 = KanjiDate.dateToKanji(
      at.toLocalDate,
      formatYoubi = info => s"（${info.youbi}）"
    )
    val p2 = KanjiDate.timeToKanji(at.toLocalTime)
    p1 + p2
