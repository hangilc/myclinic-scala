package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}

case class RegularPanel():
  val left = div
  val right = div
  val bottom = div
  val ele = div(cls := "practice-shinryou-regular-panel",
    left(cls := "practice-shinryou-regular-panel-left"),
    right(cls := "practice-shinryou-regular-panel-right"),
    bottom(cls := "practice-shinryou-regular-panel-bottom")
  )
