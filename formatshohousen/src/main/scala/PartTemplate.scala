package dev.myclinic.scala.formatshohousen

case class PartTemplate(lines: List[String], trails: List[String], commands: List[String]):
  def toPart: Part =
    Part(item, trails, commands)

  def item: Item =
    if commands.contains("@_raw") then
      Item(List.empty, None, lines)
    else
      Item.parse(lines)
