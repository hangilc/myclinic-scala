package dev.myclinic.scala.web.practiceapp.practice

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.searchform.*
import org.scalajs.dom.HTMLElement
import scala.concurrent.Future
import dev.myclinic.scala.model.Patient
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.model.Visit
import dev.fujiwara.kanjidate.KanjiDate
import dev.myclinic.scala.util.DateUtil
import scala.util.Success
import scala.util.Failure
import java.time.LocalDate
import java.time.LocalDateTime

class PracticeService extends SideMenuService:
  val left = new PracticeMain
  val right = new PracticeRight

  override def getElements = List(left.ele, right.ele)
  left.ele(
    new PatientDisplay().ele,
    new Nav().ele,
    new RecordsWrapper().ele,
    new Nav().ele
  )

  PracticeBus.addRightWidgetRequest.subscribe(ele => right.ele(ele))
  PracticeBus.startPatientRequest.subscribe(patient => startPatient(patient))
  
  val itemsPerPage = PracticeBus.visitsPerPage
  def calcNumPages(total: Int): Int =
    (total + itemsPerPage - 1) / itemsPerPage

  def startPatient(patient: Patient): Unit =
    PracticeBus.patientChanged.publish(None)
    PracticeBus.patientChanged.publish(Some(patient))
    for
      total <- Api.countVisitByPatient(patient.patientId)
      numPages = calcNumPages(total)
    yield 
      PracticeBus.navSettingChanged.publish(0, numPages)
      PracticeBus.navPageChanged.publish(0)


class PracticeMain:
  val startPatientPublisher = PracticeBus.startPatientRequest
  val startVisitPublisher = PracticeBus.startVisitRequest
  val ui = new PracticeMainUI
  def ele = ui.ele
  ui.choice.setBuilder(
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
    for
      visit <- Api.startVisit(patient.patientId, LocalDateTime.now())
    yield println(visit)

  def selectFromRegistered(): Unit =
    for pairs <- PracticeService.listRegisteredPatient()
    yield
      val sel = Selection[(Patient, Visit)]
      sel.clear()
      sel.addAll(pairs, pair => formatPatient(pair._1), identity)
      val d = new ModalDialog3
      d.title("受付患者選択")
      d.body(sel.ele(cls := "practice-select-from-registered-selection"))
      d.commands(
        button(
          "選択",
          onclick := (() =>
            sel.marked.foreach(pair =>
              d.close()
              startVisitPublisher.publish(_)
            )
          )
        ),
        button("キャンセル", onclick := (() => d.close()))
      )
      d.open()

  def selectBySearchPatient(): Unit =
    val d = new ModalDialog3
    val search =
      new SearchForm[Patient, Patient](
        formatPatient _,
        identity,
        Api.searchPatient(_).map(_._2)
      )
    d.title("患者検索")
    d.body(
      search.ele
    )
    d.commands(
      button(
        "診察登録",
        onclick := (() => {
          d.close()
          search.selected.foreach(patient =>
            startVisit(patient)
          )
        })
      ),
      button(
        "選択",
        onclick := (() => {
          d.close()
          search.selected.foreach(patient =>
            startPatientPublisher.publish(patient)
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
    val selection = new Selection[Patient]
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
        selection.addAll(result, formatter.tupled, _._2)
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
          selection.marked.foreach(patient =>
            startPatientPublisher.publish(patient)
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
    PracticeBus.addRightWidgetRequest.publish(widget.ele)

object PracticeService:
  def listRegisteredPatient(): Future[List[(Patient, Visit)]] =
    for (gen, wqList, visitMap, patientMap) <- Api.listWqueueFull()
    yield wqList.map(wq =>
      val v = visitMap(wq.visitId)
      val p = patientMap(v.patientId)
      (p, v)
    )

class PracticeMainUI:
  val choice = PullDownLink("患者選択")
  val records = div
  val ele = div(cls := "practice-main")(
    header,
    records(cls := "practice-records")
  )

  def header: HTMLElement = div(cls := "practice-header")(
    div("診察", cls := "practice-header-title"),
    choice.ele(cls := "practice-header-choice"),
    a("登録薬剤"),
    a("全文検索")
  )

class PracticeRight:
  val ui = new PracticeRightUI
  def ele = ui.ele

class PracticeRightUI:
  val ele = div(cls := "practice-right-column")
