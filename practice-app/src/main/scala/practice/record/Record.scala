package dev.myclinic.scala.web.practiceapp.practice.record

import dev.myclinic.scala.model.{
  Text as _,
  Shinryou as _,
  Drug as _,
  Conduct as _,
  Payment as _,
  *
}
import dev.fujiwara.domq.all.{*, given}
import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.TypeClasses.{Comp, Dispose}

class Record(visitEx: VisitEx):
  val title = new Title(visitEx)
  val ele = div(cls := "practice-visit")(
    title.ele,
    div(
      cls := "practice-visit-record",
      composeLeft(visitEx),
      composeRight(visitEx)
    )
  )

  def visitId: Int = visitEx.visitId

  def composeLeft(visitEx: VisitEx): HTMLElement =
    div(
      cls := "practice-visit-record-left",
      children := visitEx.texts.map(text => new Text(text).ele)
    )

  def composeRight(visitEx: VisitEx): HTMLElement =
    div(
      cls := "practice-visit-record-right",
      new Hoken(visitEx).ele,
      new Shinryou(visitEx).ele,
      new Drug(visitEx).ele,
      new Conduct(visitEx).ele,
      new Payment(visitEx).ele
    )

object Record:
  given Ordering[Record] = Ordering.by[Record, Int](r => r.visitId).reverse
  given Comp[Record] = _.ele
  given Dispose[Record] =
    rec =>
      summon[Dispose[Title]].dispose(rec.title)

