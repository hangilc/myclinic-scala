package dev.myclinic.scala.formatshohousen

import org.scalatest.funsuite.AnyFunSuite

class FormatShohousenSpec extends AnyFunSuite:
  test("extractPrefix should extract prefix") {
    val s = """院外処方
      |Ｒｐ）
      |１）カロナール錠３００ｍｇ　３錠
      |　　分３　毎食後　５日分
      |""".stripMargin
    val extract = FormatShohousen.extractPrefix(s) 
    assert(extract == Some(
      "院外処方\nＲｐ）\n",
      "１）カロナール錠３００ｍｇ　３錠\n　　分３　毎食後　５日分\n"
    ))
  }