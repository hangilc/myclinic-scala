package dev.myclinic.scala.web.practiceapp.practice.record.shinryou

import dev.fujiwara.domq.all.{*, given}
import org.scalajs.dom.HTMLElement

case class RegularPanel(
    leftNames: List[String],
    rightNames: List[String],
    bottomNames: List[String]
):
  val leftChecks: List[CheckLabel[String]] = makeChecks(leftNames)
  val rightChecks: List[CheckLabel[String]] = makeChecks(rightNames)
  val bottomChecks: List[CheckLabel[String]] = makeChecks(bottomNames)
  val checks = leftChecks ++ rightChecks ++ bottomChecks
  val ele = div(
    cls := "practice-shinryou-regular-panel",
    div(cls := "practice-shinryou-regular-panel-left", leftChecks.map(_.wrap(div))),
    div(cls := "practice-shinryou-regular-panel-right", rightChecks.map(_.wrap(div))),
    div(cls := "practice-shinryou-regular-panel-bottom", bottomChecks.map(_.wrap(div)))
  )

  def selected: List[String] =
    checks.map(_.selected).collect {
      case Some(name) => name
    }

  def makeChecks(names: List[String]): List[CheckLabel[String]] =
    names.map(name => CheckLabel[String](name, name))
