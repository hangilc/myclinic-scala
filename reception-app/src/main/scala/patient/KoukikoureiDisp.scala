package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form, ErrorBox, Modifier}
import scala.language.implicitConversions
import org.scalajs.dom.{HTMLElement, HTMLInputElement}
import dev.myclinic.scala.util.DateUtil
import dev.fujiwara.kanjidate.KanjiDate
import dev.myclinic.scala.model.*
import java.time.LocalDate

class KoukikoureiDisp(koukikourei: Koukikourei):
  val ele = Form.rows(
    span("保険者番号") -> div(koukikourei.hokenshaBangou.toString),
    span("被保険者番号") -> div(koukikourei.hihokenshaBangou.toString),
    span("負担割") -> div(koukikourei.futanWari.toString + "割"),
    span("期限開始") -> div(KanjiDate.dateToKanji(koukikourei.validFrom)),
    span("期限終了") -> div(koukikourei.validUptoOption match {
      case Some(d) => KanjiDate.dateToKanji(d)
      case None => "（期限なし）"
    })
  )
  ele(cls := "koukikourei-disp")



