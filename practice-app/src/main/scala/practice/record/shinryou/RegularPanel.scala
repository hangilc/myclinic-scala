package dev.myclinic.scala.web.practiceapp.practice.record.shinryou

import dev.fujiwara.domq.all.{*, given}
import org.scalajs.dom.HTMLElement
import PanelHelper.{toElement, makeChecks, collectChecks, listSelected}

case class RegularPanel(
    leftNames: List[String],
    rightNames: List[String],
    bottomNames: List[String]
):
  val leftChecks: List[CheckLabel[String] | Unit] = makeChecks(leftNames)
  val rightChecks: List[CheckLabel[String] | Unit] = makeChecks(rightNames)
  val bottomChecks: List[CheckLabel[String] | Unit] = makeChecks(bottomNames)
  val checks = collectChecks(leftChecks ++ rightChecks ++ bottomChecks)
  
  val ele = div(
    cls := "practice-shinryou-regular-panel",
    div(cls := "practice-shinryou-regular-panel-left", leftChecks.map(toElement _)),
    div(cls := "practice-shinryou-regular-panel-right", rightChecks.map(toElement _)),
    div(cls := "practice-shinryou-regular-panel-bottom", bottomChecks.map(toElement _))
  )

  def selected: List[String] = listSelected(checks)

