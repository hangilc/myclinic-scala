package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.webclient.{Api, global}

class RegularDialog(
    leftNames: List[String],
    rightNames: List[String],
    bottomNames: List[String]
):
  val panel = RegularPanel(leftNames, rightNames, bottomNames)
  val dlog = new ModalDialog3
  dlog.title("診療行為入力")
  dlog.body(panel.ele)
  dlog.commands(
    button("入力", onclick := (onEnter _)),
    button("キャンセル", onclick := (_ => dlog.close()))
  )
  def open: Unit =
    dlog.open()

  def onEnter(): Unit =
    val names: List[String] = panel.selected
    println(names)
    dlog.close()
