package dev.myclinic.scala.practiceapp.practice.record.title

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.Meisai
import dev.myclinic.scala.practiceapp.practice.RcptUtil
import scala.language.implicitConversions

case class RcptDetailDialog(meisai: Meisai):
  val dlog = new ModalDialog3()
  dlog.title("診療明細")
  dlog.body(RcptUtil.rcptDetail(meisai))
  dlog.commands(
    button("閉じる", onclick := (() => dlog.close()))
  )
  println(meisai)

  def open(): Unit =
    dlog.open()
