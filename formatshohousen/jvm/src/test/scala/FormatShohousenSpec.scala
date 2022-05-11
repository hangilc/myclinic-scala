package dev.myclinic.scala.formatshohousen

import org.scalatest.funsuite.AnyFunSuite
import FormatShohousen.*
import ShohouSample.*

class FormatShohousenSpec extends AnyFunSuite:
  test("should split sample1 item parts") {
    val parts = splitToParts(sample1)
    assert(parts == List(sample1))
  }

  test("shoule split sample2 to item parts") {
    val parts = splitToParts(sample2)
    assert(parts.size == 2)
    assert(parts(0) == "１）カロナール錠３００ｍｇ　３錠\n　　分３　毎食後　５日分")
    assert(parts(1) == "２）ロラタジン錠１０ｍｇ　１錠\n　　分１　朝食後　５日分")
  }