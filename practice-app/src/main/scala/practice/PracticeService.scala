package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.all.{_, given}
import dev.fujiwara.domq.searchform.*
import dev.fujiwara.kanjidate.DateUtil
import dev.fujiwara.kanjidate.KanjiDate
import dev.myclinic.scala.model.Patient
import dev.myclinic.scala.model.Visit
import dev.myclinic.scala.model.WaitState
import dev.myclinic.scala.web.appbase.SideMenuService
import dev.myclinic.scala.web.practiceapp.practice.mishuu.Mishuu
import dev.myclinic.scala.webclient.Api
import dev.myclinic.scala.webclient.global
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.web.practiceapp.PracticeBus

import java.time.LocalDate
import java.time.LocalDateTime
import scala.concurrent.Future
import scala.language.implicitConversions
import scala.quoted.FromExpr.NoneFromExpr
import scala.util.Failure
import scala.util.Success
import dev.myclinic.scala.util.NumberUtil.format

class PracticeService extends SideMenuService:
  val right = div()
  override def getElements = List(
    div(cls := "practice-main practice-sidemenu-service-main")(
      div(cls := "practice-header")(
        div(
          "診察",
          cls := "practice-header-title practice-sidemenu-service-title"
        ),
        a("患者選択", cls := "practice-header-choice", cb := PatientSelector.init),
        a("登録薬剤", onclick := (() => ShohouSampleDialog.open())),
        a("全文検索", onclick := (() => WholeTextSearchDialog.open()))
      ),
      div(
        new PatientDisplay().ele,
        PatientManip.ele,
        new Nav().ele,
        new RecordsWrapper().ele,
        new Nav().ele
      )
    ),
    right(
      cls := "practice-right-column",
      Disease.ele,
      Mishuu.ele
    )
  )

  PracticeBus.addRightWidgetRequest.subscribe(w => right(w.ele))
  PracticeBus.removeRightWidgetRequest.subscribe(w => w.remove())
  PracticeBus.patientStartingSubscriberChannel.subscribe(s => {
    RecordsHelper.refreshRecords(Some(s.patient), 0)
  })
  PracticeBus.patientClosingSubscriberChannel.subscribe(_ => {
    RecordsHelper.refreshRecords(None, 0)
  })


object PatientSelector:
  def init(a: HTMLElement): Unit =
    PullDown.attachPullDown(
      a,
      List(
        "受付患者選択" -> (selectFromRegistered _),
        "患者検索" -> (selectBySearchPatient _),
        "最近の診察" -> (selectFromRecentVisits _),
        "日付別" -> (selectByDate _)
      )
    )
  def formatPatient(patient: Patient): String =
    String.format(
      "%04d %s (%d才)",
      patient.patientId,
      patient.fullName(),
      DateUtil.calcAge(patient.birthday, LocalDate.now())
    )

  private def startVisit(patient: Patient): Unit =
    for visit <- Api.startVisit(patient.patientId, LocalDateTime.now())
    yield println(visit)

  def searchShohouSample(): Unit =
    ShohouSampleDialog.open()

  private def startExam(patient: Patient, visit: Visit): Unit =
    PracticeBus.patientStateController.startPatient(
      patient,
      Some(visit.visitId)
    )

  def selectFromRegistered(): Unit =
    for pairs <- PracticeService.listRegisteredPatientForPractice
    yield
      val sel = Selection[(Patient, Visit)]()
      sel.clear()
      sel.addAll(pairs, pair => formatPatient(pair._1))
      sel.addDone()
      val d = new ModalDialog3
      d.title("受付患者選択")
      d.body(sel.ele(cls := "practice-select-from-registered-selection"))
      d.commands(
        button(
          "選択",
          onclick := (() =>
            sel.marked.foreach(pair =>
              d.close()
              pair match {
                case (patient, visit) => startExam(patient, visit)
              }
            )
          )
        ),
        button("キャンセル", onclick := (() => d.close()))
      )
      d.open()

  def selectBySearchPatient(): Unit =
    val d = new ModalDialog3
    val search =
      new SearchForm[Patient](
        patient => div(innerText := formatPatient(patient)),
        Api.searchPatient(_).map(_._2)
      )
    d.title("患者検索")
    d.body(
      search.ele
    )
    d.commands(
      button(
        "診察登録",
        onclick := (() =>
          ShowMessage.confirm("診察登録をしますか？") { () =>
            d.close()
            search.selected.foreach(patient => startVisit(patient))
          }
        )
      ),
      button(
        "選択",
        onclick := (() => {
          d.close()
          search.selected.foreach(patient =>
            PracticeBus.patientStateController.startPatient(patient, None)
          )
        })
      ),
      button("閉じる", onclick := (() => d.close()))
    )
    d.open()
    search.ui.input.focus()

  def selectFromRecentVisits(): Unit =
    var offset = 0
    val count = 20
    val d = new ModalDialog3
    val selection = new Selection[(Visit, Patient)]
    val formatter: (Visit, Patient) => String = (visit, patient) =>
      String.format(
        "%04d %s [%s]",
        patient.patientId,
        patient.fullName(),
        KanjiDate.dateToKanji(visit.visitedAt.toLocalDate)
      )
    def update(): Future[Unit] =
      for result <- Api.listRecentVisitFull(offset, count)
      yield
        selection.clear()
        selection.addAll(result, formatter.tupled)
    d.body(
      selection.ele,
      div(
        a(
          "前へ",
          onclick := (() => {
            offset -= count
            if offset < 0 then offset = 0
            update()
            ()
          })
        ),
        a(
          "次へ",
          onclick := (() => {
            offset += count
            update()
            ()
          })
        )
      )
    )
    d.commands(
      button(
        "選択",
        onclick := (() => {
          d.close()
          selection.marked.foreach(visitPatient =>
            PracticeBus.patientStateController
              .startPatient(visitPatient._2, None)
          )
        })
      ),
      button("キャンセル", onclick := (() => d.close()))
    )
    update().onComplete {
      case Success(_)  => d.open()
      case Failure(ex) => System.err.println(ex.getMessage)
    }

  def selectByDate(): Unit =
    val widget = new SelectPatientByDateWidget()
    widget.patientSelected.subscribe(patient =>
      PracticeBus.patientStateController.startPatient(patient, None)
    )
    PracticeBus.addRightWidgetRequest.publish(widget.widget)

object PracticeService:
  def listRegisteredPatient(): Future[List[(Patient, Visit)]] =
    for (gen, wqList, visitMap, patientMap) <- Api.listWqueueFull()
    yield wqList.map(wq =>
      val v = visitMap(wq.visitId)
      val p = patientMap(v.patientId)
      (p, v)
    )

  def isForPractice(wstate: WaitState): Boolean =
    wstate == WaitState.WaitExam || wstate == WaitState.WaitReExam || wstate == WaitState.InExam

  def listRegisteredPatientForPractice: Future[List[(Patient, Visit)]] =
    for (gen, wqList, visitMap, patientMap) <- Api.listWqueueFull()
    yield wqList
      .filter(wq => isForPractice(wq.waitState))
      .map(wq =>
        val v = visitMap(wq.visitId)
        val p = patientMap(v.patientId)
        (p, v)
      )


