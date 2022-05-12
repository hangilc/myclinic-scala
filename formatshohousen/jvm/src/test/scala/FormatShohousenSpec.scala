package dev.myclinic.scala.formatshohousen

import org.scalatest.funsuite.AnyFunSuite
import FormatShohousen.*
import ShohouSample.*
import dev.myclinic.scala.formatshohousen.naifuku.NaifukuSimple
import FormatUtil.*

class FormatShohousenSpec extends AnyFunSuite:
  test("should split sample1 to item parts") {
    val parts = splitToParts(sample1)
    assert(parts == List(sample1))
  }

  test("shoule split sample2 to item parts") {
    val parts = splitToParts(sample2)
    assert(parts.size == 2)
    assert(parts(0) == "１）カロナール錠３００ｍｇ　３錠\n　　分３　毎食後　５日分")
    assert(parts(1) == "２）ロラタジン錠１０ｍｇ　１錠\n　　分１　朝食後　５日分")
  }

  test("should split sample1 to item subparts") {
    val item = splitToParts(sample1)(0)
    val subs = splitToSubparts(item)
    assert(subs.leadLine == "カロナール錠３００ｍｇ　３錠")
    assert(subs.lines == List("　　分３　毎食後　５日分"))
  }

  test("should parse NaifukuSimple firstPattern") {
    val s = "カロナール錠３００ｍｇ　３錠"
    val opt = NaifukuSimple.firstPattern.findPrefixMatchOf(s)
    assert(opt.isDefined)
    assert(opt.get.groupCount == 3)
    assert(opt.get.group(1) == "カロナール錠３００ｍｇ")
    assert(opt.get.group(2) == "３")
    assert(opt.get.group(3) == "錠")
  }

  test("should parse NaifukuSimple secondPattern") {
    val s = "　　分３　毎食後　５日分"
    val opt = NaifukuSimple.secondPattern.findPrefixMatchOf(s)
    assert(opt.isDefined)
    assert(opt.get.groupCount == 3)
    assert(opt.get.group(1) == "分３　毎食後")
    assert(opt.get.group(2) == "５")
    assert(opt.get.group(3) == "日分")
  }

  test("should parse NaifukuSimple") {
    val subs: Subparts = splitToSubparts(splitToParts(sample1)(0))
    val itemOpt = NaifukuSimple.tryParse(subs.leadLine, subs.lines)
    assert(itemOpt.isDefined)
    val item = itemOpt.get
    assert(item.name == "カロナール錠３００ｍｇ")
    assert(item.amount == "３")
    assert(item.unit == "錠")
    assert(item.usage == "分３　毎食後")
    assert(item.days == "５")
    assert(item.daysUnit == "日分")
    val ctx = FormatContext(1)
    assert(item.format(1, ctx) == List(
      "１）カロナール錠３００ｍｇ　　　　　　　　３錠",
      "　　分３　毎食後　　　　　　　　　　　　　５日分"
    ).mkString("\n"))
  }

  test("should not split following line") {
    val line = "続く文章"
    val pre = softBlank.toString * 2
    val lines = FormatUtil.softSplitLine(pre, line, 10)
    assert(lines == pre + line)
  }

  test("should split following line") {
    val line = "長く続く文章長く続く文章"
    val pre = softBlank.toString * 2
    val lines = FormatUtil.softSplitLine(pre, line, 8)
    assert(
      lines == List(
        pre + "長く続く文章" + softNewline,
        "^^" + "長く続く文章"
      ).mkString
    )
  }

  test("should not split lead line") {
    val line = "カロナール錠３００ｍｇ　３錠"
    val pre = "１）"
    val lines = FormatUtil.softSplitLine(pre, line, 31)
    assert(lines == pre + line)
  }

  test("should split lead line") {
    val line = "カロナール錠３００ｍｇ　３錠"
    val pre = "１）"
    val lines = FormatUtil.softSplitLine(pre, line, 8)
    assert(
      lines == List(
        pre + "カロナール錠" + softNewline,
        (softBlank.toString * 2) + "３００ｍｇ　" + softNewline,
        (softBlank.toString * 2) + "３錠"
      ).mkString
    )
  }

  test("should adjut line end space in split line") {
    val s = "文章　　続き"
    val pre = "　" * 2
    val lines = FormatUtil.softSplitLine(pre, s, 4)
    assert(
      lines == List(
        pre + "文章　　" + softNewline,
        (softBlank.toString * 2) + "続き"
      ).mkString
    )
  }

  test("should format with FallbackFormatter") {
    val ff = new FallbackFormatter(
      "カロナール錠３００ｍｇ　３錠",
      List("分３　毎食後　５日分")
    )
    val fmt = ff.format(1, FormatContext(3))
    assert(fmt == "１）カロナール錠３００ｍｇ　３錠\n　　分３　毎食後　５日分")
  }