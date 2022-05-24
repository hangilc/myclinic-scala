package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.apputil.HokenUtil
import dev.myclinic.scala.model.VisitEx
import dev.myclinic.scala.web.practiceapp.practice.record.hoken.{Disp, EditDialog}
import dev.myclinic.scala.model.HokenInfo
import dev.myclinic.scala.webclient.{Api, global}
import java.time.LocalDate


class Hoken(visitId: Int, patientId: Int, date: LocalDate, hoken: HokenInfo):
  val ele = div
  disp

  def disp: Unit =
    val d = Disp(hoken)
    d.onClick.subscribe(_ => edit)
    ele(d.ele)

  def edit: Unit =
    for
      shahoOpt <- Api.findAvailableShahokokuho(patientId, date)
      koukikoureiOpt <- Api.findAvailableKoukikourei(patientId, date)
      roujinOpt <- Api.findAvailableRoujin(patientId, date)
      kouhiList <- Api.listAvailableKouhi(patientId, date)
    yield 
      val dlog = EditDialog(shahoOpt, koukikoureiOpt, roujinOpt, kouhiList, hoken, visitId)
      dlog.open