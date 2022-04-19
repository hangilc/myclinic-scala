package dev.myclinic.scala.web.practiceapp.practice.record

import dev.myclinic.scala.model.{Text as _, Shinryou as _, Drug as _, Conduct as _, *}
import dev.fujiwara.domq.all.{*, given}
import org.scalajs.dom.HTMLElement

class Record(visitId: Int):
  val ele = div(cls := "practice-visit")
  def init(visitEx: VisitEx): Unit =
    ele(
      new Title(visitEx.visitedAt).ele,
      div(cls := "practice-visit-record",
        composeLeft(visitEx),
        composeRight(visitEx)
      )
    )

  def composeLeft(visitEx: VisitEx): HTMLElement =
    div(cls := "practice-visit-record-left",
      children := visitEx.texts.map(text => 
        new Text(text).ele  
      )
    )

  def composeRight(visitEx: VisitEx): HTMLElement =
    div(cls := "practice-visit-record-right",
      new Hoken(visitEx).ele,
      new Shinryou(visitEx).ele,
      new Drug(visitEx).ele,
      new Conduct(visitEx).ele
    )

