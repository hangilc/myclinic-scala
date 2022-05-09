package dev.myclinic.scala.web.practiceapp.practice.formatshohousen

case class NaifukuItem(
  drug: String,
  amount: String,
  usage: String,
  days: String
) extends ShohousenItem:
  def render(index: Int, ctx: FormatContext): List[String] =
    ???


object NaifukuItem:
  val firstLine = raw"".r
  def tryParse(h: String, t: List[String]): Option[ShohousenItem] =
    ???