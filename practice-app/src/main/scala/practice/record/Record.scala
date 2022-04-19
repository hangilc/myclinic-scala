package dev.myclinic.scala.web.practiceapp.practice.record

import dev.myclinic.scala.model.*
import dev.fujiwara.domq.all.{*, given}
import org.scalajs.dom.HTMLElement

class Record(visitId: Int):
  val ele = div(cls := "practice-visit")
  def init(visitEx: VisitEx): Unit =
    ele(
      new Title(visitEx.visitedAt).ele,
      div(cls := "practice-visit-record",
        composeLeft,
        composeRight
      )
    )

  def composeLeft: HTMLElement =
    div(cls := "practice-visit-record-left", "LEFT")

  def composeRight: HTMLElement =
    div(cls := "practice-visit-record-right","RIGHT")

