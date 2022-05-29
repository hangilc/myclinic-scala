package dev.myclinic.scala.web.practiceapp.practice.record.shinryou

import dev.fujiwara.domq.all.{*, given}

case class KensaDialog(config: Map[String, List[String]]):
  val dlog = new ModalDialog3()
  dlog.title("検査入力")
  dlog.commands(
    button("セット検査"),
    button("入力"),
    button("クリア"),
    button("キャンセル", onclick := (() => dlog.close()))
  )

  def open: Unit =
    dlog.open()

