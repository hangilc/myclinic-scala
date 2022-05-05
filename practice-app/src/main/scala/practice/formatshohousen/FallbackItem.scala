package dev.myclinic.scala.web.practiceapp.practice.formatshohousen

class FallbackItem(lines: List[String]) extends ShohousenItem:
  def render(index: Int): String = 
    lines.mkString("\n") + "\n"

