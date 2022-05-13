package dev.myclinic.scala.formatshohousen.tonpuku

import dev.myclinic.scala.formatshohousen.Formatter
import dev.myclinic.scala.formatshohousen.FormatContext
import dev.myclinic.scala.formatshohousen.RegexPattern.*
import dev.myclinic.scala.formatshohousen.FormatUtil

// Example:
// ４）カロナール（３００）
// 　　１回１錠、頭痛時。１０回分

class TonpukuTimes2(
  name: String,
  amount: String,
  usage: String,
  times: String
) extends Formatter:
  def format(index: Int, ctx: FormatContext): String =
    val (ipre, bpre) = FormatUtil.composePre(index, ctx)
    List(
      FormatUtil.formatTabLine(ipre, name, amount, ctx),
      FormatUtil.formatTabLine(bpre, usage, times, ctx)
    ).mkString("\n")

object TonpukuTimes2:
  val unit = "(?:錠)"
  val drugLine = s"($drugNameRegex)$space+((?:[一１]回)?$digit+$unit)"
  val usage1 = s"$notSpace.*時.*$notSpace"
  val usage2 = s"$notSpace.*時"
  val usageLine = s"($usage1|$usage2)$space+($digit+回分)"
  val drugLinePattern = s"$drugLine$space*".r
  val usageLinePattern = s"$usageLine$space*".r

  def tryParse(lead: String, more: List[String]): Option[TonpukuTimes2] =
    (lead :: more) match {
      case List(drugLinePattern(name, amount), usageLinePattern(usage, times)) =>
        Some(new TonpukuTimes2(name, amount, usage, times))
      case _ => None
    }

  val oneline = s"$drugLine$space+$usageLine"
  val onelinePattern = s"$oneline$space*".r

  def tryParseOneLine(lead: String, more: List[String]): Option[TonpukuTimes2] =
    (lead :: more).mkString(zenkakuSpace) match {
      case onelinePattern(name, amount, usage, times) =>
        Some(new TonpukuTimes2(name, amount, usage, times))
      case _ => None
    }


