package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form, ErrorBox, Modifier}
import scala.language.implicitConversions
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}
import dev.myclinic.scala.util.{KanjiDate, DateUtil}
import dev.myclinic.scala.model.*
import java.time.LocalDate

class KoukikoureiDisp(koukikourei: Koukikourei):
  val ele = Form.rows(
    span("保険者番号") -> div(koukikourei.hokenshaBangou.toString),
    span("被保険者") -> div(
      koukikourei.hihokenshaKigou,
      "・",
      koukikourei.hihokenshaBangou
    ),
    span("枝番") -> div(koukikourei.edaban),
    span("本人・家族") -> div(if koukikourei.isHonnin then "本人" else "家族"),
    span("高齢") -> span(koureiRep(koukikourei.koureiStore)),
    span("期限開始") -> div(KanjiDate.dateToKanji(koukikourei.validFrom)),
    span("期限終了") -> div(koukikourei.validUptoOption match {
      case Some(d) => KanjiDate.dateToKanji(d)
      case None => "（期限なし）"
    })
  )
  ele(cls := "koukikourei-disp")

  def koureiRep(kourei: Int): String = kourei match {
    case 0 => "高齢でない"
    case 2 => "２割"
    case 3 => "３割"
    case i => s"${i}割"
  }


