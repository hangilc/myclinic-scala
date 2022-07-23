package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.reception.selectpatientlink.SearchPatientBox
import java.time.LocalDate
import dev.myclinic.scala.webclient.{Api, global}
class MishuuDialog:
  val search = new SearchPatientBox(onSelect)
  val enter = button
  val dlog = new ModalDialog3()
  dlog.title("未収処理（患者検索）")
  dlog.body(
    search.ele
  )
  dlog.commands(
    enter("選択", disabled := true, onclick := (onEnter _)),
    button("閉じる", onclick := (() => dlog.close()))
  )

  def initFocus(): Unit = search.initFocus()

  def open(): Unit = 
    dlog.open()
    initFocus()

  private def onSelect(patient: Patient): Unit =
    enter(disabled := false)

  private def onEnter(): Unit =
    search.selection.marked.foreach(patient => 
      val since = LocalDate.now().minusYears(1)
      for
        visits <- Api.listVisitSince(patient.patientId, since)
        chargePays <- Api.batchGetChargePayment(visits.map(_.visitId))
      yield 
        println(visits)
        print(chargePays)
      ()
    )
