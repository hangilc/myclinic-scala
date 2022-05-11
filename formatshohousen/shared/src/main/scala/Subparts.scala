package dev.myclinic.scala.formatshohousen

case class Subparts(
  leadLine: String,
  lines: List[String],
  trails: List[String],
  commands: List[String]
)