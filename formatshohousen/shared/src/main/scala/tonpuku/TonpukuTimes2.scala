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
  usage: String,
  times: String
) extends Formatter:
  def format(index: Int, ctx: FormatContext): String =
    val (ipre, bpre) = FormatUtil.composePre(index, ctx)
    List(
      FormatUtil.softSplitLine(ipre, name, ctx),
      FormatUtil.formatTabLine(bpre, usage, times, ctx)
    ).mkString("\n")

object TonpukuTimes2:
  val drug = s".*$notSpace"
  val amount = s"[１一]回$digit+(?:錠)"
  val usage1 = s".*$amount.*時(?:.*$notSpace)?"
  val usage2 = s".+時.*$amount(?:.*$notSpace)?"
  val usage = s"$usage1|$usage2"
  val times = s"$digit+回分"
  val delim = "[。、]"
  val sep = s"(?:(?:(?<=$delim)$space*)|$space+)"
  val pattern = s"($drug)$space+($usage)$sep($times)$space*".r

  def tryParse(lead: String, more: List[String]): Option[TonpukuTimes2] =
    (lead :: more).mkString(zenkakuSpace) match {
      case pattern(name, usage, times) =>
        Some(new TonpukuTimes2(name, usage, times))
      case _ => None
    }


