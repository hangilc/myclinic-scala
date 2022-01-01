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

class KouhiDisp(kouhi: Kouhi):
  val ele = Form.rows(
    span("負担者番号") -> div(kouhi.futansha.toString),
    span("受給者番号") -> div(kouhi.jukyuusha.toString),
    span("期限開始") -> div(KanjiDate.dateToKanji(kouhi.validFrom)),
    span("期限終了") -> div(kouhi.validUptoOption match {
      case Some(d) => KanjiDate.dateToKanji(d)
      case None => "（期限なし）"
    })
  )
  ele(cls := "kouhi-disp")




