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

  private def listHoken(patientId: Int): Future[List[Hoken]] =
    for
      result <- Api.getPatientHoken(patientId, LocalDate.now())
      (_, _, shahokokuho, koukikourei, roujin, kouhi) = result
    yield List.empty[Hoken] ++ shahokokuho ++ koukikourei ++ roujin ++ kouhi

  private def start(patient: Patient): Unit =
    ???

  case class State(patient: Patient, hokenList: List[Hoken]):
    def getShahokokuho(shahokokuhoId: Int): Option[Shahokokuho] =
      hokenList
        .collect(h =>
          h match {
            case s: Shahokokuho => s
          }
        )
        .find(_.shahokokuhoId == shahokokuhoId)
    def getKoukikourei(koukikoureiId: Int): Option[Koukikourei] =
      hokenList
        .collect(h =>
          h match {
            case s: Koukikourei => s
          }
        )
        .find(_.koukikoureiId == koukikoureiId)
    def getRoujin(roujinId: Int): Option[Roujin] =
      hokenList
        .collect(h =>
          h match {
            case s: Roujin => s
          }
        )
        .find(_.roujinId == roujinId)
    def getKouhi(kouhiId: Int): Option[Kouhi] =
      hokenList
        .collect(h =>
          h match {
            case s: Kouhi => s
          }
        )
        .find(_.kouhiId == kouhiId)

  type DlogFun = (State, Transition => Unit) => Unit

  enum Transition:
    case Start(f: DlogFun, state: State)
    case GoForward(next: DlogFun, state: State, prev: DlogFun)
    case GoBack(state: State)
    case Exit

  import Transition.*

  def run(f: DlogFun, state: State, stack: List[DlogFun]): Unit =
    f(
      state,
      trans =>
        trans match {
          case GoForward(next, state, prev) => run(next, state, prev :: stack)
          case GoBack(state) if stack.headOption.isDefined =>
            run(stack.head, state, stack.tail)
          case _ =>
            dlog.close()
        }
    )

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
              onclick := (() => {
                h match {
                  case s: Shahokokuho =>
                    next(
                      GoForward(dispShahokokuho(s.shahokokuhoId), state, disp)
                    )
                  case k: Koukikourei =>
                    next(
                      GoForward(dispKoukikourei(k.koukikoureiId), state, disp)
                    )
                  case k: Kouhi =>
                    next(GoForward(dispKouhi(k.kouhiId), state, disp))
                  case r: Roujin =>
                    next(GoForward(dispRoujin(r.roujinId), state, disp))
                }
                ()
              })
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
            next(GoForward(doRegister(patient.patientId), state, disp))
          )
        ),
        button("閉じる", onclick := (() => next(Exit)))
      ),
      div(
        cls := "domq-mt-4 reception-cashier-patient-search-result-dialog-disp-link-commands",
        a("編集", onclick := (() => next(GoForward(edit, state, disp)))),
        "|",
        a("新規社保国保", onclick := (() => next(GoForward(newShahokokuho, state, disp)))),
        "|",
        a("新規後期高齢", onclick := (() => next(GoForward(newKoukikourei, state, disp)))),
        "|",
        a("新規公費", onclick := (() => next(GoForward(newKouhi, state, disp)))),
        "|",
        a("保険履歴", onclick := (() => next(GoForward(hokenHistory, state, disp))))
      )
    )

  private def hokenHistory(state: State, next: Transition => Unit): Unit = ???
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
    ???
  //     shahokokuho: Shahokokuho,
  //     patient: Patient,
  //     onDone: Modified => Unit
  // ): Unit =
  //   println(("valid-from", shahokokuho.validFrom))
  //   val props = new ShahokokuhoReps(Some(shahokokuho))
  //   dlog.body(clear, patientBlock(patient), props.dispPanel)
  //   dlog.commands(
  //     clear,
  //     button(
  //       "編集",
  //       onclick := (() => editShahokokuho(shahokokuho, patient, onDone))
  //     ),
  //     button("戻る", onclick := (() => onDone(false)))
  //   )

  private def editShahokokuho(
      shahokokuhoId: Int
  )(state: State, next: Transition => Unit): Unit =
    ???
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
    ???
  //     koukikourei: Koukikourei,
  //     patient: Patient,
  //     onDone: Modified => Unit
  // ): Unit =
  //   val props = new KoukikoureiReps(Some(koukikourei))
  //   dlog.body(clear, patientBlock(patient), props.dispPanel)
  //   dlog.commands(clear, button("戻る", onclick := (() => onDone(false))))

  private def dispKouhi(
      kouhiId: Int
  )(state: State, next: Transition => Unit): Unit = ???

  //     kouhi: Kouhi,
  //     patient: Patient,
  //     onDone: Modified => Unit
  // ): Unit =
  //   val props = new KouhiReps(Some(kouhi))
  //   dlog.body(clear, patientBlock(patient), props.dispPanel)
  //   dlog.commands(clear, button("戻る", onclick := (() => onDone(false))))

  private def dispRoujin(
      roujiId: Int
  )(state: State, next: Transition => Unit): Unit = ???
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

  private def newShahokokuho(state: State, next: Transition => Unit): Unit = ???
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

  private def newKoukikourei(state: State, next: Transition => Unit): Unit = ???
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

  private def newKouhi(state: State, next: Transition => Unit): Unit = ???
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

  private def edit(state: State, next: Transition => Unit): Unit = ???
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

  private def doRegister(patientId: Int)(state: State, next: Transition => Unit): Unit = ???
// for _ <- Api.startVisit(patientId, LocalDateTime.now())
// yield dlog.close()
