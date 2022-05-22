package dev.myclinic.scala.formatshohousen

case class ShohouRaw(text: String) extends Shohou:
    def formatForDisp: String = text
    def formatForPrint: String = text
    def formatForSave: String = text

object ShohouRaw:
  val rawCommand = raw"(?:^|\n)@raw".r

  def tryParse(src: String): Option[ShohouRaw] =
    if rawCommand.findFirstIn(src).isDefined then Some(ShohouRaw(src))
    else None
