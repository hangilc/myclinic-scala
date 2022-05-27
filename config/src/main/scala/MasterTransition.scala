package dev.myclinic.scala.config

import java.time.LocalDate
import dev.myclinic.scala.util.DateUtil.given

case class MasterTransition(
  shinryouRules: MasterTransitionRules
)

case class MasterTransitionRules(
  rules: List[MasterTransitionRule]
):
  def transit(code: Int, at: LocalDate): Int =
    rules.foldLeft(code)((cur, rule) => rule.transit(cur, at))

case class MasterTransitionRule(fromCode: Int, at: LocalDate, toCode: Int):
  def transit(code: Int, date: LocalDate): Int =
    if code == fromCode && date >= at then toCode else code
