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
    val d = p.formatForDisp
    val e = """院外処方
      |Ｒｐ）
      |１）アドエア２５０ディスカス２８吸入用　２８ブリスター　１キット
      |　　１回１吸入、１日２回（朝、夕）
      |@0410対応＋
    """.stripMargin.trim
    assert(d == e)
  }

  test("should render fully") {
    val s = """院外処方
      |Ｒｐ）
      |　１）プラバスタチンナトリウム錠５ｍｇ　１錠
      |　　　分１　寝る前　２８日分
      |　２）オメプラゾール錠２０ｍｇ　１錠
      |　　　分１　朝食後　２８日分
      |　３）ゾルピデム酒石酸塩錠５ｍｇ　１錠
      |　　　分１　寝る前　２８日分
      |　４）デパス錠０．５ｍｇ　３錠
      |　　　分３　毎食後　２８日分
      |　５）ガスサール錠４０ｍｇ　３錠
      |　　　分３　毎食後　２８日分
      |　６）ロラタジンＯＤ（１０）　１錠
      |　　　分１　朝食後　２８日分
      |　７）アンブロキソール（１５）　３錠
      |　　　分３　毎食後　１４日分
      |　８）ロキソニンパップ１００ｍｇ　１０ｃｍ×１４ｃｍ　１４枚
      |　　　１日１回、１回１枚、患部に貼付
      |　９）ロキソニンテープ５０ｍｇ　７ｃｍ×１０ｃｍ　４９枚
      |　　　１日１回、１回１枚、患部に貼付
      |１０）インテバン外用液１％　５０ｍＬ
      |　　　１日２回患部に塗布
      |１１）カロナール（３００）　３錠
      |　　　分３　毎食後　１４日分
      |１２）デルモベート軟膏０．０５％　５ｇ
      |　　　１日２回患部に塗布
      |@memo:高一    
    """.stripMargin.trim
    val p = FormatShohousen.parse(s)
    assert(p.isInstanceOf[ShohouRegular])
    val r = p.asInstanceOf[ShohouRegular]
    val d = p.formatForPrint
    val e = """院外処方
      |Ｒｐ）
      |　１）プラバスタチンナトリウム錠５ｍｇ　　１錠
      |　　　分１　寝る前　　　　　　　　　　２８日分
      |　２）オメプラゾール錠２０ｍｇ　　　　　　１錠
      |　　　分１　朝食後　　　　　　　　　　２８日分
      |　３）ゾルピデム酒石酸塩錠５ｍｇ　　　　　１錠
      |　　　分１　寝る前　　　　　　　　　　２８日分
      |　４）デパス錠０．５ｍｇ　　　　　　　　　３錠
      |　　　分３　毎食後　　　　　　　　　　２８日分
      |　５）ガスサール錠４０ｍｇ　　　　　　　　３錠
      |　　　分３　毎食後　　　　　　　　　　２８日分
      |　６）ロラタジンＯＤ（１０）　　　　　　　１錠
      |　　　分１　朝食後　　　　　　　　　　２８日分
      |　７）アンブロキソール（１５）　　　　　　３錠
      |　　　分３　毎食後　　　　　　　　　　１４日分
      |　８）ロキソニンパップ１００ｍｇ　１０ｃｍ×１４ｃｍ
      |　　　　　　　　　　　　　　　　　　　　　１４枚
      |　　　１日１回、１回１枚、患部に貼付
      |　９）ロキソニンテープ５０ｍｇ　７ｃｍ×１０ｃｍ
      |　　　　　　　　　　　　　　　　　　　　　４９枚
      |　　　１日１回、１回１枚、患部に貼付
      |１０）インテバン外用液１％　　　　　　　　５０ｍＬ
      |　　　１日２回患部に塗布
      |１１）カロナール（３００）　　　　　　　　３錠
      |　　　分３　毎食後　　　　　　　　　　１４日分
      |１２）デルモベート軟膏０．０５％　　　　　５ｇ
      |　　　１日２回患部に塗布
      |@memo:高一""".stripMargin
    assert(e == d)
  }