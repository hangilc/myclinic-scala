package dev.myclinic.scala.config

import java.time.LocalDate
import dev.fujiwara.kanjidate.DateUtil.given
import scala.language.implicitConversions

case class MasterTransition(
  shinryou: MasterTransitionRules = MasterTransitionRules(),
  kizai: MasterTransitionRules = MasterTransitionRules(),
  byoumei: MasterTransitionRules = MasterTransitionRules(),
  shuushokugo: MasterTransitionRules = MasterTransitionRules(),
  yakuzai: MasterTransitionRules = MasterTransitionRules()
)

case class MasterTransitionRules(
  rules: List[MasterTransitionRule] = List.empty
):
  def extend(rule: MasterTransitionRule): MasterTransitionRules =
    this.copy(rules = rules :+ rule)
  def transit(code: Int, at: LocalDate): Int =
    rules.foldLeft(code)((cur, rule) => rule.transit(cur, at))

case class MasterTransitionRule(fromCode: Int, at: LocalDate, toCode: Int):
  def transit(code: Int, date: LocalDate): Int =
    if code == fromCode && date >= at then toCode else code

object MasterTransitionRule:
  val pattern = raw"([YSKDA]),(\d+),(\d{4}-\d{2}-\d{2}),(\d+).*".r
  val comment = raw";.+".r
  val blank = raw"\s*".r
  def parse(s: String): Option[(String, MasterTransitionRule)] =
    s match {
      case pattern(kind, fromCode, at, toCode) =>
        Some(kind, MasterTransitionRule(fromCode.toInt, LocalDate.parse(at), toCode.toInt))
      case comment() => None
      case blank() => None
      case _ => 
        System.err.println("Cannot parse MasterTransitionRule: " + s)
        None
    }
