package dev.myclinic.scala.web.practiceapp.practice

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.all.{*, given}
import org.scalajs.dom.HTMLElement
import scala.concurrent.Future
import dev.myclinic.scala.model.Patient
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.appbase.Selections
import dev.myclinic.scala.model.Visit
import dev.myclinic.scala.web.appbase.LocalEventPublisher

class PracticeService extends SideMenuService:
  val left = new PracticeMain
  val right = new PracticeRight

  override def getElements = List(left.ele, right.ele)

  left.startPatientPublisher.subscribe(patient => println(("start-patient", patient)))
  left.startVisitPublisher.subscribe((patient, visit) => println(("start-visit", patient, visit)))

class PracticeMain:
  val startPatientPublisher = LocalEventPublisher[Patient]
  val startVisitPublisher = LocalEventPublisher[(Patient, Visit)]
  val ui = new PracticeMainUI
  def ele = ui.ele
  ui.choice.setBuilder(List(
      "受付患者選択" -> (selectFromRegistered _),
      "患者検索" -> (() => ()),
      "最近の診察" -> (() => ()),
      "日付別" -> (() => ())
  ))

  def selectFromRegistered(): Unit =
    import dev.fujiwara.domq.ModalDialog3
    for
      pairs <- PracticeService.listRegisteredPatient()
    yield
      val sel = Selections.patientSelectionWithData[Visit]()
      sel.addItems(pairs)
      val d = new ModalDialog3
      d.title("受付患者選択")
      d.body(sel.ele)
      d.commands(
        button("選択", onclick := (() => 
          sel.selected.foreach(pair => 
            d.close()
            startVisitPublisher.publish(_)
      ))),
        button("キャンセル", onclick := (() => d.close()))
      )
      d.open()

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


