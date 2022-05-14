package dev.myclinic.scala.formatshohousen

import scala.io.Source

object Sweep:
  def sweep(): Unit =
    val file = Source.fromFile("./work/shohou-delim.txt")
    val text = file.getLines.map(s => {
      s.replace("ナゾネックス点鼻液５０μｇ５６噴霧用　５ｍｇ１０ｇ", "ナゾネックス点鼻液５０μｇ５６噴霧用　１瓶")
    }).mkString("\n")
    file.close
    val items = text.split("DELIM\n")
    val (success, fail) = items.foldLeft((0, 0)) {
      case ((success, fail), s) => {
        val f = FormatShohousen.parseItem(s)
        if f.isInstanceOf[FallbackFormatter] then (success, fail + 1)
        else (success + 1, fail)
      }
    }
    println(s"${success}/${success + fail}")
    items
      .flatMap(item => FormatShohousen.splitToParts(item))
      .find(s => {
        val f = FormatShohousen.parseItem(s)
        if f.isInstanceOf[FallbackFormatter] then
          println(s)
          true
        else false
      })
