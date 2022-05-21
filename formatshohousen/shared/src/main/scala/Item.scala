package dev.myclinic.scala.formatshohousen

import FormatPattern.*

case class Item(
    drugs: List[DrugPart],
    usage: Option[UsagePart],
    more: List[String]
):
  def formatForPrint(index: Int, ctx: FormatContext): List[String] =
    val (ipre, bpre) = FormatUtil.composePre(index, ctx)
    val lsize = ctx.lineSize - ipre.size
    val lines =
      drugs.flatMap(d =>
        Formatter.breakTabLine(
          d.name,
          d.amount,
          ctx.tabPos - ipre.size,
          lsize
        )
      ) ++ usage.fold(List.empty)(u => {
        Formatter.breakTabLine(u.usage, u.daysTimes, ctx.altTabPos - ipre.size, lsize)
      }) ++ more.flatMap(m => Formatter.breakLine(m, lsize))
    Formatter.indent(ipre, bpre, lines)

  def formatForDisp(index: Int, ctx: FormatContext): List[String] =
    val (ipre, bpre) = FormatUtil.composePre(index, ctx)
    val lines =
      drugs.map(d => s"${d.name}$zenkakuSpace${d.amount}")
        ++ usage.fold(List.empty)(u =>
          List(s"${u.usage}$zenkakuSpace${u.daysTimes}")
        )
        ++ more
    val blines = Formatter.breakLines(lines, ctx.lineSize - ipre.size)
    Formatter.indent(ipre, bpre, blines)

object Item:
  val unit = "(?:錠|カプセル|ｇ|ｍｇ|包|ｍＬ|ブリスター|瓶|個|キット|枚|パック|袋|本)"
  val chunk = s"$notSpace(?:.*$notSpace)?"
  val days1 = s"$digit+日分"
  val days2 = s"$digit+日分"
  val drugPattern =
    s"($chunk)$space+((?:１回)?$digitOrPeriod+$unit(?:$chunk)?)$space*".r
  val daysPart = s"$digit+(?:日|回)分"
  val usagePattern = s"($chunk)$space+($daysPart(?:$chunk)?)$space*".r

  def parseDrugPart(src: String): Option[DrugPart] =
    src match {
      case drugPattern(name, amount) => Some(DrugPart(name, amount))
      case _                         => None
    }

  def parseUsagePart(src: String): Option[UsagePart] =
    src match {
      case usagePattern(usage, days) => Some(UsagePart(usage, days))
      case _                         => None
    }

  def parse(lines: List[String]): Item =
    val (drugs, rest) = FormatUtil.span(lines, parseDrugPart _)
    val (usage, more) = rest match {
      case r @ (h :: t) =>
        parseUsagePart(h) match {
          case Some(u) => (Some(u), t)
          case None    => (None, r)
        }
      case r => (None, r)
    }
    Item(drugs, usage, more)
