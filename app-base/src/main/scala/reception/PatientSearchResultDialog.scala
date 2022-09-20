package dev.myclinic.scala.web.appbase.reception

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.transition.*
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.myclinic.scala.webclient.{Api, global}
import java.time.LocalDateTime
import java.time.LocalDate
import scala.concurrent.Future
import dev.myclinic.scala.apputil.HokenUtil
import dev.myclinic.scala.web.appbase.*
import org.scalajs.dom.HTMLElement
import scala.util.Success
import scala.util.Failure
import dev.myclinic.scala.web.appbase.patientdialog.PatientDialog
import dev.myclinic.scala.web.appbase.patientdialog.HokenBox

case class PatientSearchResultDialog(patients: List[Patient]):
  val dlog = new ModalDialog3()
  val selection = Selection.make[Patient](patients, p => div(format(p)))
  selection.onSelect(patient => start(patient))
  dlog.content(cls := "reception-cashier-search-patient-result-dialog")
  dlog.title("患者検索結果")
  dlog.body(
    cls := "search-result-mode",
    selection.ele(cls := "selection")
  )
  dlog.commands(
    button("閉じる", onclick := (() => dlog.close()))
  )

  def start(patient: Patient): Unit =
    dlog.close()
    PatientDialog.open(patient)

  def open(): Unit =
    dlog.open()

  def format(patient: Patient): String =
    String.format(
      "(%04d) %s %s",
      patient.patientId,
      patient.lastName,
      patient.firstName
    )

  // case class State(patient: Patient, hokenList: List[Hoken]):
  //   import Hoken.*
  //   def getShahokokuho(shahokokuhoId: Int): Option[Shahokokuho] =
  //     hokenList
  //       .filter(_.isShahokokuho)
  //       .map(_.tryCastAsShahokokuho.get)
  //       .find(_.shahokokuhoId == shahokokuhoId)
  //   def getKoukikourei(koukikoureiId: Int): Option[Koukikourei] =
  //     hokenList
  //       .filter(_.isKoukikourei)
  //       .map(_.tryCastAsKoukikourei.get)
  //       .find(_.koukikoureiId == koukikoureiId)
  //   def getRoujin(roujinId: Int): Option[Roujin] =
  //     hokenList
  //       .filter(_.isRoujin)
  //       .map(_.tryCastAsRoujin.get)
  //       .find(_.roujinId == roujinId)
  //   def getKouhi(kouhiId: Int): Option[Kouhi] =
  //     hokenList
  //       .filter(_.isKouhi)
  //       .map(_.tryCastAsKouhi.get)
  //       .find(_.kouhiId == kouhiId)
  //   def add(hoken: Hoken): State =
  //     val hokenId = HokenId(hoken)
  //     val (pre, post) = hokenList.span(h => HokenId(h) != hokenId)
  //     val newList =
  //       if post.isEmpty then pre :+ hoken
  //       else (pre :+ hoken) ++ post.tail
  //     copy(hokenList = newList)
  //   def remove(hoken: Hoken): State =
  //     println(("removing", hoken))
  //     val hokenId = HokenId(hoken)
  //     copy(hokenList = hokenList.filter(h => HokenId(h) != hokenId))

  // type DlogFun = TransitionNode[State]

  // def start(patient: Patient, f: DlogFun): Unit =
  //   for hokenList <- listHoken(patient.patientId)
  //   yield
  //     val state = State(patient, hokenList)
  //     Transition.run(f, state, List.empty, () => dlog.close())

  // private def disp(state: State, next: Transition[State] => Unit): Unit =
  //   import Hoken.*
  //   doDisp(
  //     state.copy(
  //       hokenList = state.hokenList.filter(_.isValidAt(LocalDate.now()))
  //     ),
  //     next
  //   )

  // private def doDisp(state: State, next: Transition[State] => Unit): Unit =
  //   val hokenArea = div
  //   val dispElement = new PatientReps(Some(state.patient)).dispPanel
  //   dlog.title(clear, "患者情報")
  //   dlog.body(
  //     clear,
  //     div(
  //       cls := "reception-cashier-patient-search-result-dialog-disp-body",
  //       dispElement,
  //       hokenArea(
  //         cls := "hoken-area",
  //         state.hokenList.map(h => {
  //           a(
  //             HokenUtil.hokenRep(h),
  //             onclick := (() => dispatchDispHoken(h, state, next))
  //           )
  //         })
  //       )
  //     )
  //   )
  //   dlog.commands(
  //     clear,
  //     div(
  //       button(
  //         "診察受付",
  //         onclick := (() => next(GoForward(doRegister, state)))
  //       ),
  //       button("閉じる", onclick := (() => next(Exit())))
  //     ),
  //     div(
  //       cls := "domq-mt-4 reception-cashier-patient-search-result-dialog-disp-link-commands",
  //       a("編集", onclick := (() => next(GoForward(editPatient, state)))),
  //       "|",
  //       a(
  //         "新規社保国保",
  //         onclick := (() =>
  //           next(GoForward(newShahokokuho(new ShahokokuhoInputs(None)), state))
  //         )
  //       ),
  //       "|",
  //       a(
  //         "新規後期高齢",
  //         onclick := (() =>
  //           for
  //             inputs <- createKoukikoureiInputs(None)
  //           yield
  //             next(GoForward(newKoukikourei(inputs), state))
  //           ()
  //         )
  //       ),
  //       "|",
  //       a(
  //         "新規公費",
  //         onclick := (() =>
  //           next(GoForward(newKouhi(new KouhiInputs(None)), state))
  //         )
  //       ),
  //       "|",
  //       a("保険履歴", onclick := (() => next(GoForward(hokenHistory, state)))),
  //       "|",
  //       a("保存画像", onclick := (() => PatientImagesDialog.open(state.patient)))
  //     )
  //   )

  // private def dispatchDispHoken(
  //     hoken: Hoken,
  //     state: State,
  //     next: Transition[State] => Unit
  // ): Unit =
  //   hoken match {
  //     case s: Shahokokuho =>
  //       next(
  //         GoForward(dispShahokokuho(s.shahokokuhoId), state)
  //       )
  //     case k: Koukikourei =>
  //       next(
  //         GoForward(dispKoukikourei(k.koukikoureiId), state)
  //       )
  //     case k: Kouhi =>
  //       next(GoForward(dispKouhi(k.kouhiId), state))
  //     case r: Roujin =>
  //       next(GoForward(dispRoujin(r.roujinId), state))
  //   }

  // private def dispatchEditHoken(
  //   hoken: Hoken,
  //   state: State,
  //   next: Transition[State] => Unit
  // ): Unit =
  //   hoken match {
  //     case s: Shahokokuho =>
  //       next(
  //         GoForward(editShahokokuho(s.shahokokuhoId), state)
  //       )
  //     case k: Koukikourei =>
  //       next(
  //         GoForward(editKoukikourei(k.koukikoureiId), state)
  //       )
  //     case k: Kouhi =>
  //       next(GoForward(editKouhi(k.kouhiId), state))
  //     case r: Roujin => ()
  //   }

  // private def listHoken(patientId: Int): Future[List[Hoken]] =
  //   for
  //     result <- Api.getPatientHoken(patientId, LocalDate.now())
  //     (_, _, shahokokuho, koukikourei, roujin, kouhi) = result
  //   yield List.empty[Hoken] ++ shahokokuho ++ koukikourei ++ roujin ++ kouhi

  // private def hokenHistory(state: State, next: Transition[State] => Unit): Unit =
  //   dlog.title(clear, "保険履歴")
  //   dlog.body(
  //     clear,
  //     div("Loading...")
  //   )
  //   dlog.commands(
  //     clear,
  //     button("戻る", onclick := (() => next(GoBack(state))))
  //   )
  //   val op =
  //     for
  //       allHoken <- Api.listAllHoken(state.patient.patientId)
  //       (shahokokuhoList, koukikoureiList, roujinList, kouhiList) = allHoken
  //       allHokenIds = (
  //         shahokokuhoList.map(_.shahokokuhoId),
  //         koukikoureiList.map(_.koukikoureiId),
  //         roujinList.map(_.roujinId),
  //         kouhiList.map(_.kouhiId)
  //       )
  //       allHokenList = List.empty[
  //         Hoken
  //       ] ++ shahokokuhoList ++ koukikoureiList ++ roujinList ++ kouhiList
  //       countMaps <- Api.batchCountHokenUsage.tupled(allHokenIds)
  //     yield (allHokenList, countMaps)

  //   op.onComplete {
  //     case Failure(ex) =>
  //       ShowMessage.showError(ex.toString)
  //       next(GoBack(state))
  //     case Success((allHoken, countMaps)) =>
  //       next(GoTo(doHokenHistory(countMaps), state.copy(hokenList = allHoken)))
  //   }

  // private def doHokenHistory(
  //     countMaps: (Map[Int, Int], Map[Int, Int], Map[Int, Int], Map[Int, Int])
  // )(
  //     state: State,
  //     next: Transition[State] => Unit
  // ): Unit =
  //   import Hoken.*
  //   val onEdit: Hoken => Unit = hoken => dispatchEditHoken(hoken, state.add(hoken), next)
  //   val onDelete: Hoken => Unit = hoken => next(GoTo(doHokenHistory(countMaps), state.remove(hoken)))
  //   val boxWrapper = div
  //   val boxes = CompSortList[HokenBox](boxWrapper)
  //   val dispChoicePublisher = new LocalEventPublisher[(HokenKind, Boolean)]()
  //   dlog.title(clear, "保険履歴")
  //   boxes.set(state.hokenList.map(h => HokenBox(h, countMaps, onEdit, onDelete)))
  //   dlog.body(
  //     clear,
  //     patientBlock(state.patient),
  //     menu,
  //     boxWrapper,
  //     menu
  //   )
  //   dlog.commands(
  //     clear
  //   )
  //   dispChoicePublisher.subscribe(showKind.tupled)

  //   def showKind(kind: HokenKind, show: Boolean): Unit =
  //     boxes.list.foreach(b => if b.hokenKind == kind then b.ele.show(show))

  //   def menu: HTMLElement =
  //     val shahoCheck: CheckLabel[String] = CheckLabel[String]("shahokokuho", _("社保国保")).check
  //     val koukikoureiCheck: CheckLabel[String] = CheckLabel[String]("koukikourei", _("後期高齢")).check
  //     val kouhiCheck: CheckLabel[String] = CheckLabel[String]("kouhi", _("公費")).check
  //     shahoCheck.onChange(checked => {
  //       dispChoicePublisher.publish((HokenKind.ShahokokuhoKind, checked))
  //     })
  //     koukikoureiCheck.onChange(checked => {
  //       dispChoicePublisher.publish((HokenKind.KoukikoureiKind, checked))
  //     })
  //     kouhiCheck.onChange(checked => {
  //       dispChoicePublisher.publish((HokenKind.KouhiKind, checked))
  //     })
  //     dispChoicePublisher.subscribe {
  //       (kind, show) => kind match {
  //         case HokenKind.ShahokokuhoKind => shahoCheck.check(show)
  //         case HokenKind.KoukikoureiKind => koukikoureiCheck.check(show)
  //         case HokenKind.KouhiKind => kouhiCheck.check(show)
  //         case HokenKind.RoujinKind => ()
  //       }
  //     }
  //     div(
  //       cls := "reception-hoken-box-menu",
  //       shahoCheck.wrap(span),
  //       koukikoureiCheck.wrap(span),
  //       kouhiCheck.wrap(span),
  //       button("戻る", onclick := (() => next(GoBack(state))))
  //     )

  // private def dispShahokokuho(
  //     shahokokuhoId: Int
  // )(state: State, next: Transition[State] => Unit): Unit =
  //   state.getShahokokuho(shahokokuhoId) match {
  //     case None    => next(GoBack(state))
  //     case Some(h) => doDispShahokokuho(h, state, next)
  //   }

  // private def doDispShahokokuho(
  //     shahokokuho: Shahokokuho,
  //     state: State,
  //     next: Transition[State] => Unit
  // ): Unit =
  //   val panel = new ShahokokuhoReps(Some(shahokokuho)).dispPanel
  //   val renewButton = button
  //   dlog.title(clear, "社保国保")
  //   dlog.body(
  //     clear,
  //     patientBlock(state.patient),
  //     panel
  //   )
  //   dlog.commands(
  //     clear,
  //     renewButton(
  //       "保険更新",
  //       displayNone,
  //       onclick := (() =>
  //         shahokokuho.validUpto.value.foreach(d => {
  //           val inputs = new ShahokokuhoInputs(
  //             Some(
  //               shahokokuho.copy(
  //                 shahokokuhoId = 0,
  //                 validFrom = d.plusDays(1),
  //                 validUpto = ValidUpto(None)
  //               )
  //             )
  //           )
  //           next(
  //             GoTo(
  //               newShahokokuho(
  //                 inputs,
  //                 state =>
  //                   GoTo(dispShahokokuho(shahokokuho.shahokokuhoId), state)
  //               ),
  //               state
  //             )
  //           )
  //         })
  //       )
  //     ),
  //     button(
  //       "編集",
  //       onclick := (() =>
  //         next(GoForward(editShahokokuho(shahokokuho.shahokokuhoId), state))
  //       )
  //     ),
  //     button("戻る", onclick := (() => next(GoBack(state))))
  //   )
  //   renewButton.show(shahokokuho.validUpto.value.isDefined)

  // private def editShahokokuho(
  //     shahokokuhoId: Int
  // )(state: State, next: Transition[State] => Unit): Unit =
  //   state.getShahokokuho(shahokokuhoId) match {
  //     case None    => next(GoBack(state))
  //     case Some(h) => doEditShahokokuho(h, state, next)
  //   }

  // private def doEditShahokokuho(
  //     shahokokuho: Shahokokuho,
  //     state: State,
  //     next: Transition[State] => Unit
  // ): Unit =
  //   val inputs = new ShahokokuhoInputs(Some(shahokokuho))
  //   val errBox = ErrorBox()
  //   dlog.title(clear, "社保国保編集")
  //   dlog.body(
  //     clear,
  //     patientBlock(state.patient),
  //     inputs.formPanel,
  //     errBox.ele
  //   )
  //   dlog.commands(
  //     clear,
  //     button(
  //       "入力",
  //       onclick := (() =>
  //         inputs.validateForUpdate() match {
  //           case Left(msg) => errBox.show(msg)
  //           case Right(newShahokokuho) =>
  //             for
  //               _ <- Api.updateShahokokuho(newShahokokuho)
  //               updated <- Api.getShahokokuho(shahokokuho.shahokokuhoId)
  //             yield next(GoBack(state.add(updated)))
  //             ()
  //         }
  //       )
  //     ),
  //     button("キャンセル", onclick := (() => next(GoBack(state))))
  //   )

  // private def dispKoukikourei(
  //     koukikoureiId: Int
  // )(state: State, next: Transition[State] => Unit): Unit =
  //   state.getKoukikourei(koukikoureiId) match {
  //     case Some(k) => doDispKoukikourei(k, state, next)
  //     case None    => next(GoBack(state))
  //   }

  // private def doDispKoukikourei(
  //     koukikourei: Koukikourei,
  //     state: State,
  //     next: Transition[State] => Unit
  // ): Unit =
  //   val panel: HTMLElement = new KoukikoureiReps(Some(koukikourei)).dispPanel
  //   val renewButton = button
  //   dlog.title(clear, "後期高齢")
  //   dlog.body(
  //     clear,
  //     patientBlock(state.patient),
  //     panel
  //   )
  //   dlog.commands(
  //     clear,
  //     renewButton(
  //       "保険更新",
  //       displayNone,
  //       onclick := (() =>
  //         koukikourei.validUpto.value.foreach(d => {
  //           val inputs = new KoukikoureiInputs(
  //             Some(
  //               koukikourei.copy(
  //                 koukikoureiId = 0,
  //                 validFrom = d.plusDays(1),
  //                 validUpto = ValidUpto(None)
  //               )
  //             )
  //           )
  //           next(
  //             GoTo(
  //               newKoukikourei(
  //                 inputs,
  //                 state =>
  //                   GoTo(dispKoukikourei(koukikourei.koukikoureiId), state)
  //               ),
  //               state
  //             )
  //           )
  //         })
  //       )
  //     ),
  //     button(
  //       "編集",
  //       onclick := (() =>
  //         next(GoForward(editKoukikourei(koukikourei.koukikoureiId), state))
  //       )
  //     ),
  //     button("戻る", onclick := (() => next(GoBack(state))))
  //   )
  //   renewButton.show(koukikourei.validUpto.value.isDefined)

  // private def editKoukikourei(
  //     koukikoureiId: Int
  // )(state: State, next: Transition[State] => Unit): Unit =
  //   state.getKoukikourei(koukikoureiId) match {
  //     case Some(k) => doEditKoukikourei(k, state, next)
  //     case None    => next(GoBack(state))
  //   }

  // private def doEditKoukikourei(
  //     koukikourei: Koukikourei,
  //     state: State,
  //     next: Transition[State] => Unit
  // ): Unit =
  //   val inputs = new KoukikoureiInputs(Some(koukikourei))
  //   val errBox = ErrorBox()
  //   dlog.title(clear, "後期高齢編集")
  //   dlog.body(
  //     clear,
  //     patientBlock(state.patient),
  //     inputs.formPanel,
  //     errBox.ele
  //   )
  //   dlog.commands(
  //     clear,
  //     button(
  //       "入力",
  //       onclick := (() =>
  //         inputs.validateForUpdate() match {
  //           case Right(formKoukikourei) =>
  //             for
  //               _ <- Api.updateKoukikourei(formKoukikourei)
  //               updated <- Api.getKoukikourei(koukikourei.koukikoureiId)
  //             yield next(GoBack(state.add(updated)))
  //           case Left(msg) => errBox.show(msg)
  //         }
  //         ()
  //       )
  //     ),
  //     button("戻る", onclick := (() => next(GoBack(state))))
  //   )

  // private def dispKouhi(
  //     kouhiId: Int
  // )(state: State, next: Transition[State] => Unit): Unit =
  //   state.getKouhi(kouhiId) match {
  //     case Some(k) => doDispKouhi(k, state, next)
  //     case None    => next(GoBack(state))
  //   }

  // private def doDispKouhi(
  //     kouhi: Kouhi,
  //     state: State,
  //     next: Transition[State] => Unit
  // ): Unit =
  //   val panel: HTMLElement = new KouhiReps(Some(kouhi)).dispPanel
  //   val renewButton = button
  //   dlog.title(clear, "公費")
  //   dlog.body(
  //     clear,
  //     patientBlock(state.patient),
  //     panel
  //   )
  //   dlog.commands(
  //     clear,
  //     renewButton(
  //       "保険更新",
  //       displayNone,
  //       onclick := (() =>
  //         kouhi.validUpto.value.foreach(d => {
  //           val inputs = new KouhiInputs(
  //             Some(
  //               kouhi.copy(
  //                 kouhiId = 0,
  //                 validFrom = d.plusDays(1),
  //                 validUpto = ValidUpto(None)
  //               )
  //             )
  //           )
  //           next(
  //             GoTo(
  //               newKouhi(
  //                 inputs,
  //                 state => GoTo(dispKouhi(kouhi.kouhiId), state)
  //               ),
  //               state
  //             )
  //           )
  //         })
  //       )
  //     ),
  //     button(
  //       "編集",
  //       onclick := (() => next(GoForward(editKouhi(kouhi.kouhiId), state)))
  //     ),
  //     button("戻る", onclick := (() => next(GoBack(state))))
  //   )
  //   renewButton.show(kouhi.validUpto.value.isDefined)

  // private def editKouhi(
  //     kouhiId: Int
  // )(state: State, next: Transition[State] => Unit): Unit =
  //   state.getKouhi(kouhiId) match {
  //     case Some(k) => doEditKouhi(k, state, next)
  //     case None    => next(GoBack(state))
  //   }

  // private def doEditKouhi(
  //     kouhi: Kouhi,
  //     state: State,
  //     next: Transition[State] => Unit
  // ): Unit =
  //   val inputs = new KouhiInputs(Some(kouhi))
  //   val errBox = ErrorBox()
  //   dlog.title(clear, "公費")
  //   dlog.body(
  //     clear,
  //     patientBlock(state.patient),
  //     inputs.formPanel,
  //     errBox.ele
  //   )
  //   dlog.commands(
  //     clear,
  //     button(
  //       "入力",
  //       onclick := (() =>
  //         inputs.validateForUpdate() match {
  //           case Right(formKouhi) =>
  //             for
  //               _ <- Api.updateKouhi(formKouhi)
  //               updated <- Api.getKouhi(kouhi.kouhiId)
  //             yield next(GoBack(state.add(updated)))
  //           case Left(msg) => errBox.show(msg)
  //         }
  //         ()
  //       )
  //     ),
  //     button("戻る", onclick := (() => next(GoBack(state))))
  //   )

  // private def dispRoujin(
  //     roujinId: Int
  // )(state: State, next: Transition[State] => Unit): Unit =
  //   val roujin: Roujin = state.getRoujin(roujinId).get
  //   val panel: HTMLElement = new RoujinReps(Some(roujin)).dispPanel
  //   dlog.title(clear, "老人保健")
  //   dlog.body(
  //     clear,
  //     patientBlock(state.patient),
  //     panel
  //   )
  //   dlog.commands(
  //     clear,
  //     button("戻る", onclick := (() => next(GoBack(state))))
  //   )

  // private def patientBlock(patient: Patient): HTMLElement =
  //   div(
  //     innerText := s"(${patient.patientId}) ${patient.lastName} ${patient.firstName}",
  //     cls := "patient-block"
  //   )

  // private def newShahokokuho(
  //     inputs: ShahokokuhoInputs,
  //     cancel: State => Transition[State] = GoBack(_)
  // )(state: State, next: Transition[State] => Unit): Unit =
  //   val errBox = ErrorBox()
  //   dlog.title(clear, "新規社保国保")
  //   dlog.body(
  //     clear,
  //     patientBlock(state.patient),
  //     inputs.formPanel,
  //     errBox.ele
  //   )
  //   dlog.commands(
  //     clear,
  //     button(
  //       "入力",
  //       onclick := (() =>
  //         inputs.validateForEnter(state.patient.patientId) match {
  //           case Left(msg) => errBox.show(msg)
  //           case Right(shahokokuho) =>
  //             for entered <- Api.enterShahokokuho(shahokokuho)
  //             yield next(GoBack(state.add(entered)))
  //         }
  //         ()
  //       )
  //     ),
  //     button("キャンセル", onclick := (() => next(cancel(state))))
  //   )

  // private def createKoukikoureiInputs(modelOpt: Option[Koukikourei]): Future[KoukikoureiInputs] =
  //   for
  //     bangou <- Api.defaultKoukikoureiHokenshaBangou()
  //   yield 
  //     new KoukikoureiInputs(modelOpt):
  //       override def defaultKoukikoureiHokenshaBangou: String = bangou.toString

  // private def newKoukikourei(
  //     inputs: KoukikoureiInputs,
  //     cancel: State => Transition[State] = GoBack(_)
  // )(state: State, next: Transition[State] => Unit): Unit =
  //   val errBox = ErrorBox()
  //   dlog.title(clear, "後期高齢入力")
  //   dlog.body(
  //     clear,
  //     patientBlock(state.patient),
  //     inputs.formPanel,
  //     errBox.ele
  //   )
  //   dlog.commands(
  //     clear,
  //     button(
  //       "入力",
  //       onclick := (() =>
  //         inputs.validateForEnter(state.patient.patientId) match {
  //           case Left(msg) => errBox.show(msg)
  //           case Right(formKoukikourei) =>
  //             for entered <- Api.enterKoukikourei(formKoukikourei)
  //             yield next(GoBack(state.add(entered)))
  //         }
  //         ()
  //       )
  //     ),
  //     button("キャンセル", onclick := (() => next(cancel(state))))
  //   )

  // private def newKouhi(
  //     inputs: KouhiInputs,
  //     cancel: State => Transition[State] = GoBack(_)
  // )(state: State, next: Transition[State] => Unit): Unit =
  //   val errBox = ErrorBox()
  //   dlog.title(clear, "公費入力")
  //   dlog.body(
  //     clear,
  //     patientBlock(state.patient),
  //     inputs.formPanel,
  //     errBox.ele
  //   )
  //   dlog.commands(
  //     clear,
  //     button(
  //       "入力",
  //       onclick := (() =>
  //         inputs.validateForEnter(state.patient.patientId) match {
  //           case Left(msg) => errBox.show(msg)
  //           case Right(formKouhi) =>
  //             for entered <- Api.enterKouhi(formKouhi)
  //             yield next(GoBack(state.add(entered)))
  //         }
  //         ()
  //       )
  //     ),
  //     button("キャンセル", onclick := (() => next(cancel(state))))
  //   )

  // def editPatient(state: State, next: Transition[State] => Unit): Unit =
  //   val inputs = new PatientInputs(Some(state.patient))
  //   val errBox = ErrorBox()
  //   dlog.title(clear, "患者情報編集")
  //   dlog.body(
  //     clear,
  //     patientBlock(state.patient),
  //     inputs.formPanel,
  //     errBox.ele
  //   )
  //   dlog.commands(
  //     clear,
  //     button(
  //       "入力",
  //       onclick := (() =>
  //         inputs.validateForUpdate() match {
  //           case Left(msg) => errBox.show(msg)
  //           case Right(formPatient) =>
  //             (
  //               for
  //                 _ <- Api.updatePatient(formPatient)
  //                 updated <- Api.getPatient(state.patient.patientId)
  //               yield updated
  //             ).onComplete {
  //               case Success(updated) =>
  //                 next(GoBack(state.copy(patient = updated)))
  //               case Failure(ex) => errBox.show(ex.toString)
  //             }
  //         }
  //       )
  //     ),
  //     button("キャンセル", onclick := (() => next(GoBack(state))))
  //   )

  // private def doRegister(state: State, next: Transition[State] => Unit): Unit =
  //   val patient: Patient = state.patient
  //   Api.startVisit(patient.patientId, LocalDateTime.now()).onComplete {
  //     case Success(_) =>
  //       dlog.close()
  //       next(Exit())
  //     case Failure(ex) => ShowMessage.showError(ex.toString)
  //   }

