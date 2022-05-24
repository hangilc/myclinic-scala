package dev.myclinic.scala.formatshohousen

import org.scalatest.funsuite.AnyFunSuite

class FormatTest extends AnyFunSuite:
  test("should parse") {
    val s = """院外処方
      |Ｒｐ）
      |１）アドエア２５０ディスカス２８吸入用　２８ブリスター　１キット
      |　　１回１吸入、１日２回（朝、夕）
      |@0410対応＋
    """.stripMargin.trim
    val p = FormatShohousen.parse(s)
    assert(p.isInstanceOf[ShohouRegular])
    val r = p.asInstanceOf[ShohouRegular]
    println(("shohou", r.parts))
    val d = p.formatForDisp
    val e = """院外処方
      |Ｒｐ）
      |１）アドエア２５０ディスカス２８吸入用　２８ブリスター　１キット
      |　　１回１吸入、１日２回（朝、夕）
      |@0410対応＋
    """.stripMargin.trim
    assert(d == e)
  }