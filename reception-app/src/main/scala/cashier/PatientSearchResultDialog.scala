package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.myclinic.scala.webclient.{Api, global}
import java.time.LocalDateTime
import java.time.LocalDate
import scala.concurrent.Future
import dev.myclinic.scala.apputil.HokenUtil
import dev.myclinic.scala.web.appbase.*
import org.scalajs.dom.HTMLElement

case class PatientSearchResultDialog(patients: List[Patient]):
  val selection = Selection[Patient](patients, p => div(format(p)))
  selection.onSelect(patient => start(patient))
  val dlog = new ModalDialog3()
  dlog.content(cls := "reception-cashier-search-patient-result-dialog")
  dlog.title("患者検索結果")
  dlog.body(selection.ele(cls := "selection"))
  dlog.commands(
    button("閉じる", onclick := (() => dlog.close()))
  )
  if patients.size == 1 then start(patients.head)

  def open(): Unit =
    dlog.open()

  def format(patient: Patient): String =
    String.format(
      "(%04d) %s %s",
      patient.patientId,
      patient.lastName,
      patient.firstName
    )

  case class State(patient: Patient, hokenList: List[Hoken]):
    import Hoken.*
    def getShahokokuho(shahokokuhoId: Int): Option[Shahokokuho] =
      hokenList
        .filter(_.isShahokokuho)
        .map(_.tryCastAsShahokokuho.get)
        .find(_.shahokokuhoId == shahokokuhoId)
    def getKoukikourei(koukikoureiId: Int): Option[Koukikourei] =
      hokenList
        .filter(_.isKoukikourei)
        .map(_.tryCastAsKoukikourei.get)
        .find(_.koukikoureiId == koukikoureiId)
    def getRoujin(roujinId: Int): Option[Roujin] =
      hokenList
        .filter(_.isRoujin)
        .map(_.tryCastAsRoujin.get)
        .find(_.roujinId == roujinId)
    def getKouhi(kouhiId: Int): Option[Kouhi] =
      hokenList
        .filter(_.isKouhi)
        .map(_.tryCastAsKouhi.get)
        .find(_.kouhiId == kouhiId)
    def add(hoken: Hoken): State =
      val hokenId = HokenId(hoken)
      val (pre, post) = hokenList.span(h => HokenId(h) != hokenId)
      val newList =
        if post.isEmpty then pre :+ hoken
        else (pre :+ hoken) ++ post.tail
      copy(hokenList = newList)
    def remove(hoken: Hoken): State =
      println(("removing", hoken))
      val hokenId = HokenId(hoken)
      copy(hokenList = hokenList.filter(h => HokenId(h) != hokenId))

  type DlogFun = (State, Transition => Unit) => Unit

  enum Transition:
    case GoForward(next: DlogFun, state: State)
    case GoBack(state: State)
    case GoTo(
        next: DlogFun,
        state: State,
        stackFun: List[DlogFun] => List[DlogFun] = identity
    )
    case Exit

  import Transition.*

  def run(f: DlogFun, state: State, stack: List[DlogFun]): Unit =
    f(
      state,
      trans =>
        trans match {
          case GoForward(next, state)  => run(next, state, f :: stack)
          case GoTo(next, state, sfun) => run(next, state, sfun(stack))
          case GoBack(state) =>
            if stack.isEmpty then System.err.println("stack under flow")
            else run(stack.head, state, stack.tail)
          case Exit => dlog.close()
        }
    )

  private def listHoken(patientId: Int): Future[List[Hoken]] =
    for
      result <- Api.getPatientHoken(patientId, LocalDate.now())
      (_, _, shahokokuho, koukikourei, roujin, kouhi) = result
    yield List.empty[Hoken] ++ shahokokuho ++ koukikourei ++ roujin ++ kouhi

  private def start(patient: Patient): Unit =
    for hokenList <- listHoken(patient.patientId)
    yield
      val state = State(patient, hokenList)
      run(disp, state, List.empty)

  private def disp(state: State, next: Transition => Unit): Unit =
    import Hoken.*
    doDisp(
      state.copy(
        hokenList = state.hokenList.filter(_.isValidAt(LocalDate.now()))
      ),
      next
    )

  private def doDisp(state: State, next: Transition => Unit): Unit =
    val hokenArea = div
    val dispElement = new PatientReps(Some(state.patient)).dispPanel
    dlog.body(
      clear,
      div(
        cls := "reception-cashier-patient-search-result-dialog-disp-body",
        dispElement,
        hokenArea(
          cls := "hoken-area",
          state.hokenList.map(h => {
            a(
              HokenUtil.hokenRep(h),
              onclick := (() => dispatchDispHoken(h, state, next))
            )
          })
        )
      )
    )
    dlog.commands(
      clear,
      div(
        button(
          "診察受付",
          onclick := (() =>
            next(GoForward(doRegister(state.patient.patientId), state))
          )
        ),
        button("閉じる", onclick := (() => next(Exit)))
      ),
      div(
        cls := "domq-mt-4 reception-cashier-patient-search-result-dialog-disp-link-commands",
        a("編集", onclick := (() => next(GoForward(edit, state)))),
        "|",
        a(
          "新規社保国保",
          onclick := (() =>
            next(GoForward(newShahokokuho(new ShahokokuhoInputs(None)), state))
          )
        ),
        "|",
        a(
          "新規後期高齢",
          onclick := (() =>
            next(GoForward(newKoukikourei(new KoukikoureiInputs(None)), state))
          )
        ),
        "|",
        a("新規公費", onclick := (() => next(GoForward(newKouhi(new KouhiInputs(None)), state)))),
        "|",
        a("保険履歴", onclick := (() => next(GoForward(hokenHistory, state))))
      )
    )

  private def dispatchDispHoken(
      hoken: Hoken,
      state: State,
      next: Transition => Unit
  ): Unit =
    hoken match {
      case s: Shahokokuho =>
        next(
          GoForward(dispShahokokuho(s.shahokokuhoId), state)
        )
      case k: Koukikourei =>
        next(
          GoForward(dispKoukikourei(k.koukikoureiId), state)
        )
      case k: Kouhi =>
        next(GoForward(dispKouhi(k.kouhiId), state))
      case r: Roujin =>
        next(GoForward(dispRoujin(r.roujinId), state))
    }

  private def hokenHistory(state: State, next: Transition => Unit): Unit =
    dlog.body(
      clear,
      "Hoken History"
    )
    dlog.commands(
      clear,
      button("戻る", onclick := (() => next(GoBack(state))))
    )

  // val hokenWrapper = div
  // val boxes: CompSortList[HokenBox] = CompSortList[HokenBox](hokenWrapper)
  // dlog.body(
  //   clear,
  //   patientBlock(patient),
  //   div("保険履歴", cls := "patient-search-result-dialog-subtitle"),
  //   hokenWrapper
  // )
  // dlog.commands(
  //   clear,
  //   button("戻る", onclick := (() => onDone(false)))
  // )
  // for result <- Api.listAllHoken(patient.patientId)
  // yield
  //   val hokenList = HokenUtil.toHokenList.tupled(result)
  //   boxes.set(hokenList.map(HokenBox.apply _))

  private def dispShahokokuho(
      shahokokuhoId: Int
  )(state: State, next: Transition => Unit): Unit =
    state.getShahokokuho(shahokokuhoId) match {
      case None    => next(GoBack(state))
      case Some(h) => doDispShahokokuho(h, state, next)
    }

  private def doDispShahokokuho(
      shahokokuho: Shahokokuho,
      state: State,
      next: Transition => Unit
  ): Unit =
    val panel = new ShahokokuhoReps(Some(shahokokuho)).dispPanel
    val renewButton = button
    dlog.body(
      clear,
      patientBlock(state.patient, "社保国保"),
      panel
    )
    dlog.commands(
      clear,
      renewButton(
        "更新",
        displayNone,
        onclick := (() =>
          shahokokuho.validUpto.value.foreach(d => {
            val inputs = new ShahokokuhoInputs(
              Some(
                shahokokuho.copy(
                  shahokokuhoId = 0,
                  validFrom = d.plusDays(1),
                  validUpto = ValidUpto(None)
                )
              )
            )
            next(
              GoTo(
                newShahokokuho(
                  inputs,
                  state =>
                    GoTo(dispShahokokuho(shahokokuho.shahokokuhoId), state)
                ),
                state
              )
            )
          })
        )
      ),
      button(
        "編集",
        onclick := (() =>
          next(GoForward(editShahokokuho(shahokokuho.shahokokuhoId), state))
        )
      ),
      button("戻る", onclick := (() => next(GoBack(state))))
    )
    renewButton.show(shahokokuho.validUpto.value.isDefined)

  private def editShahokokuho(
      shahokokuhoId: Int
  )(state: State, next: Transition => Unit): Unit =
    state.getShahokokuho(shahokokuhoId) match {
      case None    => next(GoBack(state))
      case Some(h) => doEditShahokokuho(h, state, next)
    }

  private def doEditShahokokuho(
      shahokokuho: Shahokokuho,
      state: State,
      next: Transition => Unit
  ): Unit =
    val inputs = new ShahokokuhoInputs(Some(shahokokuho))
    val errBox = ErrorBox()
    dlog.body(
      clear,
      patientBlock(state.patient, "社保国保編集"),
      inputs.formPanel,
      errBox.ele
    )
    dlog.commands(
      clear,
      button(
        "入力",
        onclick := (() =>
          inputs.validateForUpdate() match {
            case Left(msg) => errBox.show(msg)
            case Right(newShahokokuho) =>
              for
                _ <- Api.updateShahokokuho(newShahokokuho)
                updated <- Api.getShahokokuho(shahokokuho.shahokokuhoId)
              yield next(GoBack(state.add(updated)))
              ()
          }
        )
      ),
      button("キャンセル", onclick := (() => next(GoBack(state))))
    )

  private def dispKoukikourei(
      koukikoureiId: Int
  )(state: State, next: Transition => Unit): Unit =
    state.getKoukikourei(koukikoureiId) match {
      case Some(k) => doDispKoukikourei(k, state, next)
      case None    => next(GoBack(state))
    }

  private def doDispKoukikourei(
      koukikourei: Koukikourei,
      state: State,
      next: Transition => Unit
  ): Unit =
    val panel: HTMLElement = new KoukikoureiReps(Some(koukikourei)).dispPanel
    val renewButton = button
    dlog.body(
      clear,
      patientBlock(state.patient, "後期高齢"),
      panel
    )
    dlog.commands(
      clear,
      renewButton(
        "更新",
        displayNone,
        onclick := (() =>
          koukikourei.validUpto.value.foreach(d => {
            val inputs = new KoukikoureiInputs(
              Some(
                koukikourei.copy(
                  koukikoureiId = 0,
                  validFrom = d.plusDays(1),
                  validUpto = ValidUpto(None)
                )
              )
            )
            next(
              GoTo(
                newKoukikourei(
                  inputs,
                  state =>
                    GoTo(dispKoukikourei(koukikourei.koukikoureiId), state)
                ),
                state
              )
            )
          })
        )
      ),
      button(
        "編集",
        onclick := (() =>
          next(GoForward(editKoukikourei(koukikourei.koukikoureiId), state))
        )
      ),
      button("戻る", onclick := (() => next(GoBack(state))))
    )
    renewButton.show(koukikourei.validUpto.value.isDefined)

  private def editKoukikourei(
      koukikoureiId: Int
  )(state: State, next: Transition => Unit): Unit =
    state.getKoukikourei(koukikoureiId) match {
      case Some(k) => doEditKoukikourei(k, state, next)
      case None    => next(GoBack(state))
    }

  private def doEditKoukikourei(
      koukikourei: Koukikourei,
      state: State,
      next: Transition => Unit
  ): Unit =
    val inputs = new KoukikoureiInputs(Some(koukikourei))
    val errBox = ErrorBox()
    dlog.body(
      clear,
      patientBlock(state.patient, "後期高齢編集"),
      inputs.formPanel,
      errBox.ele
    )
    dlog.commands(
      clear,
      button(
        "入力",
        onclick := (() =>
          inputs.validateForUpdate() match {
            case Right(formKoukikourei) =>
              for
                _ <- Api.updateKoukikourei(formKoukikourei)
                updated <- Api.getKoukikourei(koukikourei.koukikoureiId)
              yield next(GoBack(state.add(updated)))
            case Left(msg) => errBox.show(msg)
          }
          ()
        )
      ),
      button("戻る", onclick := (() => next(GoBack(state))))
    )

  private def dispKouhi(
      kouhiId: Int
  )(state: State, next: Transition => Unit): Unit =
    state.getKouhi(kouhiId) match {
      case Some(k) => doDispKouhi(k, state, next)
      case None    => next(GoBack(state))
    }

  private def doDispKouhi(
      kouhi: Kouhi, state: State, next: Transition => Unit): Unit =
    val panel: HTMLElement = new KouhiReps(Some(kouhi)).dispPanel
    val renewButton = button
    dlog.body(
      clear,
      patientBlock(state.patient, "公費"),
      panel
    )
    dlog.commands(
      clear,
      renewButton(
        "更新",
        displayNone,
        onclick := (() =>
          kouhi.validUpto.value.foreach(d => {
            val inputs = new KouhiInputs(
              Some(
                kouhi.copy(
                  kouhiId = 0,
                  validFrom = d.plusDays(1),
                  validUpto = ValidUpto(None)
                )
              )
            )
            next(
              GoTo(
                newKouhi(
                  inputs,
                  state =>
                    GoTo(dispKouhi(kouhi.kouhiId), state)
                ),
                state
              )
            )
          })
        )
      ),
      button(
        "編集",
        onclick := (() =>
          next(GoForward(editKouhi(kouhi.kouhiId), state))
        )
      ),
      button("戻る", onclick := (() => next(GoBack(state))))
    )
    renewButton.show(kouhi.validUpto.value.isDefined)

  private def editKouhi(
      kouhiId: Int
  )(state: State, next: Transition => Unit): Unit =
    state.getKouhi(kouhiId) match {
      case Some(k) => doEditKouhi(k, state, next)
      case None    => next(GoBack(state))
    }

  private def doEditKouhi(
      kouhi: Kouhi,
      state: State,
      next: Transition => Unit
  ): Unit =
    val inputs = new KouhiInputs(Some(kouhi))
    val errBox = ErrorBox()
    dlog.body(
      clear,
      patientBlock(state.patient, "公費"),
      inputs.formPanel,
      errBox.ele
    )
    dlog.commands(
      clear,
      button(
        "入力",
        onclick := (() =>
          inputs.validateForUpdate() match {
            case Right(formKouhi) =>
              for
                _ <- Api.updateKouhi(formKouhi)
                updated <- Api.getKouhi(kouhi.kouhiId)
              yield next(GoBack(state.add(updated)))
            case Left(msg) => errBox.show(msg)
          }
          ()
        )
      ),
      button("戻る", onclick := (() => next(GoBack(state))))
    )

  private def dispRoujin(
      roujinId: Int
  )(state: State, next: Transition => Unit): Unit =
    val roujin: Roujin = state.getRoujin(roujinId).get
    val panel: HTMLElement = new RoujinReps(Some(roujin)).dispPanel
    dlog.body(
      clear,
      patientBlock(state.patient, "老人保健"),
      panel
    )
    dlog.commands(
      clear,
      button("戻る", onclick := (() => next(GoBack(state))))
    )

  private def patientBlock(patient: Patient, msg: String = ""): HTMLElement =
    val extra = if msg.isEmpty then "" else s"${msg}："
    div(
      innerText := extra + s"(${patient.patientId}) ${patient.lastName} ${patient.firstName}",
      cls := "patient-block"
    )

  private def newShahokokuho(
      inputs: ShahokokuhoInputs,
      cancel: State => Transition = GoBack(_)
  )(state: State, next: Transition => Unit): Unit =
    val errBox = ErrorBox()
    dlog.body(
      clear,
      patientBlock(state.patient, "新規社保国保"),
      inputs.formPanel,
      errBox.ele
    )
    dlog.commands(
      clear,
      button(
        "入力",
        onclick := (() =>
          inputs.validateForEnter(state.patient.patientId) match {
            case Left(msg) => errBox.show(msg)
            case Right(shahokokuho) =>
              for entered <- Api.enterShahokokuho(shahokokuho)
              yield next(GoBack(state.add(entered)))
          }
          ()
        )
      ),
      button("キャンセル", onclick := (() => next(cancel(state))))
    )

  private def newKoukikourei(
      inputs: KoukikoureiInputs,
      cancel: State => Transition = GoBack(_)
  )(state: State, next: Transition => Unit): Unit =
    val errBox = ErrorBox()
    dlog.body(
      clear,
      patientBlock(state.patient, "後期高齢入力"),
      inputs.formPanel,
      errBox.ele
    )
    dlog.commands(
      clear,
      button(
        "入力",
        onclick := (() =>
          inputs.validateForEnter(state.patient.patientId) match {
            case Left(msg) => errBox.show(msg)
            case Right(formKoukikourei) =>
              for entered <- Api.enterKoukikourei(formKoukikourei)
              yield next(GoBack(state.add(entered)))
          }
          ()
        )
      ),
      button("キャンセル", onclick := (() => next(cancel(state))))
    )
  // val props = KoukikoureiInputs(None)
  // val errBox = ErrorBox()
  // dlog.body(clear, props.formPanel, errBox.ele)
  // dlog.commands(
  //   clear,
  //   button(
  //     "入力",
  //     onclick := (() => {
  //       props.validateForEnter(patient.patientId) match {
  //         case Left(msg) => errBox.show(msg)
  //         case Right(newKoukikourei) =>
  //           for entered <- Api.enterKoukikourei(newKoukikourei)
  //           yield invokeDisp(patient)
  //       }
  //       ()
  //     })
  //   ),
  //   button("キャンセル", onclick := (() => disp(patient, hokenList)))
  // )

  private def newKouhi(
      inputs: KouhiInputs,
      cancel: State => Transition = GoBack(_)
  )(state: State, next: Transition => Unit): Unit =
    val errBox = ErrorBox()
    dlog.body(
      clear,
      patientBlock(state.patient, "公費入力"),
      inputs.formPanel,
      errBox.ele
    )
    dlog.commands(
      clear,
      button(
        "入力",
        onclick := (() =>
          inputs.validateForEnter(state.patient.patientId) match {
            case Left(msg) => errBox.show(msg)
            case Right(formKouhi) =>
              for entered <- Api.enterKouhi(formKouhi)
              yield next(GoBack(state.add(entered)))
          }
          ()
        )
      ),
      button("キャンセル", onclick := (() => next(cancel(state))))
    )

  private def edit(state: State, next: Transition => Unit): Unit =
    dlog.body(
      clear,
      s"Edit Patient ${state.patient}"
    )
    dlog.commands(
      clear,
      button(
        "入力",
        onclick := (() =>
          val newState: State = state
          next(GoBack(newState))
        )
      ),
      button("キャンセル", onclick := (() => next(GoBack(state))))
    )
  // val patient: Patient = state.patient
  // val props = new PatientInputs(Some(patient))
  // val errBox = ErrorBox()
  // dlog.body(clear, props.formPanel, errBox.ele)
  // dlog.commands(
  //   clear,
  //   button(
  //     "入力",
  //     onclick := (() => {
  //       props.validateForUpdate match {
  //         case Left(msg) => errBox.show(msg)
  //         case Right(newPatient) =>
  //           for
  //             _ <- Api.updatePatient(newPatient)
  //             updated <- Api.getPatient(patient.patientId)
  //             _ <- invokeDisp(updated)
  //           yield ()
  //       }
  //       ()
  //     })
  //   ),
  //   button("キャンセル", onclick := (() => { invokeDisp(patient); () }))
  // )

  private def doRegister(
      patientId: Int
  )(state: State, next: Transition => Unit): Unit =
    dlog.body(
      clear,
      s"Register Exam"
    )
    dlog.commands(
      clear,
      button(
        "入力",
        onclick := (() =>
          val newState: State = state
          next(GoBack(newState))
        )
      ),
      button("キャンセル", onclick := (() => next(GoBack(state))))
    )

// for _ <- Api.startVisit(patientId, LocalDateTime.now())
// yield dlog.close()
