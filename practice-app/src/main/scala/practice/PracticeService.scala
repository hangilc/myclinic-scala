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

class PracticeMainUI:
  val ele = div(cls := "practice-main")("MAIN")

class PracticeRight:
  val ui = new PracticeRightUI
  def ele = ui.ele

class PracticeRightUI:
  val ele = div(cls := "practice-right-column")("RIGHT")


