package dev.myclinic.scala.web.practiceapp.practice.record.shinryou

import dev.fujiwara.domq.all.{*, given}
import PanelHelper.{toElement, makeChecks, collectChecks, listSelected, clearChecks}

final case class KensaPanel(config: Map[String, List[String]]):
  val leftChecks: List[CheckLabel[String] | Unit] =
    makeChecks(config("left"))

  val rightChecks: List[CheckLabel[String] | Unit] =
    makeChecks(config("right"))

  val checks: List[CheckLabel[String]] = collectChecks(leftChecks ++ rightChecks)

  val ele = div(
    cls := "practice-shinryou-kensa-panel",
    div(cls := "practice-shinryou-kensa-panel-left", leftChecks.map(toElement _)),
    div(cls := "practice-shinryou-kensa-panel-right", rightChecks.map(toElement _))
  )

  val preset: Set[String] = Set(config("preset"): _*)

  def checkPreset: Unit =
    checks.filter(check => preset.contains(check.value)).foreach(_.check)

  def clear: Unit = clearChecks(checks)



