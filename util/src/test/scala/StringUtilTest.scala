package dev.myclinic.scala.util

import org.scalatest.funsuite.AnyFunSuite

class StringUtilTest extends AnyFunSuite:
  test("classify"){
    val pat = raw"\d+".r
    case class M(s: String)
    case class U(s: String)
    val src = "abc123def"
    val list = StringUtil.classify(pat, src, M.apply, U.apply)
    assert(list == List(U("abc"), M("123"), U("def")))
  }