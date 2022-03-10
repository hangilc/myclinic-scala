package dev.myclinic.scala.web.reception.patient

import dev.myclinic.scala.web.appbase.SyncedDataSource
import dev.myclinic.scala.model.Roujin
import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.apputil.EffectivePeriod.{repValidFrom, repValidUpto}

class RoujinDisp(ds: SyncedDataSource[Roujin]):
  def roujin = ds.data
  val form: TableForm = new TableForm
  val eShichouson = form.row("市町村番号")
  val eJukyuusha = form.row("受給者番号")
  val eFutanWari = form.row("負担割")
  val eValidFrom = form.row("期限開始")
  val eValidUpto = form.row("期限終了")
  ds.onUpdate(updateUI _)
  updateUI()
  
  def ele = form.ele

  def updateUI(): Unit =
    eShichouson(innerText := roujin.shichouson.toString)
    eJukyuusha(innerText := roujin.jukyuusha.toString)
    eFutanWari(innerText := s"${roujin.futanWari}割")
    eValidFrom(innerText := repValidFrom(roujin.validFrom))
    eValidUpto(innerText := repValidUpto(roujin.validUpto))
