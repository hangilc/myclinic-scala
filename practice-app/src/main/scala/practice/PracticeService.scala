package dev.myclinic.scala.web.practiceapp.practice

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.all.{*, given}
import org.scalajs.dom.HTMLElement

class PracticeService extends SideMenuService:
  val left = new PracticeMain
  val right = new PracticeRight

  override def getElements = List(left.ele, right.ele)

class PracticeMain:
  val ui = new PracticeMainUI
  def ele = ui.ele
  ui.choice.setBuilder(List(
      "受付患者選択" -> (selectFromRegistered _),
      "患者検索" -> (() => ()),
      "最近の診察" -> (() => ()),
      "日付別" -> (() => ())
  ))

  def selectFromRegistered(): Unit =
    import dev.fujiwara.domq.ModalDialog
    val d = new ModalDialog
    d.content(button(onclick := (() => d.close())))
    d.open()
    

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


