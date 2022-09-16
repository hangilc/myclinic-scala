package dev.myclinic.scala.util

import org.scalatest.funsuite.AnyFunSuite

class CheckingDigitTest extends AnyFunSuite:
  test("valid checking digit"){
    assert(Misc.HokenUtil.hasValidCheckingDigit(138156))
    assert(Misc.HokenUtil.hasValidCheckingDigit(6138093))
    assert(Misc.HokenUtil.hasValidCheckingDigit(6110852))
    assert(Misc.HokenUtil.hasValidCheckingDigit(63132286))
    assert(Misc.HokenUtil.hasValidCheckingDigit(31130685))

    assert(Misc.HokenUtil.hasValidCheckingDigit(39131156))
    assert(Misc.HokenUtil.hasValidCheckingDigit(39131131))
    assert(Misc.HokenUtil.hasValidCheckingDigit(39131040))

    assert(Misc.HokenUtil.hasValidCheckingDigit(80137151))
    assert(Misc.HokenUtil.hasValidCheckingDigit(41139155))
    assert(Misc.HokenUtil.hasValidCheckingDigit(81137150))
  }

  test("invalid checking digit") {
    assert(!Misc.HokenUtil.hasValidCheckingDigit(138157))
    assert(!Misc.HokenUtil.hasValidCheckingDigit(6138092))
    assert(!Misc.HokenUtil.hasValidCheckingDigit(6110854))
    assert(!Misc.HokenUtil.hasValidCheckingDigit(63132280))
    assert(!Misc.HokenUtil.hasValidCheckingDigit(31130683))
  }