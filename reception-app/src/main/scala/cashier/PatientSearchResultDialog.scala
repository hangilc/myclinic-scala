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
      val newList = (pre :+ hoken) ++ post.tail
      copy(hokenList = newList)

  type DlogFun = (State, Transition => Unit) => Unit

  enum Transition:
    case GoForward(next: DlogFun, state: State)
    case Replace(next: DlogFun, state: State)
    case GoBack(state: State)
    case Exit

  import Transition.*

  def run(f: DlogFun, state: State, stack: List[DlogFun]): Unit =
    f(
      state,
      trans =>
        trans match {
          case GoForward(next, state) => run(next, state, f :: stack)
          case Replace(next, state)   => run(next, state, stack.tail)
          case GoBack(state) if stack.headOption.isDefined =>
            run(stack.head, state, stack.tail)
          case _ =>
            dlog.close()
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
    val patient: Patient = state.patient
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
            next(GoForward(doRegister(patient.patientId), state))
          )
        ),
        button("閉じる", onclick := (() => next(Exit)))
      ),
      div(
        cls := "domq-mt-4 reception-cashier-patient-search-result-dialog-disp-link-commands",
        a("編集", onclick := (() => next(GoForward(edit, state)))),
        "|",
        a("新規社保国保", onclick := (() => next(GoForward(newShahokokuho, state)))),
        "|",
        a("新規後期高齢", onclick := (() => next(GoForward(newKoukikourei, state)))),
        "|",
        a("新規公費", onclick := (() => next(GoForward(newKouhi, state)))),
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
    dlog.body(
      clear,
      panel
    )
    dlog.commands(
      clear,
      button(
        "編集",
        onclick := (() =>
          next(GoForward(editShahokokuho(shahokokuho.shahokokuhoId), state))
        )
      ),
      button("戻る", onclick := (() => next(GoBack(state))))
    )

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
    val renewButton = button
    dlog.body(
      clear,
      inputs.formPanel,
      errBox.ele
    )
    dlog.commands(
      clear,
      renewButton("更新"),
      button(
        "入力",
        onclick := (() =>
          inputs.validateForUpdate() match {
            case Left(msg) => errBox.show(msg)
            case Right(newShahokokuho) =>
              for 
                _ <- Api.updateShahokokuho(newShahokokuho)
                updated <- Api.getShahokokuho(shahokokuho.shahokokuhoId)
              yield
                next(GoBack(state.add(updated)))
          }
          val newState: State = state
          next(GoBack(newState))
        )
      ),
      button("キャンセル", onclick := (() => next(GoBack(state))))
    )
    renewButton.show(inputs.validUptoInput.getValue.value.isDefined)
    inputs.validUptoInput.onChange(validUpto =>
      renewButton.show(validUpto.value.isDefined)
    )
  //     shahokokuho: Shahokokuho,
  //     patient: Patient,
  //     onDone: Modified => Unit
  // ): Unit =
  //   val props = new ShahokokuhoInputs(Some(shahokokuho))
  //   val errBox = ErrorBox()
  //   val renewButton = button
  //   dlog.body(clear, patientBlock(patient), props.formPanel, errBox.ele)
  //   dlog.commands(
  //     clear,
  //     renewButton(
  //       "更新",
  //       displayNone,
  //       onclick := (() => {
  //         props.validateForUpdate().flatMap(createRenewalShahokokuho _) match {
  //           case Left(msg) => errBox.show(msg)
  //           case Right((renewalShahokokuho, inputShahokokuho)) => {
  //             for updated <- updateIfModified(inputShahokokuho, shahokokuho)
  //             yield newShahokokuho(patient, Some(renewalShahokokuho))
  //           }
  //         }
  //         ()
  //       })
  //     ),
  //     button(
  //       "入力",
  //       onclick := (() => {
  //         props.validateForUpdate() match {
  //           case Left(msg) => errBox.show(msg)
  //           case Right(newShahokokuho) =>
  //             for _ <- Api.updateShahokokuho(newShahokokuho)
  //             yield onDone(true)
  //         }
  //         ()
  //       })
  //     ),
  //     button("キャンセル", onclick := (() => onDone(false)))
  //   )
  //   if props.validUptoInput.getValue.value.isDefined then
  //     renewButton(displayDefault)
  //   props.validUptoInput.onChange(validUpto =>
  //     validUpto.value match {
  //       case Some(_) => renewButton(displayDefault)
  //       case None    => renewButton(displayNone)
  //     }
  //   )

  private def updateIfModified(
      newShahokokuho: Shahokokuho,
      oldShahokokuho: Shahokokuho
  ): Future[Boolean] =
    if newShahokokuho == oldShahokokuho then Future.successful(false)
    else Api.updateShahokokuho(newShahokokuho).map(_ => true)

  type RenewalShahokokuho = Shahokokuho

  private def createRenewalShahokokuho(
      oldShahokokuho: Shahokokuho
  ): Either[String, (RenewalShahokokuho, Shahokokuho)] =
    oldShahokokuho.validUpto.value match {
      case None => Left("期限終了日が設定されていません。")
      case Some(validUptoValue) =>
        val renew: RenewalShahokokuho = oldShahokokuho.copy(
          shahokokuhoId = 0,
          validFrom = validUptoValue.plusDays(1),
          validUpto = ValidUpto(None)
        )
        Right(renew, oldShahokokuho)
    }

  private def dispKoukikourei(
      koukikoureiId: Int
  )(state: State, next: Transition => Unit): Unit =
    dlog.body(
      clear,
      s"Disp Koukikourei ${koukikoureiId}"
    )
    dlog.commands(
      clear,
      button("戻る", onclick := (() => next(GoBack(state))))
    )
  //     koukikourei: Koukikourei,
  //     patient: Patient,
  //     onDone: Modified => Unit
  // ): Unit =
  //   val props = new KoukikoureiReps(Some(koukikourei))
  //   dlog.body(clear, patientBlock(patient), props.dispPanel)
  //   dlog.commands(clear, button("戻る", onclick := (() => onDone(false))))

  private def dispKouhi(
      kouhiId: Int
  )(state: State, next: Transition => Unit): Unit =
    dlog.body(
      clear,
      s"Disp Kouhi ${kouhiId}"
    )
    dlog.commands(
      clear,
      button("戻る", onclick := (() => next(GoBack(state))))
    )

  //     kouhi: Kouhi,
  //     patient: Patient,
  //     onDone: Modified => Unit
  // ): Unit =
  //   val props = new KouhiReps(Some(kouhi))
  //   dlog.body(clear, patientBlock(patient), props.dispPanel)
  //   dlog.commands(clear, button("戻る", onclick := (() => onDone(false))))

  private def dispRoujin(
      roujinId: Int
  )(state: State, next: Transition => Unit): Unit =
    dlog.body(
      clear,
      s"Disp Roujin ${roujinId}"
    )
    dlog.commands(
      clear,
      button("戻る", onclick := (() => next(GoBack(state))))
    )
  //     roujin: Roujin,
  //     patient: Patient,
  //     onDone: Modified => Unit
  // ): Unit =
  //   val props = new RoujinReps(Some(roujin))
  //   dlog.body(clear, patientBlock(patient), props.dispPanel)
  //   dlog.commands(clear, button("戻る", onclick := (() => onDone(false))))

  private def patientBlock(patient: Patient): HTMLElement =
    div(
      innerText := s"(${patient.patientId}) ${patient.lastName} ${patient.firstName}",
      cls := "patient-block"
    )

  private def newShahokokuho(state: State, next: Transition => Unit): Unit =
    dlog.body(
      clear,
      s"New Shahokokuho"
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
  //     patient: Patient,
  //     template: Option[Shahokokuho] = None
  // ): Unit =
  //   val props = new ShahokokuhoInputs(template)
  //   val errBox = ErrorBox()
  //   dlog.body(clear, patientBlock(patient), props.formPanel, errBox.ele)
  //   dlog.commands(
  //     clear,
  //     button(
  //       "入力",
  //       onclick := (() => {
  //         props.validateForEnter(patient.patientId) match {
  //           case Left(msg) => errBox.show(msg)
  //           case Right(newShahokokuho) =>
  //             for entered <- Api.enterShahokokuho(newShahokokuho)
  //             yield invokeDisp(patient)
  //         }
  //         ()
  //       })
  //     ),
  //     button("キャンセル", onclick := (() => { invokeDisp(patient); () }))
  //   )

  private def newKoukikourei(state: State, next: Transition => Unit): Unit =
    dlog.body(
      clear,
      s"New Koukikourei"
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

  private def newKouhi(state: State, next: Transition => Unit): Unit =
    dlog.body(
      clear,
      s"New Kouhi"
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
  // val props = new KouhiInputs(None)
  // val errBox = ErrorBox()
  // dlog.body(clear, props.formPanel, errBox.ele)
  // dlog.commands(
  //   clear,
  //   button(
  //     "入力",
  //     onclick := (() => {
  //       props.validateForEnter(patient.patientId) match {
  //         case Left(msg) => errBox.show(msg)
  //         case Right(newKouhi) =>
  //           for _ <- Api.enterKouhi(newKouhi)
  //           yield invokeDisp(patient)
  //       }
  //       ()
  //     })
  //   ),
  //   button("キャンセル", onclick := (() => disp(patient, hokenList)))
  // )

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
