package dev.myclinic.scala.formatshohousen.tonpuku

import dev.myclinic.scala.formatshohousen.Formatter
import dev.myclinic.scala.formatshohousen.FormatContext
import dev.myclinic.scala.formatshohousen.RegexPattern.*

class TonpukuTimes(
  name: String,
  amount: String,
  usage: String,
  times: String
) extends Formatter:
  def format(index: Int, ctx: FormatContext): String =
    ???

object TonpukuTimes:
  val unit = "(?:錠)"
  val drugLine = s"($drugNameRegex)$space+((?:[一１]回)?$digit+$unit)"
  val usage1 = s"$notSpace.*時.*$notSpace"
  val usage2 = s"$notSpace.*時"
  val usageLine = s"($usage1|$usage2)$space+($digit+回分)"
  val drugLinePattern = drugLine.r
  val usageLinePattern = usageLine.r
  def tryParse(lead: String, more: List[String]): Option[TonpukuTimes] =
    (lead :: more) match {
      case List(drugLinePattern(name, amount), usageLinePattern(usage, times)) =>
        Some(new TonpukuTimes(name, amount, usage, times))
      case _ => None
    }

