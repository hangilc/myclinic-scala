package dev.myclinic.scala.formatshohousen

import scala.io.Source

object Sweep:
  def readSamples: String =
    val file = Source.fromFile("./work/shohou-sample.txt")
    val text = file.getLines.mkString("\n")
    file.close
    FormatUtil.prepareForFormat(text)

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
        val trails: List[String] = post.map(line => {
          if line.startsWith("＠") then
            FormatUtil.restoreCommandLine(line)
          else line
        })
        val ctx = FormatContext(1)
        println(part)
        println(item.format(1, ctx).mkString("\n").map(c => {
          c match {
            case FormatUtil.softNewlineChar => '\n'
            case FormatUtil.softBlankChar => '　'
            case _ => c
          }
        }))
        println(trails)
        println
      })
    })
