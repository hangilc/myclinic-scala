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
import dev.myclinic.scala.web.appbase.SyncedDataSource

class KoukikoureiDisp(ds: SyncedDataSource[Koukikourei]):
  def gen = ds.gen
  def koukikourei = ds.data
  val eHokenshaBangou = div
  val eHihokenshaBangou = div
  val eFutanwari = div
  val eValidFrom = div
  val eValidUpto = div
  val ele = Form.rows(
    span("保険者番号") -> eHokenshaBangou,
    span("被保険者番号") -> eHihokenshaBangou,
    span("負担割") -> eFutanwari,
    span("期限開始") -> eValidFrom,
    span("期限終了") -> eValidUpto
  )
  ele(cls := "koukikourei-disp")
  ds.onUpdate(updateUI _)
  updateUI()

  def updateUI(): Unit =
    eHokenshaBangou(innerText := koukikourei.hokenshaBangou.toString)
    eHihokenshaBangou(innerText := koukikourei.hihokenshaBangou.toString)
    eFutanwari(innerText := (koukikourei.futanWari.toString + "割"))
    eValidFrom(innerText := KanjiDate.dateToKanji(koukikourei.validFrom))
    eValidUpto(innerText := (koukikourei.validUptoOption match {
      case Some(d) => KanjiDate.dateToKanji(d)
      case None    => "（期限なし）"
    }))
