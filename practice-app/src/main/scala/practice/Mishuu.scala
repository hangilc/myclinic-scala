package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.*
import scala.language.implicitConversions

object Mishuu:
  val ele = div(
    displayNone,
    cls := "practice-right-widget",
    div(cls := "title", "未収リスト"),
    div(cls := "body")
  )


