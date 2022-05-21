package dev.myclinic.scala.formatshohousen

case class ShohouRaw(text: String)

object ShohouRaw:
  given shohouRawShohou: Shohou[ShohouRaw] with
    def formatForDisp(s: ShohouRaw): String = s.text
    def formatForPrint(s: ShohouRaw): String = s.text

    def formatForSave(s: ShohouRaw): String = s.text

  val rawCommand = raw"(?:^|\n)@raw".r

  def tryParse(src: String): Option[ShohouRaw] =
    if rawCommand.findFirstIn(src).isDefined then Some(ShohouRaw(src))
    else None
