package dev.myclinic.scala.web.practiceapp.practice.record.conduct

import dev.fujiwara.domq.all.{*, given}

case class XpWidget():
  val ele = div(
    div("Ｘ線検査入力"),
    div(
      select(
        option("胸部単純Ｘ線"),
        option("腹部単純Ｘ線")
      ),
      select(
        option("大角"),
        option("四ツ切")
      )
    ),
    div(
      button("入力"),
      button("キャンセル", onclick := (() => close))
    )
  )

  def close: Unit =
    ele.remove()