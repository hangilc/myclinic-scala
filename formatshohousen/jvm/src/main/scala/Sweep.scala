package dev.myclinic.scala.formatshohousen

import scala.io.Source

object Sweep:
  def sweep(): Unit =
    val file = Source.fromFile("./work/shohou-delim.txt")
    val text = file.getLines.mkString("\n")
    file.close
    val items = text.split("DELIM\n")
    items.flatMap(item => FormatShohousen.splitToParts(item))
      .find(s => {
        val f = FormatShohousen.parseItem(s)
        if f.isInstanceOf[FallbackFormatter] then
          println(s)
          true
        else false
      })