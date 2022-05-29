package dev.myclinic.scala.web.practiceapp.practice.record.shinryou

import dev.fujiwara.domq.all.{*, given}
import org.scalajs.dom.HTMLElement

object PanelHelper:

  val pattern1 = raw"(.+):(.+)".r

  def makeChecks(names: List[String]): List[CheckLabel[String] | Unit] =
    names.map {
      case "---" => ()
      case pattern1(label, name) => CheckLabel[String](name, label)
      case name  => CheckLabel[String](name, name)
    }

  def toElement(a: CheckLabel[String] | Unit): HTMLElement =
    a match {
      case ()                    => div(cls := "practice-shinryou-panel-space")
      case c: CheckLabel[String] => c.wrap(div)
    }

  def collectChecks(
      list: List[CheckLabel[String] | Unit]
  ): List[CheckLabel[String]] =
    list.collect {
      case a if a.isInstanceOf[CheckLabel[String]] =>
        a.asInstanceOf[CheckLabel[String]]
    }

  def listSelected(checks: List[CheckLabel[String]]): List[String] =
    checks.map(_.selected).collect { case Some(name) => name }

  def clearChecks(checks: List[CheckLabel[String]]): Unit = checks.foreach(_.uncheck)
