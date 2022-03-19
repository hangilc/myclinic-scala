package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form, ErrorBox, Modifier}
import scala.language.implicitConversions
import org.scalajs.dom.{HTMLElement, HTMLInputElement}
import dev.myclinic.scala.util.DateUtil
import dev.myclinic.scala.model.*
import java.time.LocalDate
import dev.fujiwara.kanjidate.KanjiDate
import dev.myclinic.scala.web.appbase.SyncedDataSource

class ShahokokuhoDisp(ds: SyncedDataSource[Shahokokuho]):
  def gen = ds.gen
  def shahokokuho = ds.data
  val eHokenshaBangou = div
  val eHihokensha = div
  val eEdaban = div
  val eHonnin = div
  val eKourei = div
  val eValidFrom = div
  val eValidUpto = div
  val ele = Form.rows(
    span("保険者番号") -> eHokenshaBangou,
    span("被保険者") -> eHihokensha,
    span("枝番") -> eEdaban,
    span("本人・家族") -> eHonnin,
    span("高齢") -> eKourei,
    span("期限開始") -> eValidFrom,
    span("期限終了") -> eValidUpto
  )
  ele(cls := "shahokokuho-disp")
  ds.onUpdate(_ => updateUI())
  updateUI()

  def updateUI(): Unit =
    eHokenshaBangou(innerText := shahokokuho.hokenshaBangou.toString)
    eHihokensha(clear, 
      shahokokuho.hihokenshaKigou,
      "・",
      shahokokuho.hihokenshaBangou
    )
    eEdaban(innerText := shahokokuho.edaban)
    eHonnin(innerText := (if shahokokuho.isHonnin then "本人" else "家族"))
    eKourei(innerText := koureiRep(shahokokuho.koureiStore))
    eValidFrom(innerText := KanjiDate.dateToKanji(shahokokuho.validFrom))
    eValidUpto(innerText := (shahokokuho.validUptoOption match {
      case Some(d) => KanjiDate.dateToKanji(d)
      case None => "（期限なし）"
    }))

  def koureiRep(kourei: Int): String = kourei match {
    case 0 => "高齢でない"
    case 2 => "２割"
    case 3 => "３割"
    case i => s"${i}割"
  }
