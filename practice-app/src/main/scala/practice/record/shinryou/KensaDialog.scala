package dev.myclinic.scala.web.practiceapp.practice.record.shinryou

import dev.fujiwara.domq.all.{*, given}

case class KensaDialog(config: Map[String, List[String]]):
  val panel = KensaPanel(config)
  val dlog = new ModalDialog3()
  dlog.title("検査入力")
  dlog.body(panel.ele)
  dlog.commands(
    button("セット検査", onclick := (() => panel.checkPreset)),
    button("入力"),
    button("クリア", onclick := (() => panel.clear)),
    button("キャンセル", onclick := (() => dlog.close()))
  )

  def open: Unit =
    dlog.open()

