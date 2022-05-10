package dev.myclinic.scala.formatshohousen

object FormatShohousen:
  val ShohousenPrefix = "院外処方\nＲｐ）\n"

  def extractPrefix(s: String): Option[(String, String)] =
    if s.startsWith(ShohousenPrefix) then
      Some(ShohousenPrefix, s.substring(ShohousenPrefix.size))
    else None