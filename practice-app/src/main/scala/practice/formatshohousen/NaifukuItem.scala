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
  val firstLine = raw"\s*(.+)\s+(\d.*(?:錠|g|カプセル))\s*".r
  def tryParse(h: String, t: List[String]): Option[ShohousenItem] =
    println(("first-line", h))
    h :: t match {
      case List(firstLine, _) => 
        println("Naifuku")
        None
      case _ => None
    }