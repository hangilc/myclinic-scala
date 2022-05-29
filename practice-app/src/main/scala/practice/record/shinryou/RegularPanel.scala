package dev.myclinic.scala.web.practiceapp.practice.record.shinryou

import dev.fujiwara.domq.all.{*, given}
import org.scalajs.dom.HTMLElement

case class RegularPanel(
    leftNames: List[String],
    rightNames: List[String],
    bottomNames: List[String]
):
  val leftChecks: List[CheckLabel[String] | Unit] = makeChecks(leftNames)
  val rightChecks: List[CheckLabel[String] | Unit] = makeChecks(rightNames)
  val bottomChecks: List[CheckLabel[String] | Unit] = makeChecks(bottomNames)
  val checks = (leftChecks ++ rightChecks ++ bottomChecks).collect {
    case a if a.isInstanceOf[CheckLabel[String]] => a.asInstanceOf[CheckLabel[String]]
  }
  val ele = div(
    cls := "practice-shinryou-regular-panel",
    div(cls := "practice-shinryou-regular-panel-left", leftChecks.map(toElement _)),
    div(cls := "practice-shinryou-regular-panel-right", rightChecks.map(toElement _)),
    div(cls := "practice-shinryou-regular-panel-bottom", bottomChecks.map(toElement _))
  )

  def toElement(a: CheckLabel[String] | Unit): HTMLElement = 
    a match {
      case () => div(cls := "practice-shinryou-regular-panel-space")
      case c: CheckLabel[String] => c.wrap(div)
    }

  def selected: List[String] =
    checks.map(_.selected).collect {
      case Some(name) => name
    }

  def makeChecks(names: List[String]): List[CheckLabel[String] | Unit] =
    names.map {
      case "---" => ()
      case name =>  CheckLabel[String](name, name)
    }

