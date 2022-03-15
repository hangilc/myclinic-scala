package dev.myclinic.scala.web.practiceapp.practice

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.searchform.*
import org.scalajs.dom.HTMLElement
import scala.concurrent.Future
import dev.myclinic.scala.model.Patient
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.appbase.Selections
import dev.myclinic.scala.model.Visit
import dev.myclinic.scala.web.appbase.LocalEventPublisher
import dev.fujiwara.kanjidate.KanjiDate
import scala.util.Success
import scala.util.Failure

class PracticeService extends SideMenuService:
  val left = new PracticeMain
  val right = new PracticeRight

  override def getElements = List(left.ele, right.ele)

  left.startPatientPublisher.subscribe(patient => println(("start-patient", patient)))
  left.startVisitPublisher.subscribe(patient => println(("start-visit", patient)))

class PracticeMain:
  val startPatientPublisher = LocalEventPublisher[Patient]
  val startVisitPublisher = LocalEventPublisher[Patient]
  val ui = new PracticeMainUI
  def ele = ui.ele
  ui.choice.setBuilder(List(
      "受付患者選択" -> (selectFromRegistered _),
      "患者検索" -> (selectBySearchPatient _),
      "最近の診察" -> (selectFromRecentVisits _),
      "日付別" -> (() => ())
  ))

  def selectFromRegistered(): Unit =
    for
      pairs <- PracticeService.listRegisteredPatient()
    yield
      val sel = Selections.patientSelectionWithData[Visit]()
      sel.clear()
      sel.addAll(pairs)
      val d = new ModalDialog3
      d.title("受付患者選択")
      d.body(sel.ele(cls := "practice-select-from-registered-selection"))
      d.commands(
        button("選択", onclick := (() => 
          sel.selected.foreach(pair => 
            d.close()
            startVisitPublisher.publish(_)
      ))),
        button("キャンセル", onclick := (() => d.close()))
      )
      d.open()

  def selectBySearchPatient(): Unit =
    val d = new ModalDialog3
    val search = new SearchForm[Patient, Patient](identity, Api.searchPatient(_).map(_._2))
    search.ui.selection.formatter = PracticeService.patientFormatter
    d.title("患者検索")
    d.body(
      search.ele
    )
    d.commands(
      button("診察登録", onclick := (() => {
        d.close()
        search.selected.foreach(patient => startVisitPublisher.publish(patient))
      })),
      button("選択", onclick := (() => {
        d.close()
        search.selected.foreach(patient => startPatientPublisher.publish(patient))
      })),
      button("閉じる", onclick := (() => d.close()))
    )
    d.open()
    search.ui.input.focus()

  def selectFromRecentVisits(): Unit =
    var offset = 0
    val count = 20
    val d = new ModalDialog3
    val selection = new Selection[(Visit, Patient), Patient](_._2)
    selection.formatter = (visit, patient) => 
      String.format("%04d %s [%s]", patient.patientId, patient.fullName(),
        KanjiDate.dateToKanji(visit.visitedAt.toLocalDate))
    def update(): Future[Unit] =
      for
        result <- Api.listRecentVisitFull(offset, count)
      yield
        selection.clear()
        selection.addAll(result)
    d.body(
      selection.ele,
      div(
        a("前へ", onclick := (() => {
          offset -= count
          if offset < 0 then offset = 0
          update()
          ()
        })),
        a("次へ", onclick := (() => {
          offset += count
          update()
          ()
        }))
      )
    )
    d.commands(
      button("選択", onclick := (() => {
        d.close()
        selection.selected.foreach(patient => startPatientPublisher.publish(patient))
      })),
      button("キャンセル", onclick := (() => d.close()))
    )
    update().onComplete {
      case Success(_) => d.open()
      case Failure(ex) => System.err.println(ex.getMessage)
    }

      

object PracticeService:
  def listRegisteredPatient(): Future[List[(Patient, Visit)]] =
    for
      (gen, wqList, visitMap, patientMap) <- Api.listWqueueFull()
    yield
      wqList.map(wq => 
        val v = visitMap(wq.visitId)
        val p = patientMap(v.patientId)
        (p, v)  
      )

  val patientFormatter: Patient => String = patient =>
    String.format("%04d %s (%s)", patient.patientId, patient.fullName(), 
      KanjiDate.dateToKanji(patient.birthday))
    

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


