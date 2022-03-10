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
import dev.myclinic.scala.apputil.EffectivePeriod.{repValidFrom, repValidUpto}

class KouhiDisp(ds: SyncedDataSource[Kouhi]):
  def gen = ds.gen
  def kouhi = ds.data
  val eFutansha = div
  val eJukyuusha = div
  val eValidFrom = div
  val eValidUpto = div
  val ele = Form.rows(
    span("負担者番号") -> eFutansha,
    span("受給者番号") -> eJukyuusha,
    span("期限開始") -> eValidFrom,
    span("期限終了") -> eValidUpto
  )
  ele(cls := "kouhi-disp")
  ds.onUpdate(updateUI _)
  updateUI()

  def updateUI(): Unit =
    eFutansha(innerText := kouhi.futansha.toString)
    eJukyuusha(innerText := kouhi.jukyuusha.toString)
    eValidFrom(innerText := repValidFrom(kouhi.validFrom))
    eValidUpto(innerText := repValidUpto(kouhi.validUpto))





