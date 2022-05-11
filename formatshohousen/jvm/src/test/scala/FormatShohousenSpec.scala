package dev.myclinic.scala.formatshohousen

import org.scalatest.funsuite.AnyFunSuite
import FormatShohousen.*
import ShohouSample.*
import dev.myclinic.scala.formatshohousen.naifuku.NaifukuSimple

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
    assert(item.firstLine(1, ctx) == List("１）カロナール錠３００ｍｇ　　　　　　　　３錠"))
  }