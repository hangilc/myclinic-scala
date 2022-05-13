package dev.myclinic.scala.formatshohousen.gaiyou

import dev.myclinic.scala.formatshohousen.Formatter
import dev.myclinic.scala.formatshohousen.FormatContext
import dev.myclinic.scala.formatshohousen.FormatUtil
import dev.myclinic.scala.formatshohousen.RegexPattern.*

class GaiyouCream(name: String, amount: String, usage: String) extends Formatter:
  def format(index: Int, ctx: FormatContext): String =
    val (ipre, bpre) = FormatUtil.composePre(index, ctx)
    List(
      FormatUtil.softSplitLine(ipre, s"$name$zenkakuSpace$amount", ctx.lineSize),
      FormatUtil.softSplitLine(bpre, usage, ctx.lineSize)
    ).mkString("\n")


object GaiyouCream:
  val unit = "(?:ｇ)"
  val drugPart = s"($drugNameRegex)$space+($digit+$unit)"
  val drugPattern = drugPart.r
  val usageStart1 = s"１日$digit+回"
  val usageStart = s"(?:$usageStart1)"
  val usage = s"($usageStart(?:.*$notSpace)?)"
  val usagePattern = s"$usage$space*".r
  def tryParse(lead: String, more: List[String]): Option[GaiyouCream] =
    (lead match {
      case drugPattern(name, amount) => Some(name, amount)
      case _ => None
    }) .flatMap {
      case (name, amount) => 
        more.mkString(zenkakuSpace) match {
          case usagePattern(usage) => Some(new GaiyouCream(name, amount, usage))
          case _ => None
        }
    }

  val onelineRegex = s"$drugPart$space+$usage"
  val onelinePattern = s"$onelineRegex$space*".r

  def tryParseOneLine(lead: String, more: List[String]): Option[GaiyouCream] =
    (lead :: more).mkString(zenkakuSpace) match {
      case onelinePattern(name, amount, usage) => Some(new GaiyouCream(name, amount, usage))
      case _ => None
    }
    

