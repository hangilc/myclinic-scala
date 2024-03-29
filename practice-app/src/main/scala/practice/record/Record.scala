package dev.myclinic.scala.web.practiceapp.practice.record

import dev.myclinic.scala.model.{
  Text as ModelText,
  Shinryou as ModelShinryou,
  Drug as ModelDrug_,
  Conduct as ModelConduct,
  Payment as ModelPayment,
  Hoken as ModelHoken,
  *
}
import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.CompAppendList
import dev.fujiwara.domq.CompAppendList.given
import dev.fujiwara.domq.CompSortList
import dev.fujiwara.domq.CompSortList.given
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.web.practiceapp.PracticeBus
import dev.myclinic.scala.web.practiceapp.practice.record.shinryou.ShinryouMenu
import dev.myclinic.scala.web.practiceapp.practice.record.conduct.ConductMenu
import dev.myclinic.scala.web.practiceapp.practice.record.conduct.Conduct
import dev.myclinic.scala.web.practiceapp.practice.record.payment.Payment
import java.time.LocalDate
import scala.language.implicitConversions

class Record(visitEx: VisitEx):
  val title = new Title(visitEx)
  val textsWrapper = div
  val hokenWrapper = div
  val shinryouWrapper = div
  val conductWrapper = div
  val payment = Payment(visitEx.chargeOption, visitEx.lastPayment, visitEx.visitId)
  val texts: CompAppendList[Text] = CompAppendList[Text](textsWrapper)
  val shinryouList: CompSortList[Shinryou] = CompSortList[Shinryou](shinryouWrapper)
  val conductList: CompAppendList[Conduct] = CompAppendList[Conduct](conductWrapper)
  val textMenu = TextMenu()
  textMenu.newText.subscribe(_ => doNewText())
  val ele = div(cls := "practice-visit", cls := (if isCurrent then Some("current") else None))(
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
  shinryouList.set(visitEx.shinryouList.map(s => Shinryou(s)))
  conductList.set(visitEx.conducts.map(c => Conduct(c)))

  private def isCurrent: Boolean =
    PracticeBus.currentVisitId == Some(visitId)

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

  def at: LocalDate = visitEx.visitedAt.toLocalDate
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
      new ShinryouMenu(visitEx.visitedAt.toLocalDate, visitId).ele,
      shinryouWrapper,
      new Drug(visitEx).ele,
      ConductMenu(at, visitId).ele,
      conductWrapper,
      payment.ele
    )

  def onTextEntered(text: ModelText): Unit =
    if text.visitId == visitId then
      val t = Text(text)
      texts.append(t)

  def onHokenInfoChanged(newHoken: HokenInfo): Unit =
    updateHoken(newHoken)
    
  def onShinryouEntered(entered: ShinryouEx): Unit =
    shinryouList.insert(Shinryou(entered))

  def onShinryouDeleted(deleted: ModelShinryou): Unit =
    shinryouList.remove(_.shinryou.shinryouId == deleted.shinryouId)

  def onConductEntered(entered: ConductEx): Unit =
    conductList.append(Conduct(entered))

  def onConductDeleted(deleted: ModelConduct): Unit =
    conductList.remove(c => c.conduct.conductId == deleted.conductId)

  def onChargeUpdated(updated: Charge): Unit =
    payment.onChargeUpdated(updated)

object Record:
  given Ordering[Record] = Ordering.by[Record, Int](r => r.visitId).reverse
  given Comp[Record] = _.ele
  given Dispose[Record] =
    Dispose.nop[Record] + (_.title) + (_.texts) + (_.payment) + (_.unsubs)
