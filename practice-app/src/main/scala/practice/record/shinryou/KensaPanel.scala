package dev.myclinic.scala.web.practiceapp.practice.record.shinryou

import dev.fujiwara.domq.all.{*, given}
import PanelHelper.{toElement, makeChecks, collectChecks, listSelected}

final case class KensaPanel(config: Map[String, List[String]]):
  val ele = div(
    cls := "practice-shinryou-kensa-panel",
    div(cls := "practice-shinryou-kensa-panel-left", leftChecks.map(toElement _)),
    div(cls := "practice-shinryou-kensa-panel-right", rightChecks.map(toElement _))
  )

  def leftChecks: List[CheckLabel[String] | Unit] =
    makeChecks(config("left"))

  def rightChecks: List[CheckLabel[String] | Unit] =
    makeChecks(config("right"))

  val preset: List[String] = config("preset")


