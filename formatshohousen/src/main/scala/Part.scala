package dev.myclinic.scala.formatshohousen

case class Part(
  item: Item,
  trails: List[String],
  commands: List[String]
):
  def formatForDisp(index: Int, ctx: FormatContext): List[String] =
    item.formatForDisp(index, ctx) ++ trails ++ commands

  val filterOutCommandsForPrint = List("@_raw")
  def excludeForPrint(cmd: String): Boolean = filterOutCommandsForPrint.contains(cmd)

  def formatForPrint(index: Int, ctx: FormatContext): List[String] =
    item.formatForPrint(index, ctx) 
      ++ Formatter.breakLines(trails, ctx.lineSize) 
      ++ commands.filter(!excludeForPrint(_))

