package dev.myclinic.scala.web.appbase.patientdialog

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.{TransNode, TransNodeRuntime}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.appbase.PatientReps
import dev.myclinic.scala.apputil.HokenUtil
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.web.appbase.reception.PatientImagesDialog
import java.time.LocalDateTime
import scala.util.Success
import scala.util.Failure
import dev.myclinic.scala.web.appbase.PatientInputs
import Common.*
import org.scalajs.dom.HTMLElement

case class HokenHistoryNode(state: State) extends TransNode[State](state):
  override def init(): Unit =
    val dlog = state.dialog
    dlog.changeTitle("保険履歴")
    dlog.body(
      clear,
      div("Loading...")
    )
    dlog.commands(
      clear,
      button("戻る", onclick := (() => goBack()))
    )
    val op =
      for
        allHoken <- Api.listAllHoken(state.patient.patientId)
        (shahokokuhoList, koukikoureiList, roujinList, kouhiList) = allHoken
        allHokenIds = (
          shahokokuhoList.map(_.shahokokuhoId),
          koukikoureiList.map(_.koukikoureiId),
          roujinList.map(_.roujinId),
          kouhiList.map(_.kouhiId)
        )
        allHokenList = List.empty[
          Hoken
        ] ++ shahokokuhoList ++ koukikoureiList ++ roujinList ++ kouhiList
        countMaps <- Api.batchCountHokenUsage.tupled(allHokenIds)
      yield (allHokenList, countMaps)

    op.onComplete {
      case Failure(ex) =>
        ShowMessage.showError(ex.toString)
        goBack()
      case Success((allHoken, countMaps)) =>
        goReplacing(
          s => DoHokenHistoryNode(countMaps, s),
          state.copy(hokenList = allHoken)
        )
    }

case class DoHokenHistoryNode(
    countMaps: (Map[Int, Int], Map[Int, Int], Map[Int, Int], Map[Int, Int]),
    state: State
) extends TransNode[State](state):
  override def init(): Unit =
    import Hoken.*
    val dlog = state.dialog
    val onEdit: Hoken => Unit = hoken =>
      dispatchEditHoken(hoken, state.add(hoken))
    val onDelete: Hoken => Unit = hoken =>
      goReplacing(s => DoHokenHistoryNode(countMaps, s), state.remove(hoken))
    val boxWrapper = div
    val boxes = CompSortList[HokenBox](boxWrapper)
    val dispChoicePublisher = new LocalEventPublisher[(HokenKind, Boolean)]()
    dlog.changeTitle("保険履歴")
    boxes.set(
      state.hokenList.map(h => HokenBox(h, countMaps, onEdit, onDelete))
    )
    dlog.body(
      clear,
      patientBlock(state.patient),
      menu,
      boxWrapper,
      menu
    )
    dlog.commands(
      clear
    )
    dispChoicePublisher.subscribe(showKind.tupled)

    def showKind(kind: HokenKind, show: Boolean): Unit =
      boxes.list.foreach(b => if b.hokenKind == kind then b.ele.show(show))

    def menu: HTMLElement =
      val shahoCheck: CheckLabel[String] =
        CheckLabel[String]("shahokokuho", _("社保国保")).check
      val koukikoureiCheck: CheckLabel[String] =
        CheckLabel[String]("koukikourei", _("後期高齢")).check
      val kouhiCheck: CheckLabel[String] =
        CheckLabel[String]("kouhi", _("公費")).check
      shahoCheck.onChange(checked => {
        dispChoicePublisher.publish((HokenKind.ShahokokuhoKind, checked))
      })
      koukikoureiCheck.onChange(checked => {
        dispChoicePublisher.publish((HokenKind.KoukikoureiKind, checked))
      })
      kouhiCheck.onChange(checked => {
        dispChoicePublisher.publish((HokenKind.KouhiKind, checked))
      })
      dispChoicePublisher.subscribe { (kind, show) =>
        kind match {
          case HokenKind.ShahokokuhoKind => shahoCheck.check(show)
          case HokenKind.KoukikoureiKind => koukikoureiCheck.check(show)
          case HokenKind.KouhiKind       => kouhiCheck.check(show)
          case HokenKind.RoujinKind      => ()
        }
      }
      div(
        cls := "reception-hoken-box-menu",
        shahoCheck.wrap(span),
        koukikoureiCheck.wrap(span),
        kouhiCheck.wrap(span),
        button("戻る", onclick := (() => {
          for
            hokenList <- listCurrentHoken(state.patient.patientId)
          yield
            goBack(state.copy(hokenList = hokenList))
          ()
        }))
      )

  private def dispatchEditHoken(
      hoken: Hoken,
      state: State
  ): Unit =
    hoken match {
      case s: Shahokokuho => {
        goForward(state => EditShahokokuhoNode(s, state))
      }
      case k: Koukikourei => {
        goForward(state => EditKoukikoureiNode(k, state))
      }
      case k: Kouhi => {
        goForward(state => EditKouhiNode(k, state))
      }
      case _: Roujin => ()
    }
