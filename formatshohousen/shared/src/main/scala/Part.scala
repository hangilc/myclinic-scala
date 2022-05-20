package dev.myclinic.scala.formatshohousen

case class Part(
  item: Item,
  trails: List[String],
  commands: List[String]
):
  def formatForDisp(index: Int, ctx: FormatContext): List[String] =
    item.formatForDisp(index, ctx) ++ trails ++ commands

