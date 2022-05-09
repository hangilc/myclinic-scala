package dev.myclinic.scala.web.practiceapp.practice.formatshohousen

class FallbackItem(h: String, t: List[String]) extends ShohousenItem:
  def render(index: Int, ctx: FormatContext): List[String] = 
    (FormatShohousen.indexToZenkaku(index) + h) :: t

