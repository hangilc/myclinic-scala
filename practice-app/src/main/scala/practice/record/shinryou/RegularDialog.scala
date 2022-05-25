package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}

class RegularDialog():
  val dlog = new ModalDialog3
  dlog.title("診療行為入力")
  dlog.body(RegularPanel().ele)
  dlog.commands(
    button("入力"),
    button("キャンセル", onclick := (_ => dlog.close()))
  )
  def open: Unit =
    dlog.open()
