package dev.myclinic.scala.formatshohousen

case class PartTemplate(lines: List[String], trails: List[String], commands: List[String]):
  def toPart: Part =
    val item = Item.parse(lines)
    Part(item, trails, commands)

  def toRawPart: Part =
    val item = Item(List.empty, None, lines)
    Part(item, trails, commands)
