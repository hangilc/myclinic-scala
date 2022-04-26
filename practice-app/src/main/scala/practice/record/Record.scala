package dev.myclinic.scala.web.practiceapp.practice.record

import dev.myclinic.scala.model.{
  Text as ModelText,
  Shinryou as _,
  Drug as _,
  Conduct as _,
  Payment as _,
  *
}
import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.CompList
import dev.fujiwara.domq.CompList.{Append, given}
import org.scalajs.dom.HTMLElement

class Record(visitEx: VisitEx):
  val title = new Title(visitEx)
  val leftCol: HTMLElement = div(cls := "practice-visit-record-left")
  given CompList.Append[HTMLElement] = Append(ele)
  val texts: CompList[Text] = CompList[Text]()
  val ele = div(cls := "practice-visit")(
    title.ele,
    div(
      cls := "practice-visit-record",
      leftCol,
      composeRight(visitEx)
    )
  )

  def visitId: Int = visitEx.visitId

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
    Dispose.nop[Record] + (_.title) + (_.texts)

