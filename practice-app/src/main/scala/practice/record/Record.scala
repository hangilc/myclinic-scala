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
  val textsWrapper = div
  val hokenWrapper = div
  val texts: CompAppendList[Text] = CompAppendList[Text](textsWrapper)
  val textMenu = TextMenu()
  textMenu.newText.subscribe(_ => doNewText())
  val ele = div(cls := "practice-visit")(
    title.ele,
    div(
      cls := "practice-visit-record",
      div(cls := "practice-visit-record-left")(
        textsWrapper,
        textMenu.ele
      ),
      composeRight(visitEx)
    )
  )
  texts.set(visitEx.texts.map(Text(_)))
  updateHoken(visitEx.hoken)

  def doNewText(): Unit =
    val editor = TextEnter(visitEx.visitId)
    editor.onDone.subscribe(_ => {
      editor.ele.remove()
      textMenu.ele(displayDefault)
    })
    textMenu.ele(displayNone)
    textMenu.ele.preInsert(editor.ele)

  val unsubs: List[LocalEventUnsubscriber] = List(
    PracticeBus.textEntered.subscribe(onTextEntered _)
  )

  def visitId: Int = visitEx.visitId

  def createHoken(hokenInfo: HokenInfo): Hoken = new Hoken(
    visitEx.visitId,
    visitEx.patientId,
    visitEx.visitedAt.toLocalDate,
    hokenInfo
  )

  def updateHoken(hoken: HokenInfo): Unit =
    hokenWrapper(clear, createHoken(hoken).ele)

  def composeRight(visitEx: VisitEx): HTMLElement =
    div(
      cls := "practice-visit-record-right",
      hokenWrapper,
      new ShinryouMenu().ele,
      new Shinryou(visitEx).ele,
      new Drug(visitEx).ele,
      new Conduct(visitEx).ele,
      new Payment(visitEx).ele
    )

  def onTextEntered(text: ModelText): Unit =
    val t = Text(text)
    texts.append(t)

  def onHokenInfoChanged(newHoken: HokenInfo): Unit =
    updateHoken(newHoken)

object Record:
  given Ordering[Record] = Ordering.by[Record, Int](r => r.visitId).reverse
  given Comp[Record] = _.ele
  given Dispose[Record] =
    Dispose.nop[Record] + (_.title) + (_.texts) + (_.unsubs)
