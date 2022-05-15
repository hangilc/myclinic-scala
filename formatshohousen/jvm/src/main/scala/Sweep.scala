package dev.myclinic.scala.formatshohousen

import scala.io.Source

object Sweep:
  def readSamples: String =
    val file = Source.fromFile("./work/shohou-sample.txt")
    val text = file.getLines.mkString("\n")
    file.close
    FormatUtil.convertToZenkaku(text)

  def splitToShohou(text: String): List[String] =
    val delim = raw"\n\n+".r
    delim.split(text).toList

  def sweep(): Unit =
    val text = readSamples
    val shohouList = splitToShohou(text).map("１）" + _)
    shohouList.foreach(shohou => {
      val parts = FormatUtil.splitToParts(shohou)
      parts.foreach(part => {
        val (pre, post) = FormatUtil.splitToSubparts(part)
        val item = Item.parse(pre)
        println(item)
        println(post)
        println
      })
    })
