package dev.myclinic.scala.formatshohousen.gaiyou

import dev.myclinic.scala.formatshohousen.Formatter
import dev.myclinic.scala.formatshohousen.FormatContext
import dev.myclinic.scala.formatshohousen.FormatUtil
import dev.myclinic.scala.formatshohousen.RegexPattern.*

class GaiyouShippu(name: String, amount: String, usage: String) extends Formatter:
  def format(index: Int, ctx: FormatContext): String =
    val (ipre, bpre) = FormatUtil.composePre(index, ctx)
    List(
      FormatUtil.softSplitLine(ipre, s"$name$zenkakuSpace$amount", ctx.lineSize),
      FormatUtil.softSplitLine(bpre, usage, ctx.lineSize)
    ).mkString("\n")


object GaiyouShippu:
  val unit = "(?:枚|パック)"
  val drugPart = s"($drugNameRegex)$space+($digit+$unit)".r
  val usagePart1 = s"(１日$digit+回.*)".r
  val usagePart2 = s"(１回$digit+枚.*)".r
  def tryParse(lead: String, more: List[String]): Option[GaiyouShippu] =
    (lead match {
      case drugPart(name, amount) => Some(name, amount)
      case _ => None
    }) .flatMap {
      case (name, amount) => 
        more.mkString(zenkakuSpace) match {
          case usagePart1(usage) => Some(new GaiyouShippu(name, amount, usage))
          case usagePart2(usage) => Some(new GaiyouShippu(name, amount, usage))
          case _ => None
        }
    }
    

