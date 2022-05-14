package dev.myclinic.scala.formatshohousen.tonpuku

import dev.myclinic.scala.formatshohousen.Formatter
import dev.myclinic.scala.formatshohousen.FormatContext
import dev.myclinic.scala.formatshohousen.RegexPattern.*
import dev.myclinic.scala.formatshohousen.FormatUtil

// Example
// リレンザ　２０ブリスター
// 　　１回２ブリスター、１日２回

class TonpukuTotal(
  name: String,
  amount: String,
  usage: String
) extends Formatter:
  def format(index: Int, ctx: FormatContext): String =
    val (ipre, bpre) = FormatUtil.composePre(index, ctx)
    List(
      FormatUtil.formatTabLine(ipre, name, amount, ctx),
      FormatUtil.softSplitLine(bpre, usage, ctx)
    ).mkString("\n")

object TonpukuTotal:
  val name = s".*$notSpace"
  val unit = "(?:錠|カプセル|ブリスター|個|ｍＬ)"
  val amount = s"$digit+$unit"
  val perUsage = s"[１一]回$digit$unit"
  val delimComma = s"[、。]$space*"
  val delimSpace = s"$space+"
  val delim = s"(?:$delimComma|$delimSpace)"
  val usage1 = s"[１一]回$digit$unit${delim}１日${digit}回(?:.*$notSpace)?"
  val usage2 = s"[１一]回$digit$unit${delim}.+時(?:.*$notSpace)?"
  val usage3 = s"(?:稀釈して|希釈して)１日${digit}回(?:.*$notSpace)?"
  val usage = s"(?:$usage1|$usage2|$usage3)"
  val pat = s"($name)$space+($amount)$space+($usage)$space*".r

  def tryParse(lead: String, more: List[String]): Option[TonpukuTotal] =
    (lead :: more).mkString(zenkakuSpace) match {
      case pat(name, amount, usage) => 
        Some(new TonpukuTotal(name, amount, usage))
      case _ => None
    }

