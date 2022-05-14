package dev.myclinic.scala.formatshohousen.gaiyou

import dev.myclinic.scala.formatshohousen.Formatter
import dev.myclinic.scala.formatshohousen.FormatContext
import dev.myclinic.scala.formatshohousen.FormatUtil
import dev.myclinic.scala.formatshohousen.RegexPattern.*

class GaiyouDrop(name: String, amount: String, usage: String) extends Formatter:
  def format(index: Int, ctx: FormatContext): String =
    val (ipre, bpre) = FormatUtil.composePre(index, ctx)
    List(
      FormatUtil.softSplitLine(ipre, s"$name$zenkakuSpace$amount", ctx.lineSize),
      FormatUtil.softSplitLine(bpre, usage, ctx.lineSize)
    ).mkString("\n")


object GaiyouDrop:
  val unit = "(?:ｍＬ|ｇ|ブリスター|瓶|個)"
  val drugPart = s"($drugNameRegex)$space+($digit+$unit)"
  val drugPattern = drugPart.r
  val usageStart1 = s"１日$digit+回"
  val usageStart2 = s"１回$digit+(?:滴|ｇ|噴霧|吸入)"
  val usageStart = s"(?:$usageStart1|$usageStart2)"
  val usage = s"($usageStart(?:.*$notSpace)?)"
  val usagePattern = s"$usage$space*".r
  def tryParse(lead: String, more: List[String]): Option[GaiyouDrop] =
    (lead match {
      case drugPattern(name, amount) => Some(name, amount)
      case _ => None
    }) .flatMap {
      case (name, amount) => 
        more.mkString(zenkakuSpace) match {
          case usagePattern(usage) => Some(new GaiyouDrop(name, amount, usage))
          case _ => None
        }
    }

  val onelineRegex = s"$drugPart$space+$usage"
  val onelinePattern = s"$onelineRegex$space*".r

  def tryParseOneLine(lead: String, more: List[String]): Option[GaiyouDrop] =
    (lead :: more).mkString(zenkakuSpace) match {
      case onelinePattern(name, amount, usage) => Some(new GaiyouDrop(name, amount, usage))
      case _ => None
    }
    

