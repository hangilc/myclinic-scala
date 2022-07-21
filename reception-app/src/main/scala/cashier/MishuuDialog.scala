package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.reception.selectpatientlink.SearchPatientBox

class MishuuDialog:
  val search = new SearchPatientBox(onSelect)
  val dlog = new ModalDialog3()
  dlog.title("未収処理（患者検索）")
  dlog.body(
    search.ele
  )
  dlog.commands(
    button("閉じる", onclick := (() => dlog.close()))
  )

  def open(): Unit = dlog.open()

  private def onSelect(patient: Patient): Unit =
    ???
