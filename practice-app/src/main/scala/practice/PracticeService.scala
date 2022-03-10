package dev.myclinic.scala.web.practiceapp.practice

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.all.{*, given}

class PracticeService extends SideMenuService:
  val ele = div

  def getElement = ele

class PracticeServiceUI:
  val leftColumn = div
  val rightColumn = div
  val ele = div(cls := "practice-workarea")(
    leftColumn(cls := "practice-left-column"),
    rightColumn
  )

