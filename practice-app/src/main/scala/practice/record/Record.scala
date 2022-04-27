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
import dev.fujiwara.domq.CompAppendList
import dev.fujiwara.domq.CompAppendList.given
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.web.practiceapp.practice.PracticeBus

class Record(visitEx: VisitEx):
  val title = new Title(visitEx)
  val leftCol: HTMLElement = div(cls := "practice-visit-record-left")
  val texts: CompAppendList[Text] = CompAppendList[Text](leftCol)
  val ele = div(cls := "practice-visit")(
    title.ele,
    div(
      cls := "practice-visit-record",
      leftCol,
      composeRight(visitEx)
    )
  )

  val unsubs: List[LocalEventUnsubscriber] = List(
    PracticeBus.textEntered.subscribe(onTextEntered _)
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

  def onTextEntered(text: ModelText): Unit =
    val t = Text(text)
    texts.append(t)

object Record:
  given Ordering[Record] = Ordering.by[Record, Int](r => r.visitId).reverse
  given Comp[Record] = _.ele
  given Dispose[Record] =
    Dispose.nop[Record] + (_.title) + (_.texts) + (_.unsubs)

